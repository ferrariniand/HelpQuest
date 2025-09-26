package com.helpquest.core.designsystem.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.core.designsystem.components.brand.HelpQuestBrandLogo
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveFormLayout
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
fun HelpQuestAdaptiveFormLayoutPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveFormLayout(
            headerText = "Welcome to Help Quest!",
            errorText = "Login failed!",
            logo = { HelpQuestBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}