package com.example.connecta.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecta.models.CommunityPost
import com.example.connecta.repository.ConnectaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: ConnectaRepository
) : ViewModel() {

    val currentUserId: String?
        get() = repository.currentUserId

    private val _posts = MutableStateFlow<List<CommunityPost>>(emptyList())
    val posts: StateFlow<List<CommunityPost>> = _posts.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            repository.getPostsFlow().collect { list ->
                _posts.value = list
            }
        }
    }

    fun createPost(caption: String, imageUri: Uri?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val uid = currentUserId ?: return onError("User not logged in")
        _isSubmitting.value = true

        viewModelScope.launch {
            val user = repository.getUserProfile(uid)
            val name = user?.name ?: "Connecta User"
            val image = user?.profileImage

            val result = repository.createPost(
                authorId = uid,
                authorName = name,
                authorImage = image,
                caption = caption,
                imageUri = imageUri
            )

            _isSubmitting.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                onError(it.localizedMessage ?: "Failed to publish post")
            }
        }
    }

    fun toggleLike(postId: String) {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            repository.toggleLikePost(postId, uid)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId)
        }
    }
}
