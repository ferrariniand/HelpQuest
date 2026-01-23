package com.helpquest.profile.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestIconButton
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.cancel
import helpquest.core.designsystem.generated.resources.profile_settings
import org.jetbrains.compose.resources.stringResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ProfileHeaderSection(
    username: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Text(
                text = stringResource(DesignSystemRes.string.profile_settings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
        }
        HelpQuestIconButton(
            onClick = onCloseClick
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(DesignSystemRes.string.cancel),
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.extended.textSecondary
            )
        }
    }
}