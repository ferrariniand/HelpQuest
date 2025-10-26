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
import com.helpquest.core.designsystem.theme.extended

@Composable
fun ColumnScope.HeaderSection(
    topHeaderText: String? = null,
    headerText: String,
    headerColor: Color,
    errorText: String? = null,
    successText: String? = null,
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
    AnimatedVisibility(
        visible = errorText != null,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorText ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            textAlign = headerTextAlign
        )
    }
    AnimatedVisibility(
        visible = successText != null,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = successText ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.success,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            textAlign = headerTextAlign
        )
    }
}