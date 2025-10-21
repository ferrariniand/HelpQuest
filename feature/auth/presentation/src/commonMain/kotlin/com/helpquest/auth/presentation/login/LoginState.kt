package com.helpquest.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.core.presentation.util.UiText

data class LoginState(
    val emailTextState: TextFieldState = TextFieldState(),
    val passwordTextState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false,
    val error: UiText? = null,
)