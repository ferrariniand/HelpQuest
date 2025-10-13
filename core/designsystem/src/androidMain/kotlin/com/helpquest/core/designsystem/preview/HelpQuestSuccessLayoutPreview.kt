package com.helpquest.core.designsystem.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.layouts.HelpQuestSuccessLayout
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
fun HelpQuestSuccessLayoutPreview() {
    HelpQuestTheme {
        HelpQuestSuccessLayout(
            title = "Help Quest account successfully created!",
            description = "We've sent a verification email to email@test.com",
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
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = true,
                    isLoading = false,
                    style = HelpQuestButtonStyle.SECONDARY
                )
            },
            secondaryError = "text to show some error",
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp)
        )
    }
}