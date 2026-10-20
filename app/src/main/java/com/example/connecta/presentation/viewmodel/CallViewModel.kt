package com.example.connecta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecta.models.CallRecord
import com.example.connecta.repository.ConnectaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val repository: ConnectaRepository
) : ViewModel() {

    val currentUserId: String?
        get() = repository.currentUserId

    private val _calls = MutableStateFlow<List<CallRecord>>(emptyList())
    val calls: StateFlow<List<CallRecord>> = _calls.asStateFlow()

    init {
        loadCalls()
    }

    private fun loadCalls() {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            repository.getCallsFlow(uid).collect { list ->
                _calls.value = list
            }
        }
    }

    fun logCall(
        partnerName: String,
        callType: String = "Outgoing call",
        isMissed: Boolean = false
    ) {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            val user = repository.getUserProfile(uid)
            val name = user?.name ?: "User"
            repository.logCall(
                callerId = uid,
                callerName = name,
                partnerName = partnerName,
                callType = callType,
                isMissed = isMissed
            )
        }
    }
}
