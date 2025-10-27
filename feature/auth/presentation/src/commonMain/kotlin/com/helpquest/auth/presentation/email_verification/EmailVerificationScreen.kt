package com.helpquest.auth.presentation.email_verification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.icons.HelpQuestFailureIcon
import com.helpquest.core.designsystem.components.icons.HelpQuestSuccessIcon
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveResultLayout
import com.helpquest.core.designsystem.components.layouts.HelpQuestLoadingLayout
import com.helpquest.core.designsystem.components.layouts.ScaffoldWithInsets
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.close
import helpquest.feature.auth.presentation.generated.resources.email_verified_fail_description
import helpquest.feature.auth.presentation.generated.resources.email_verified_failed
import helpquest.feature.auth.presentation.generated.resources.email_verified_success_description
import helpquest.feature.auth.presentation.generated.resources.email_verified_successfully
import helpquest.feature.auth.presentation.generated.resources.login
import helpquest.feature.auth.presentation.generated.resources.verifying_account
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationRoot(
    onLoginClick: () -> Unit,
    onCloseClick: () -> Unit,
    viewModel: EmailVerificationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    //TODO("Handle events")
//    ObserveAsEvents(viewModel.events) { event ->
//        when (event) {
//
//        }
//    }

    EmailVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is EmailVerificationAction.OnCloseClick -> onCloseClick()
                is EmailVerificationAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun EmailVerificationScreen(
    state: EmailVerificationState,
    onAction: (EmailVerificationAction) -> Unit,
) {
    ScaffoldWithInsets {
        when {
            state.isVerifying -> {
                HelpQuestLoadingLayout(
                    text = stringResource(Res.string.verifying_account),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            state.isVerified -> {
                HelpQuestAdaptiveResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_success_description),
                    resultLogo = {
                        HelpQuestSuccessIcon()
                    },
                    primaryButton = {
                        HelpQuestButton(
                            text = stringResource(Res.string.login),
                            onClick = {
                                onAction(EmailVerificationAction.OnLoginClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            else -> {
                HelpQuestAdaptiveResultLayout(
                    title = stringResource(Res.string.email_verified_failed),
                    description = stringResource(Res.string.email_verified_fail_description),
                    resultLogo = {
                        HelpQuestFailureIcon()
                    },
                    primaryButton = {
                        HelpQuestButton(
                            text = stringResource(Res.string.close),
                            onClick = {
                                onAction(EmailVerificationAction.OnCloseClick)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = HelpQuestButtonStyle.SECONDARY
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
@Preview(
    showBackground = true
)
private fun EmailVerificationScreenSuccessLightPreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
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
private fun EmailVerificationScreenSuccessDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun EmailVerificationScreenLoadingLightPreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
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
private fun EmailVerificationScreenLoadingDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun EmailVerificationScreenFailureLightPreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun EmailVerificationScreenFailureDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}