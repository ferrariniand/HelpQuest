package com.helpquest.auth.presentation.email_verification

sealed interface EmailVerificationAction {
    data object OnLoginClick : EmailVerificationAction
    data object OnResendVerificationEmailClick : EmailVerificationAction
    data object OnCloseClick : EmailVerificationAction
}