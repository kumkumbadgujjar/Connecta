package com.example.connecta.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.connecta.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectaRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    val currentUserId: String?
        get() = auth.currentUser?.uid

    val currentUserPhone: String?
        get() = auth.currentUser?.phoneNumber

    // ==========================================
    // USER PROFILE
    // ==========================================

    suspend fun saveUserProfile(
        userId: String,
        name: String,
        status: String,
        imageUri: Uri? = null,
        imageBitmap: Bitmap? = null
    ): Result<PhoneAuthUser> {
        return try {
            var imageUrl: String? = null

            // Upload image to Firebase Storage if provided
            if (imageUri != null) {
                val ref = storage.reference.child("profile_images/${userId}.jpg")
                ref.putFile(imageUri).await()
                imageUrl = ref.downloadUrl.await().toString()
            } else if (imageBitmap != null) {
                val ref = storage.reference.child("profile_images/${userId}.jpg")
                val baos = ByteArrayOutputStream()
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
                ref.putBytes(baos.toByteArray()).await()
                imageUrl = ref.downloadUrl.await().toString()
            }

            val userDocRef = firestore.collection("users").document(userId)
            val existingDoc = userDocRef.get().await()

            val existingImageUrl = existingDoc.getString("profileImage")
            val finalImageUrl = imageUrl ?: existingImageUrl

            val now = System.currentTimeMillis()
            val createdAt = existingDoc.getLong("createdAt") ?: now

            val userProfile = PhoneAuthUser(
                userId = userId,
                phoneNumber = currentUserPhone ?: existingDoc.getString("phoneNumber") ?: "",
                name = name,
                status = status,
                profileImage = finalImageUrl,
                createdAt = createdAt,
                updatedAt = now
            )

            userDocRef.set(userProfile).await()
            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: String): PhoneAuthUser? {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            if (doc.exists()) {
                doc.toObject(PhoneAuthUser::class.java)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // ==========================================
    // CONVERSATIONS & MESSAGING
    // ==========================================

    fun getConversationsFlow(userId: String): Flow<List<Conversation>> = callbackFlow {
        val listener = firestore.collection("conversations")
            .whereArrayContains("participantIds", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val conversations = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Conversation::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.lastMessageTime } ?: emptyList()

                trySend(conversations)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createOrGetConversation(
        myUserId: String,
        myName: String,
        myImage: String,
        partnerPhone: String,
        partnerName: String,
        partnerImage: String
    ): Result<String> {
        return try {
            // Check if conversation already exists between these users
            val query = firestore.collection("conversations")
                .whereArrayContains("participantIds", myUserId)
                .get().await()

            val existing = query.documents.firstOrNull { doc ->
                val phones = doc.get("participantPhones") as? Map<*, *>
                phones?.containsValue(partnerPhone) == true
            }

            if (existing != null) {
                return Result.success(existing.id)
            }

            // Create new conversation
            val newId = UUID.randomUUID().toString()
            val conversation = Conversation(
                id = newId,
                participantIds = listOf(myUserId, partnerPhone),
                participantNames = mapOf(myUserId to myName, partnerPhone to partnerName),
                participantImages = mapOf(myUserId to myImage, partnerPhone to partnerImage),
                participantPhones = mapOf(myUserId to (currentUserPhone ?: ""), partnerPhone to partnerPhone),
                lastMessage = "Started a new conversation",
                lastMessageTime = System.currentTimeMillis(),
                lastSenderId = myUserId
            )

            firestore.collection("conversations").document(newId).set(conversation).await()
            Result.success(newId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getMessagesFlow(conversationId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Message::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        senderName: String,
        text: String
    ): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            val messageId = UUID.randomUUID().toString()
            val message = Message(
                id = messageId,
                conversationId = conversationId,
                senderId = senderId,
                senderName = senderName,
                text = text,
                timestamp = now,
                isRead = false
            )

            // Add message
            firestore.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .document(messageId)
                .set(message).await()

            // Update conversation last message
            firestore.collection("conversations")
                .document(conversationId)
                .update(
                    mapOf(
                        "lastMessage" to text,
                        "lastMessageTime" to now,
                        "lastSenderId" to senderId
                    )
                ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // UPDATES (STATUS STORIES - 24HR EXPIRY)
    // ==========================================

    fun getUpdatesFlow(): Flow<List<StatusUpdate>> = callbackFlow {
        val now = System.currentTimeMillis()
        val listener = firestore.collection("updates")
            .whereGreaterThan("expiresAt", now)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val updates = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(StatusUpdate::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.timestamp } ?: emptyList()

                trySend(updates)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createStatusUpdate(
        userId: String,
        userName: String,
        userImage: String?,
        caption: String,
        mediaUri: Uri?
    ): Result<Unit> {
        return try {
            val statusId = UUID.randomUUID().toString()
            var mediaUrl = ""

            if (mediaUri != null) {
                val ref = storage.reference.child("status_images/${statusId}.jpg")
                ref.putFile(mediaUri).await()
                mediaUrl = ref.downloadUrl.await().toString()
            }

            val now = System.currentTimeMillis()
            val expiry = now + (24 * 60 * 60 * 1000) // 24 hours

            val update = StatusUpdate(
                id = statusId,
                userId = userId,
                userName = userName,
                userImage = userImage,
                mediaUrl = mediaUrl,
                caption = caption,
                timestamp = now,
                expiresAt = expiry,
                viewerIds = emptyList()
            )

            firestore.collection("updates").document(statusId).set(update).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // COMMUNITY POSTS
    // ==========================================

    fun getPostsFlow(): Flow<List<CommunityPost>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(CommunityPost::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(posts)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createPost(
        authorId: String,
        authorName: String,
        authorImage: String?,
        caption: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            val postId = UUID.randomUUID().toString()
            var imageUrl = ""

            if (imageUri != null) {
                val ref = storage.reference.child("post_images/${postId}.jpg")
                ref.putFile(imageUri).await()
                imageUrl = ref.downloadUrl.await().toString()
            }

            val now = System.currentTimeMillis()
            val post = CommunityPost(
                id = postId,
                authorId = authorId,
                authorName = authorName,
                authorImage = authorImage,
                caption = caption,
                imageUrl = imageUrl,
                timestamp = now,
                likedBy = emptyList(),
                likesCount = 0
            )

            firestore.collection("posts").document(postId).set(post).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLikePost(postId: String, userId: String): Result<Unit> {
        return try {
            val postRef = firestore.collection("posts").document(postId)
            val doc = postRef.get().await()
            val likedBy = (doc.get("likedBy") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

            val updatedLikedBy = if (likedBy.contains(userId)) {
                likedBy - userId
            } else {
                likedBy + userId
            }

            postRef.update(
                mapOf(
                    "likedBy" to updatedLikedBy,
                    "likesCount" to updatedLikedBy.size
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            firestore.collection("posts").document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==========================================
    // CALL RECORDS
    // ==========================================

    fun getCallsFlow(userId: String): Flow<List<CallRecord>> = callbackFlow {
        val listener = firestore.collection("calls")
            .whereEqualTo("callerId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val calls = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(CallRecord::class.java)?.copy(id = doc.id)
                }?.sortedByDescending { it.timestamp } ?: emptyList()

                trySend(calls)
            }
        awaitClose { listener.remove() }
    }

    suspend fun logCall(
        callerId: String,
        callerName: String,
        partnerName: String,
        callType: String = "Outgoing call",
        isMissed: Boolean = false
    ): Result<Unit> {
        return try {
            val callId = UUID.randomUUID().toString()
            val record = CallRecord(
                id = callId,
                callerId = callerId,
                callerName = callerName,
                partnerName = partnerName,
                callType = callType,
                timestamp = System.currentTimeMillis(),
                isMissed = isMissed
            )
            firestore.collection("calls").document(callId).set(record).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
