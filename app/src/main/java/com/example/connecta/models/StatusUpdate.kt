package com.example.connecta.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class StatusUpdate(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userImage: String? = null,
    val mediaUrl: String = "",
    val caption: String = "",
    val timestamp: Long = 0L,
    val expiresAt: Long = 0L,
    val viewerIds: List<String> = emptyList()
)
