package com.helpquest.auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.brand.BrandLogo
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.containers_layouts.HelpQuestAdaptiveFormLayout
import com.helpquest.core.designsystem.components.containers_layouts.ScaffoldWithInsets
import com.helpquest.core.designsystem.components.textfields.HelpQuestPasswordTextField
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.core.presentation.util.UiText
import helpquest.core.designsystem.generated.resources.email
import helpquest.core.designsystem.generated.resources.password
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.create_account
import helpquest.feature.auth.presentation.generated.resources.email_placeholder
import helpquest.feature.auth.presentation.generated.resources.error_email_not_verified
import helpquest.feature.auth.presentation.generated.resources.forgot_password
import helpquest.feature.auth.presentation.generated.resources.help_quest
import helpquest.feature.auth.presentation.generated.resources.login
import helpquest.feature.auth.presentation.generated.resources.resend_verification_email
import helpquest.feature.auth.presentation.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes


@Composable
fun LoginRoot(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onResendVerificationEmailSuccess: (String) -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.Success -> onLoginSuccess()
            is LoginEvent.ResendVerificationEmailSuccess -> {
                onResendVerificationEmailSuccess(event.email)
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                LoginAction.OnSignUpClick -> onCreateAccountClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    ScaffoldWithInsets {
        HelpQuestAdaptiveFormLayout(
            topHeaderText = stringResource(Res.string.welcome_back),
            headerText = stringResource(Res.string.help_quest),
            errorText = state.error?.asString(),
            logo = {
                BrandLogo()
            },
            modifier = Modifier
                .fillMaxSize(),
            formContent = {
                AnimatedVisibility(
                    visible = state.showResendVerificationEmail
                ) {
                    if (state.showResendVerificationEmail) {
                        HelpQuestButton(
                            text = stringResource(Res.string.resend_verification_email),
                            onClick = {
                                onAction(LoginAction.OnResendVerificationEmailClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 6.dp
                                ),
                            enabled = !state.isResendingVerificationEmail,
                            isLoading = state.isResendingVerificationEmail,
                            style = HelpQuestButtonStyle.SECONDARY
                        )
                    }
                }
                HelpQuestTextField(
                    state = state.emailTextState,
                    placeholder = stringResource(Res.string.email_placeholder),
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = stringResource(DesignSystemRes.string.email)
                )
                Spacer(modifier = Modifier.height(16.dp))
                HelpQuestPasswordTextField(
                    state = state.passwordTextState,
                    placeholder = stringResource(DesignSystemRes.string.password),
                    isPasswordVisible = state.isPasswordVisible,
                    onToggleVisibilityClick = {
                        onAction(LoginAction.OnTogglePasswordVisibilityClick)
                    },
                    title = stringResource(DesignSystemRes.string.password),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                HelpQuestButton(
                    text = stringResource(Res.string.forgot_password),
                    style = HelpQuestButtonStyle.TEXT,
                    onClick = {
                        onAction(LoginAction.OnForgotPasswordClick)
                    },
                    enabled = true,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            },
            buttonsContent = {
                HelpQuestButton(
                    text = stringResource(Res.string.login),
                    onClick = {
                        onAction(LoginAction.OnLoginClick)
                    },
                    enabled = state.canLogin,
                    isLoading = state.isLoggingIn,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                HelpQuestButton(
                    text = stringResource(Res.string.create_account),
                    onClick = {
                        onAction(LoginAction.OnSignUpClick)
                    },
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun LoginScreenLightPreview() {
    HelpQuestTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun LoginScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}


@Composable
@Preview(
    showBackground = true
)
private fun LoginScreenErrorVerificationEmailLightPreview() {
    HelpQuestTheme {
        LoginScreen(
            state = LoginState(
                showResendVerificationEmail = true,
                error = UiText.Resource(Res.string.error_email_not_verified)
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun LoginScreenErrorVerificationEmailDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        LoginScreen(
            state = LoginState(
                showResendVerificationEmail = true,
                error = UiText.Resource(Res.string.error_email_not_verified)
            ),
            onAction = {}
        )
    }
}