package com.helpquest.auth.presentation.forgot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.auth.domain.EmailValidator
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_invalid_email
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

class ForgotPasswordViewModel(
    private val authService: AuthService,
    initialState: ForgotPasswordState = ForgotPasswordState()
) : ViewModel() {

    private var hasLoadedInitialData = false

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

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnSubmitClick -> submitForgotPasswordRequest()
            is ForgotPasswordAction.OnInputTextFocusGain -> checkEmailInputs()
            ForgotPasswordAction.OnBackClick -> {
                _state.update {
                    it.copy(
                        submitError = null,
                        isEmailSentSuccessfully = false
                    )
                }
            }
        }
    }

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()

    private val isLoadingFlow = state
        .map { it.isLoading }
        .distinctUntilChanged()

    private fun observeValidationStates() {
        combine(
            isEmailValidFlow,
            isLoadingFlow
        ) { isEmailValid, isLoading ->
            _state.update {
                it.copy(
                    canSubmit = !isLoading && isEmailValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun checkEmailInputs() {
        _state.update {
            it.copy(
                emailError = null,
            )
        }

        val email = state.value.emailTextState.text.toString()

        val isEmailValidOrBlank = email.isBlank() || EmailValidator.validate(email)

        val emailError = if (!isEmailValidOrBlank) {
            UiText.Resource(Res.string.error_invalid_email)
        } else null

        _state.update {
            it.copy(
                emailError = emailError
            )
        }
    }

    private fun submitForgotPasswordRequest() {
        if (!state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isEmailSentSuccessfully = false,
                    submitError = null
                )
            }

            val email = state.value.emailTextState.text.toString()
            authService
                .forgotPassword(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isEmailSentSuccessfully = true,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            submitError = error.toUiText(),
                            isEmailSentSuccessfully = false,
                            isLoading = false
                        )
                    }
                }
        }
    }
}