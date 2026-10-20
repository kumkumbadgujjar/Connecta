package com.example.connecta.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connecta.models.PhoneAuthUser
import com.example.connecta.repository.ConnectaRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class AuthState {
    object Ideal : AuthState()
    object Loading : AuthState()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken? = null
    ) : AuthState()
    data class Success(val user: PhoneAuthUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: ConnectaRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Ideal)
    val authState = _authState.asStateFlow()

    private val _resendCountdown = MutableStateFlow(0)
    val resendCountdown = _resendCountdown.asStateFlow()

    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var lastPhoneNumber: String = ""
    private var countdownJob: Job? = null

    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    val isUserSignedIn: Boolean
        get() = firebaseAuth.currentUser != null

    init {
        checkExistingSession()
    }

    fun checkExistingSession() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {
                val profile = repository.getUserProfile(user.uid)
                if (profile != null) {
                    _authState.value = AuthState.Success(profile)
                } else {
                    _authState.value = AuthState.Success(
                        PhoneAuthUser(
                            userId = user.uid,
                            phoneNumber = user.phoneNumber ?: ""
                        )
                    )
                }
            }
        }
    }

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        lastPhoneNumber = phoneNumber

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.d("PhoneAuth", "SMS verification code sent successfully. ID: $verificationId")
                resendToken = token
                _authState.value = AuthState.CodeSent(verificationId, token)
                startCountdown()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("PhoneAuth", "Instant verification completed automatically.")
                signInWithPhoneCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("PhoneAuth", "Verification request failed", e)
                val userFriendlyMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "The phone number format is invalid. Please check and try again."
                    is FirebaseTooManyRequestsException -> "SMS quota exceeded or too many attempts. Please wait a few minutes before trying again."
                    else -> e.localizedMessage ?: "Failed to send SMS verification code."
                }
                _authState.value = AuthState.Error(userFriendlyMessage)
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(activity: Activity) {
        if (_resendCountdown.value > 0 || lastPhoneNumber.isEmpty()) return

        val token = resendToken
        if (token == null) {
            sendVerificationCode(lastPhoneNumber, activity)
            return
        }

        _authState.value = AuthState.Loading

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                verificationId: String,
                newToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, newToken)
                resendToken = newToken
                _authState.value = AuthState.CodeSent(verificationId, newToken)
                startCountdown()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Resend failed.")
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(lastPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            _resendCountdown.value = 60
            while (_resendCountdown.value > 0) {
                delay(1000)
                _resendCountdown.value -= 1
            }
        }
    }

    fun verifyCode(code: String) {
        val currentState = _authState.value
        val verificationId = (currentState as? AuthState.CodeSent)?.verificationId

        if (verificationId.isNullOrEmpty()) {
            _authState.value = AuthState.Error("Session expired. Please request a new code.")
            return
        }

        _authState.value = AuthState.Loading
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneCredential(credential)
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        viewModelScope.launch {
                            val existingProfile = repository.getUserProfile(firebaseUser.uid)
                            val user = existingProfile ?: PhoneAuthUser(
                                userId = firebaseUser.uid,
                                phoneNumber = firebaseUser.phoneNumber ?: lastPhoneNumber
                            )
                            _authState.value = AuthState.Success(user)
                        }
                    } else {
                        _authState.value = AuthState.Error("Sign in completed but user profile was null.")
                    }
                } else {
                    val message = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "The entered SMS code is incorrect. Please verify and re-enter."
                        else -> task.exception?.localizedMessage ?: "Verification failed. Please try again."
                    }
                    _authState.value = AuthState.Error(message)
                }
            }
    }

    fun saveProfile(
        name: String,
        status: String,
        imageUri: Uri? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = currentUserId ?: return onError("User is not signed in.")

        viewModelScope.launch {
            val result = repository.saveUserProfile(
                userId = uid,
                name = name,
                status = status,
                imageUri = imageUri
            )

            result.onSuccess {
                _authState.value = AuthState.Success(it)
                onSuccess()
            }.onFailure {
                onError(it.localizedMessage ?: "Failed to save profile.")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Ideal
    }

    fun signOut(context: Context) {
        firebaseAuth.signOut()
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        _authState.value = AuthState.Ideal
    }
}