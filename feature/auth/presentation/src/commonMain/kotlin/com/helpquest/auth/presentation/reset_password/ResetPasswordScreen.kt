package com.helpquest.auth.presentation.reset_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveFormLayout
import com.helpquest.core.designsystem.components.layouts.ScaffoldWithInsets
import com.helpquest.core.designsystem.components.textfields.HelpQuestPasswordTextField
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.close
import helpquest.feature.auth.presentation.generated.resources.confirm_new_password
import helpquest.feature.auth.presentation.generated.resources.new_password
import helpquest.feature.auth.presentation.generated.resources.password
import helpquest.feature.auth.presentation.generated.resources.password_hint
import helpquest.feature.auth.presentation.generated.resources.reset_password_successfully
import helpquest.feature.auth.presentation.generated.resources.set_new_password
import helpquest.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordRoot(
    onCloseClick: () -> Unit,
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResetPasswordScreen(
        state = state,
        onAction = { action ->
            when (action) {
                ResetPasswordAction.OnCloseClick -> onCloseClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    ScaffoldWithInsets {
        HelpQuestAdaptiveFormLayout(
            headerText = stringResource(Res.string.set_new_password),
            errorText = state.submitError?.asString(),
            successText = if (state.isResetSuccessful) {
                stringResource(Res.string.reset_password_successfully)
            } else {
                null
            },
            modifier = Modifier
                .fillMaxSize(),
            formContent = {
                HelpQuestPasswordTextField(
                    state = state.passwordTextState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = stringResource(Res.string.password),
                    title = stringResource(Res.string.new_password),
                    supportingText = state.passwordError?.asString()
                        ?: stringResource(Res.string.password_hint),
                    isError = state.passwordError != null,
                    onFocusChanged = { isFocused ->
                        onAction(ResetPasswordAction.OnInputTextFocusGain)
                    },
                    onDebouncedValueChange = {
                        onAction(ResetPasswordAction.OnInputTextFocusGain)
                    },
                    onToggleVisibilityClick = {
                        onAction(ResetPasswordAction.OnTogglePasswordVisibilityClick)
                    },
                    isPasswordVisible = state.isPasswordVisible
                )
                Spacer(modifier = Modifier.height(16.dp))
                HelpQuestPasswordTextField(
                    state = state.confirmPasswordTextState,
                    placeholder = stringResource(Res.string.password),
                    title = stringResource(Res.string.confirm_new_password),
                    supportingText = state.confirmPasswordError?.asString(),
                    isError = state.confirmPasswordError != null,
                    onFocusChanged = { isFocused ->
                        onAction(ResetPasswordAction.OnInputTextFocusGain)
                    },
                    onDebouncedValueChange = {
                        onAction(ResetPasswordAction.OnInputTextFocusGain)
                    },
                    onToggleVisibilityClick = {
                        onAction(ResetPasswordAction.OnToggleConfirmPasswordVisibilityClick)
                    },
                    isPasswordVisible = state.isConfirmPasswordVisible
                )
            },
            buttonsContent = {
                HelpQuestButton(
                    text = stringResource(Res.string.submit),
                    onClick = {
                        onAction(ResetPasswordAction.OnSubmitClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.canSubmit,
                    isLoading = state.isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))
                HelpQuestButton(
                    text = stringResource(Res.string.close),
                    onClick = {
                        onAction(ResetPasswordAction.OnCloseClick)
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
private fun ResetPasswordScreenLightPreview() {
    HelpQuestTheme {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun ResetPasswordScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}