package com.example.connecta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecta.models.Conversation
import com.example.connecta.models.Message
import com.example.connecta.repository.ConnectaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ConnectaRepository
) : ViewModel() {

    val currentUserId: String?
        get() = repository.currentUserId

    val currentUserPhone: String?
        get() = repository.currentUserPhone

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _currentMessages = MutableStateFlow<List<Message>>(emptyList())
    val currentMessages: StateFlow<List<Message>> = _currentMessages.asStateFlow()

    init {
        loadConversations()
    }

    fun loadConversations() {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            repository.getConversationsFlow(uid).collect { list ->
                _conversations.value = list
            }
        }
    }

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.getMessagesFlow(conversationId).collect { list ->
                _currentMessages.value = list
            }
        }
    }

    fun startNewConversation(
        partnerPhone: String,
        partnerName: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = currentUserId ?: return onError("User not authenticated")
        viewModelScope.launch {
            val myProfile = repository.getUserProfile(uid)
            val myName = myProfile?.name ?: "User"
            val myImage = myProfile?.profileImage ?: ""

            val result = repository.createOrGetConversation(
                myUserId = uid,
                myName = myName,
                myImage = myImage,
                partnerPhone = partnerPhone,
                partnerName = partnerName,
                partnerImage = ""
            )

            result.onSuccess { convId ->
                onSuccess(convId)
            }.onFailure { err ->
                onError(err.localizedMessage ?: "Failed to start conversation")
            }
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        val uid = currentUserId ?: return
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            val profile = repository.getUserProfile(uid)
            val name = profile?.name ?: "User"
            repository.sendMessage(conversationId, uid, name, text.trim())
        }
    }
}
