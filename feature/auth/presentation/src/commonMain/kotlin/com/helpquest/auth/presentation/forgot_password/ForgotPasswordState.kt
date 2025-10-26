package com.helpquest.auth.presentation.forgot_password

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.presentation.util.UiText

data class ForgotPasswordState(
    val emailTextState: TextFieldState = TextFieldState(),
    val emailError: UiText? = null,
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val submitError: UiText? = null,
    val isEmailSentSuccessfully: Boolean = false,
)