package com.example.connecta.models

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class CallRecord(
    val id: String = "",
    val callerId: String = "",
    val callerName: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val partnerName: String = "",
    val partnerImage: String? = null,
    val callType: String = "Outgoing call", // "Incoming call", "Outgoing call", "Missed call"
    val timestamp: Long = 0L,
    val isMissed: Boolean = false,
    val durationSeconds: Int = 0
)
