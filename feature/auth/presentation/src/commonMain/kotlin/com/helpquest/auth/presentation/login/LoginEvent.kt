package com.helpquest.auth.presentation.login

sealed interface LoginEvent {
    data object Success : LoginEvent
    data class ResendVerificationEmailSuccess(val email: String) : LoginEvent
}