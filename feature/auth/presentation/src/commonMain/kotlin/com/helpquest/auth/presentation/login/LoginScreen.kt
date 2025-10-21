package com.helpquest.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.brand.HelpQuestBrandLogo
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveFormLayout
import com.helpquest.core.designsystem.components.textfields.HelpQuestPasswordTextField
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.create_account
import helpquest.feature.auth.presentation.generated.resources.email
import helpquest.feature.auth.presentation.generated.resources.email_placeholder
import helpquest.feature.auth.presentation.generated.resources.forgot_password
import helpquest.feature.auth.presentation.generated.resources.help_quest
import helpquest.feature.auth.presentation.generated.resources.login
import helpquest.feature.auth.presentation.generated.resources.password
import helpquest.feature.auth.presentation.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginRoot(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.Success -> onLoginSuccess()
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
    Scaffold(
        contentWindowInsets = WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .union(WindowInsets.navigationBars)
            .union(WindowInsets.ime),
    ) { innerPadding ->
        HelpQuestAdaptiveFormLayout(
            topHeaderText = stringResource(Res.string.welcome_back),
            headerText = stringResource(Res.string.help_quest),
            errorText = state.error?.asString(),
            logo = {
                HelpQuestBrandLogo()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            formContent = {
                HelpQuestTextField(
                    state = state.emailTextState,
                    placeholder = stringResource(Res.string.email_placeholder),
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = stringResource(Res.string.email)
                )
                Spacer(modifier = Modifier.height(16.dp))
                HelpQuestPasswordTextField(
                    state = state.passwordTextState,
                    placeholder = stringResource(Res.string.password),
                    isPasswordVisible = state.isPasswordVisible,
                    onToggleVisibilityClick = {
                        onAction(LoginAction.OnTogglePasswordVisibilityClick)
                    },
                    title = stringResource(Res.string.password),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.forgot_password),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            onAction(LoginAction.OnForgotPasswordClick)
                        }
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