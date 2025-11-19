package com.helpquest.core.designsystem.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.presentation.util.currentDeviceConfiguration

@Composable
fun DialogSheetButtonSection(
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable () -> Unit,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val isSmallScreenHeight = deviceConfiguration.isSmallScreenHeight
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = if (isSmallScreenHeight) 8.dp else 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
        ) {
            secondaryButton()
            primaryButton()
        }
        AnimatedVisibility(
            visible = error != null
        ) {
            Column {
                Spacer(modifier = Modifier.height(if (isSmallScreenHeight) 4.dp else 8.dp))
                error?.let {
                    Text(
                        text = error,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}