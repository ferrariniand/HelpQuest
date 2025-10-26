package com.helpquest.auth.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordScreen
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordState
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun ForgotPasswordPreview() {
    HelpQuestTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {},
        )
    }
}