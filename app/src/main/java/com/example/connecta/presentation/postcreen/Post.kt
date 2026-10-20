package com.example.connecta.presentation.postscreen

data class Post(
    val userName: String,
    val userImage: Int,
    val postImage: Int,
    val caption: String,
    val time: String,
    val likes: Int
)