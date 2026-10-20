package com.example.connecta.presentation.callscreen

data class Call(
    val userName: String,
    val userImage: Int,
    val callType: String,
    val time: String,
    val isMissed: Boolean = false
)