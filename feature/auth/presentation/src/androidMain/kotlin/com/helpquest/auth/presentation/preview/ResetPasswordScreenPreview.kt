package com.helpquest.auth.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.auth.presentation.reset_password.ResetPasswordScreen
import com.helpquest.auth.presentation.reset_password.ResetPasswordState
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun ResetPasswordPreview() {
    HelpQuestTheme {
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {},
        )
    }
}