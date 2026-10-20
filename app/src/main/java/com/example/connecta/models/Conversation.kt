package com.example.connecta.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Conversation(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val participantNames: Map<String, String> = emptyMap(),
    val participantImages: Map<String, String> = emptyMap(),
    val participantPhones: Map<String, String> = emptyMap(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val lastSenderId: String = "",
    val unreadCounts: Map<String, Int> = emptyMap()
)
