package com.example.connecta.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecta.models.StatusUpdate
import com.example.connecta.repository.ConnectaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val repository: ConnectaRepository
) : ViewModel() {

    val currentUserId: String?
        get() = repository.currentUserId

    private val _updates = MutableStateFlow<List<StatusUpdate>>(emptyList())
    val updates: StateFlow<List<StatusUpdate>> = _updates.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    init {
        loadUpdates()
    }

    private fun loadUpdates() {
        viewModelScope.launch {
            repository.getUpdatesFlow().collect { list ->
                _updates.value = list
            }
        }
    }

    fun createStatus(caption: String, mediaUri: Uri?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val uid = currentUserId ?: return onError("User not logged in")
        _isSubmitting.value = true

        viewModelScope.launch {
            val user = repository.getUserProfile(uid)
            val name = user?.name ?: "Connecta User"
            val image = user?.profileImage

            val result = repository.createStatusUpdate(
                userId = uid,
                userName = name,
                userImage = image,
                caption = caption,
                mediaUri = mediaUri
            )

            _isSubmitting.value = false
            result.onSuccess {
                onSuccess()
            }.onFailure {
                onError(it.localizedMessage ?: "Failed to upload status update")
            }
        }
    }
}
