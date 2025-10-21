package com.helpquest.auth.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.auth.presentation.login.LoginScreen
import com.helpquest.auth.presentation.login.LoginState
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun LoginScreenPreview() {
    HelpQuestTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
        )
    }
}