package com.helpquest.core.designsystem.components.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.theme.extended

@Composable
fun ColumnScope.HelpQuestResultBodyLayout(
    description: String,
    primaryButton: (@Composable () -> Unit)? = null,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    isMobileAndNotLoading: Boolean = false
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.extended.textSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
    )
    if (isMobileAndNotLoading) {
        Spacer(modifier = Modifier.weight(1f))
    }
    if (primaryButton != null) {
        Spacer(modifier = Modifier.height(16.dp))
        primaryButton()
    }

    if (secondaryButton != null) {
        Spacer(modifier = Modifier.height(8.dp))
        secondaryButton()
        if (secondaryError != null) {
            Spacer(modifier = Modifier.height(8.dp))
        }
        AnimatedVisibility(
            visible = secondaryError != null
        ) {
            if (secondaryError != null) {
                Text(
                    text = secondaryError,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}