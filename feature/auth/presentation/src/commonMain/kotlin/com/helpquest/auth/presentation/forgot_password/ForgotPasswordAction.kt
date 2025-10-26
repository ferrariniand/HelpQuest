package com.helpquest.auth.presentation.forgot_password

sealed interface ForgotPasswordAction {
    data object OnSubmitClick : ForgotPasswordAction
    data object OnInputTextFocusGain : ForgotPasswordAction
    data object OnBackClick : ForgotPasswordAction
}