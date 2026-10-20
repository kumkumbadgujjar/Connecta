package com.example.connecta.models

import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class PhoneAuthUser(
    val userId: String = "",
    val phoneNumber: String = "",
    val name: String = "",
    val status: String = "",
    val profileImage: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)