package com.helpquest.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helpquest.auth.domain.EmailValidator
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toUiText
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_email_not_verified
import helpquest.feature.auth.presentation.generated.resources.error_invalid_credentials
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage,
    initialState: LoginState = LoginState()
) : ViewModel() {

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(initialState)
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                observeTextState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            LoginAction.OnForgotPasswordClick,
            LoginAction.OnSignUpClick -> {
                _state.update {
                    it.copy(
                        error = null
                    )
                }
            }
        }
    }


    private val isEmailValidFlow = snapshotFlow { state.value.emailTextState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()

    private val isPasswordNotBlankFlow =
        snapshotFlow { state.value.passwordTextState.text.toString() }
            .map { it.isNotBlank() }
            .distinctUntilChanged()

    private val isLoggingInFlow = state
        .map { it.isLoggingIn }
        .distinctUntilChanged()

    private fun observeTextState() {
        combine(
            isEmailValidFlow,
            isPasswordNotBlankFlow,
            isLoggingInFlow
        ) { isEmailValid, isPasswordNotBlank, isLoggingIn ->
            _state.update {
                it.copy(
                    canLogin = !isLoggingIn && isEmailValid && isPasswordNotBlank
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun login() {
        if (!state.value.canLogin) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoggingIn = true,
                    error = null
                )
            }

            val email = state.value.emailTextState.text.toString()
            val password = state.value.passwordTextState.text.toString()

            authService
                .login(
                    email = email,
                    password = password
                )
                .onSuccess { authInfo ->
                    sessionStorage.setAuthInfo(authInfo)
                    _state.update {
                        it.copy(
                            isLoggingIn = false
                        )
                    }
                    eventChannel.send(LoginEvent.Success)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_invalid_credentials)
                        DataError.Remote.FORBIDDEN -> UiText.Resource(Res.string.error_email_not_verified)
                        else -> error.toUiText()
                    }

                    _state.update {
                        it.copy(
                            error = errorMessage,
                            isLoggingIn = false
                        )
                    }
                }
        }
    }

}