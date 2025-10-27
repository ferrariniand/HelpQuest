package com.helpquest.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.presentation.util.UiText

data class ResetPasswordState(
    val passwordTextState: TextFieldState = TextFieldState(),
    val passwordError: UiText? = null,
    val confirmPasswordTextState: TextFieldState = TextFieldState(),
    val confirmPasswordError: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val submitError: UiText? = null,
    val isResetSuccessful: Boolean = false,
)