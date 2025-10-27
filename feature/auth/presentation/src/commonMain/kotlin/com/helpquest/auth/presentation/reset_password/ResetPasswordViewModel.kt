package com.helpquest.auth.presentation.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.domain.validation.PasswordValidator
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_different_confirm_password
import helpquest.feature.auth.presentation.generated.resources.error_invalid_password
import helpquest.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import helpquest.feature.auth.presentation.generated.resources.error_same_password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle,
    initialState: ResetPasswordState = ResetPasswordState()
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val token = savedStateHandle.get<String>("token")
        ?: throw IllegalStateException("No password reset token")

    private val _state = MutableStateFlow(initialState)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeValidationStates()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = initialState
        )

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.OnSubmitClick -> resetPassword()
            ResetPasswordAction.OnInputTextFocusGain -> validateFormInputs()
            ResetPasswordAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            ResetPasswordAction.OnToggleConfirmPasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isConfirmPasswordVisible = !it.isConfirmPasswordVisible
                    )
                }
            }

            ResetPasswordAction.OnCloseClick -> {
                _state.update {
                    it.copy(
                        submitError = null
                    )
                }
            }
        }
    }

    private val isNewPasswordValidFlow =
        snapshotFlow { state.value.passwordTextState.text.toString() }
            .map { password -> PasswordValidator.validate(password).isValidPassword }
            .distinctUntilChanged()

    private val newPasswordFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .distinctUntilChanged()

    private val confirmPasswordFlow =
        snapshotFlow { state.value.confirmPasswordTextState.text.toString() }
            .distinctUntilChanged()

    private val isLoadingFlow = state
        .map { it.isLoading }
        .distinctUntilChanged()

    private fun observeValidationStates() {
        combine(
            isNewPasswordValidFlow,
            newPasswordFlow,
            confirmPasswordFlow,
            isLoadingFlow
        ) { isPasswordValid, newPassword, confirmPassword, isLoading ->
            _state.update {
                it.copy(
                    canSubmit = !isLoading && isPasswordValid && (newPassword == confirmPassword)
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun validateFormInputs() {
        _state.update {
            it.copy(
                passwordError = null,
                confirmPasswordError = null
            )
        }

        val currentState = state.value
        val newPassword = currentState.passwordTextState.text.toString()
        val confirmPassword = currentState.confirmPasswordTextState.text.toString()

        val newPasswordValidationState = PasswordValidator.validate(newPassword)
        val isNewPasswordValid = newPasswordValidationState.isValidPassword
        val isNewPasswordValidOrEmpty =
            newPassword.isBlank() || isNewPasswordValid
        val isConfirmPasswordValid = (newPassword == confirmPassword)
        val isConfirmPasswordValidOrEmpty =
            confirmPassword.isBlank() || isConfirmPasswordValid

        val newPasswordError = if (!isNewPasswordValidOrEmpty) {
            UiText.Resource(Res.string.error_invalid_password)
        } else null
        val confirmPasswordError = if (!isConfirmPasswordValidOrEmpty) {
            UiText.Resource(Res.string.error_different_confirm_password)
        } else null

        _state.update {
            it.copy(
                passwordError = newPasswordError,
                confirmPasswordError = confirmPasswordError
            )
        }
    }

    private fun resetPassword() {
        if (!state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    submitError = null,
                    isResetSuccessful = false
                )
            }

            val newPassword = state.value.passwordTextState.text.toString()
            authService
                .resetPassword(
                    newPassword = newPassword,
                    token = token
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isResetSuccessful = true,
                            submitError = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorText = when (error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_reset_password_token_invalid)
                        DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_same_password)
                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isResetSuccessful = false,
                            submitError = errorText,
                        )
                    }

                }
        }
    }

}