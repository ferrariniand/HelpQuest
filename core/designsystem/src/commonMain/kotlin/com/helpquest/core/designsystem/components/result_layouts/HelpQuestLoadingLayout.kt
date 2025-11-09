package com.helpquest.core.designsystem.components.result_layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.helpquest.core.designsystem.components.icons.HelpQuestPageLoadingIndicator

@Composable
fun HelpQuestLoadingLayout(
    text: String,
    modifier: Modifier = Modifier,
) {
    HelpQuestAdaptiveResultLayout(
        description = text,
        resultLogo = {
            HelpQuestPageLoadingIndicator()
        },
        isLoadings = true,
        modifier = modifier
    )
}