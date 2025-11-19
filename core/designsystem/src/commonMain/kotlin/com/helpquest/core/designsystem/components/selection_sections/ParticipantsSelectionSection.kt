package com.helpquest.core.designsystem.components.selection_sections

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.designsystem.theme.titleXSmall
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardVisible

@Composable
fun ColumnScope.ParticipantsSelectionSection(
    selectedParticipants: List<ParticipantUi>,
    modifier: Modifier = Modifier,
) {
    val isKeyboardVisible by isKeyboardVisible()

    val deviceConfiguration = currentDeviceConfiguration()
    val rootHeightModifier = when (deviceConfiguration) {
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Modifier
                .animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }

        else -> Modifier
            .weight(1f)
    }

    val isSmallScreenHeight = deviceConfiguration.isSmallScreenHeight
    val shouldHideSelectedParticipants =
        isSmallScreenHeight && isKeyboardVisible

    Box(
        modifier = rootHeightModifier
            .then(modifier)
            .padding(vertical = if (isSmallScreenHeight) 4.dp else 8.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            if (selectedParticipants.isNotEmpty() && !shouldHideSelectedParticipants) {
                items(
                    items = selectedParticipants,
                    key = { it.id }
                ) { participant ->
                    ParticipantListItem(
                        participantUi = participant,
                        isSmallScreenHeight = isSmallScreenHeight,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

}

@Composable
fun ParticipantListItem(
    participantUi: ParticipantUi,
    isSmallScreenHeight: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = if (isSmallScreenHeight) 4.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HelpQuestAvatar(
            displayText = participantUi.initials,
            userImageUrl = participantUi.imageUrl,
            showUserIdentity = participantUi.showParticipantIdentity,
            classImageUrl = participantUi.classImageUrl,
            showClass = true
        )
        Text(
            text = participantUi.username,
            style = MaterialTheme.typography.titleXSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}