package com.helpquest.auth.presentation.register_success

import com.helpquest.core.presentation.util.UiText

data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isRegisterFlow: Boolean = false,
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationError: UiText? = null,
)