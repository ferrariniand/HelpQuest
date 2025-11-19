package com.helpquest.core.designsystem.components.generic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpquest.core.presentation.util.currentDeviceConfiguration

@Composable
fun GenericPageHeaderSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val isSmallScreenHeight = deviceConfiguration.isSmallScreenHeight
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = if (isSmallScreenHeight) 56.dp else 72.dp)
                .padding(
                    vertical = if (isSmallScreenHeight) 8.dp else 16.dp,
                    horizontal = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        HelpQuestHorizontalDivider()
    }
}