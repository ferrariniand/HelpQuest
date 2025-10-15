package com.helpquest.core.designsystem.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.icons.HelpQuestSuccessIcon
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveResultLayout
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
fun HelpQuestAdaptiveResultLayoutPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            title = "Registration successful!",
            description = "Test description disposed on more than one line and showing success message",
            primaryButton = {
                HelpQuestButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryError = "This is an error",
            resultLogo = {
                HelpQuestSuccessIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}