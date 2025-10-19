package com.helpquest.core.designsystem.components.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.HeaderSection(
    topHeaderText: String? = null,
    headerText: String,
    headerColor: Color,
    errorText: String? = null,
    headerTextAlign: TextAlign = TextAlign.Companion.Start
) {
    if (topHeaderText != null) {
        Text(
            text = topHeaderText,
            style = MaterialTheme.typography.titleMedium,
            color = headerColor,
            textAlign = headerTextAlign,
            modifier = Modifier.Companion.fillMaxWidth()
        )
    }
    Text(
        text = headerText,
        style = MaterialTheme.typography.titleLarge,
        color = headerColor,
        textAlign = headerTextAlign,
        modifier = Modifier.Companion.fillMaxWidth()
    )
    if (errorText != null) {
        Spacer(modifier = Modifier.height(8.dp))
    }
    AnimatedVisibility(
        visible = errorText != null
    ) {
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                textAlign = headerTextAlign
            )
        }
    }
}