package com.helpquest.core.designsystem.preview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.layouts.HelpQuestAdaptiveResultLayout
import com.helpquest.core.designsystem.theme.HelpQuestTheme

@Composable
@PreviewLightDark
@PreviewScreenSizes
fun HelpQuestAdaptiveResultLayoutPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Registration successful!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        )
    }
}