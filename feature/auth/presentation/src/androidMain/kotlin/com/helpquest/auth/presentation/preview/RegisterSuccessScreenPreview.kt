package com.helpquest.auth.presentation.preview

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.auth.presentation.register_success.RegisterSuccessScreen
import com.helpquest.auth.presentation.register_success.RegisterSuccessState
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun RegisterSuccessScreenPreview() {
    HelpQuestTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}