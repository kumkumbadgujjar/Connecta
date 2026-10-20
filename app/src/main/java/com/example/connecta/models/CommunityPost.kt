package com.example.connecta.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class CommunityPost(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorImage: String? = null,
    val caption: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0L,
    val likedBy: List<String> = emptyList(),
    val likesCount: Int = 0
)
