package com.helpquest.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.components.icons.HelpQuestSuccessIcon
import com.helpquest.core.designsystem.components.result_layouts.HelpQuestAdaptiveResultLayout
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.core.presentation.util.UiText
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.account_successfully_created
import helpquest.feature.auth.presentation.generated.resources.login
import helpquest.feature.auth.presentation.generated.resources.resend_verification_email
import helpquest.feature.auth.presentation.generated.resources.resent_verification_email
import helpquest.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessRoot(
    onLoginClick: () -> Unit,
    viewModel: RegisterSuccessViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterSuccessEvent.ResendVerificationEmailSuccess -> {
                snackbarHostState.showSnackbar(
                    message = getString(
                        resource = Res.string.resent_verification_email
                    )
                )
            }
        }
    }

    RegisterSuccessScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RegisterSuccessAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    SnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        HelpQuestAdaptiveResultLayout(
            title = stringResource(Res.string.account_successfully_created),
            description = stringResource(
                Res.string.verification_email_sent_to_x,
                state.registeredEmail
            ),
            primaryButton = {
                HelpQuestButton(
                    text = stringResource(Res.string.login),
                    onClick = {
                        onAction(RegisterSuccessAction.OnLoginClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = stringResource(Res.string.resend_verification_email),
                    onClick = {
                        onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !state.isResendingVerificationEmail,
                    isLoading = state.isResendingVerificationEmail,
                    style = HelpQuestButtonStyle.SECONDARY
                )
            },
            secondaryError = state.resendVerificationError?.asString(),
            resultLogo = {
                HelpQuestSuccessIcon()
            },
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun RegisterSuccessScreenLightPreview() {
    HelpQuestTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun RegisterSuccessScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun RegisterSuccessScreenErrorLightPreview() {
    HelpQuestTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
                isResendingVerificationEmail = false,
                resendVerificationError = UiText.DynamicString("This is an error")
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun RegisterSuccessScreenErrorDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
                isResendingVerificationEmail = false,
                resendVerificationError = UiText.DynamicString("This is an error")
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun RegisterSuccessScreenLoadingLightPreview() {
    HelpQuestTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
                isResendingVerificationEmail = true
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun RegisterSuccessScreenLoadingDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "prova@test.com",
                isResendingVerificationEmail = true,
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

