package com.helpquest.auth.presentation.reset_password

sealed interface ResetPasswordAction {
    data object OnSubmitClick : ResetPasswordAction
    data object OnTogglePasswordVisibilityClick : ResetPasswordAction
    data object OnInputTextFocusGain : ResetPasswordAction
    data object OnToggleConfirmPasswordVisibilityClick : ResetPasswordAction
    data object OnCloseClick : ResetPasswordAction
}