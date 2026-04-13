package com.helpquest.chat.presentation.chat_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.brand.BrandLogo
import com.helpquest.core.designsystem.components.generic.GenericPageHeaderSection
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.appName
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatListHeader(
    localParticipant: ParticipantUi?,
    onProfileSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GenericPageHeaderSection(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BrandLogo(
                tintColor = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = appName(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            if (localParticipant != null) {
                HelpQuestAvatar(
                    displayText = localParticipant.initials,
                    userImageUrl = localParticipant.imageUrl,
                    showUserIdentity = true,
                    classImageUrl = localParticipant.classImageUrl,
                    showClass = true,
                    onClick = onProfileSettingsClick
                )
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HomepageHeaderLightPreview() {
    HelpQuestTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                onProfileSettingsClick = {},
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HomepageHeaderDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                onProfileSettingsClick = {},
            )
        }
    }
}