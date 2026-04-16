package com.helpquest.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.containers_layouts.HelpQuestAdaptiveFormLayout
import com.helpquest.core.designsystem.components.containers_layouts.ScaffoldWithInsets
import com.helpquest.core.designsystem.components.textfields.HelpQuestTextField
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import helpquest.core.designsystem.generated.resources.email
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.back
import helpquest.feature.auth.presentation.generated.resources.email_placeholder
import helpquest.feature.auth.presentation.generated.resources.forgot_password
import helpquest.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully
import helpquest.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ForgotPasswordRoot(
    onBackClick: () -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ForgotPasswordAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
) {
    ScaffoldWithInsets {
        HelpQuestAdaptiveFormLayout(
            headerText = stringResource(Res.string.forgot_password),
            errorText = state.submitError?.asString(),
            successText = if (state.isEmailSentSuccessfully) {
                stringResource(Res.string.forgot_password_email_sent_successfully)
            } else {
                null
            },
            modifier = Modifier
                .fillMaxSize(),
            formContent = {
                HelpQuestTextField(
                    state = state.emailTextState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = stringResource(Res.string.email_placeholder),
                    title = stringResource(DesignSystemRes.string.email),
                    isError = state.emailError != null,
                    supportingText = state.emailError?.asString(),
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    onFocusChanged = { isFocused ->
                        onAction(ForgotPasswordAction.OnInputTextFocusGain)
                    },
                    onDebouncedValueChange = {
                        onAction(ForgotPasswordAction.OnInputTextFocusGain)
                    },
                )
            },
            buttonsContent = {
                HelpQuestButton(
                    text = stringResource(Res.string.submit),
                    onClick = {
                        onAction(ForgotPasswordAction.OnSubmitClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.canSubmit,
                    isLoading = state.isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))
                HelpQuestButton(
                    text = stringResource(Res.string.back),
                    onClick = {
                        onAction(ForgotPasswordAction.OnBackClick)
                    },
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            isLongScreen = false
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun ForgotPasswordScreenLightPreview() {
    HelpQuestTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun ForgotPasswordScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}