package com.helpquest.auth.presentation.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.auth.presentation.email_verification.EmailVerificationScreen
import com.helpquest.auth.presentation.email_verification.EmailVerificationState
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun EmailVerificationScreenFailurePreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {},
        )
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun EmailVerificationScreenSuccessPreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = false,
                isVerified = true
            ),
            onAction = {},
        )
    }
}

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun EmailVerificationScreenLoadingPreview() {
    HelpQuestTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true,
                isVerified = false
            ),
            onAction = {},
        )
    }
}