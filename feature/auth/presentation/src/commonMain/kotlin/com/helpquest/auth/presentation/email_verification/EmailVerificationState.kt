package com.helpquest.auth.presentation.email_verification

data class EmailVerificationState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false,
)