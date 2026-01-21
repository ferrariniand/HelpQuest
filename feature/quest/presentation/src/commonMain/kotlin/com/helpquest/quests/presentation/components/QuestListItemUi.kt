package com.helpquest.quests.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.avatar.HelpQuestStackedAvatars
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDividerWithTitle
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.domain.models.Category
import com.helpquest.core.presentation.modelsUi.Location
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.quests.domain.models.QuestStatus
import com.helpquest.quests.presentation.model.QuestListUiElement
import com.helpquest.quests.presentation.model.QuestUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock


@Composable
fun QuestListItemUi(
    questListUiElement: QuestListUiElement,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (questListUiElement) {
            is QuestListUiElement.PlaceSeparator -> {
                PlaceSeparatorItem(
                    place = questListUiElement.place.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is QuestListUiElement.QuestItem -> {
                QuestItemUi(
                    quest = questListUiElement.quest,
                    isSelected = isSelected,
                    modifier = Modifier
                        .clickable {
                            onSelect()
                        }
                )
            }
        }
    }
}

@Composable
private fun PlaceSeparatorItem(
    place: String,
    modifier: Modifier = Modifier
) {
    HelpQuestHorizontalDividerWithTitle(
        place,
        modifier
    )
}

@Composable
fun QuestItemUi(
    quest: QuestUi,
    isSelected: Boolean,
    modifier: Modifier
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val isSmallScreenHeight = deviceConfiguration.isSmallScreenHeight

    Surface(
        modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.extended.primaryHover
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = if (isSmallScreenHeight) 4.dp else 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(
                if (isSmallScreenHeight) 4.dp else 8.dp
            ),
        ) {
            Text(
                text = quest.questTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
            )

            Text(
                text = quest.questDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            HelpQuestStackedAvatars(
                avatars = quest.participants,
            )
        }
    }
}

@Composable
@Preview
fun QuestListItemUiLightPreview() {
    HelpQuestTheme {
        QuestItemUi(
            quest = QuestUi(
                questId = "1",
                questTitle = "Quest",
                questDescription = "Hello world, this is a preview message that spans multiple lines",
                questCreatorId = "2",
                createdAt = Clock.System.now(),
                location = Location(0.0, 0.0),
                questCategory = Category.GENERIC,
                participants = listOf(
                    ParticipantUi(
                        id = "id1",
                        username = "primo",
                        initials = "PR"
                    ),
                    ParticipantUi(
                        id = "id2",
                        username = "secondo",
                        initials = "SE"
                    )
                ),
                questStatus = QuestStatus.OPEN,
                lastActivity = null
            ),
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun QuestListItemUiDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        QuestItemUi(
            quest = QuestUi(
                questId = "1",
                questTitle = "Quest",
                questDescription = "Hello world, this is a preview message that spans multiple lines",
                questCreatorId = "2",
                createdAt = Clock.System.now(),
                location = Location(0.0, 0.0),
                questCategory = Category.GENERIC,
                participants = listOf(
                    ParticipantUi(
                        id = "id1",
                        username = "primo",
                        initials = "PR"
                    ),
                    ParticipantUi(
                        id = "id2",
                        username = "secondo",
                        initials = "SE"
                    )
                ),
                questStatus = QuestStatus.OPEN,
                lastActivity = null
            ),
            isSelected = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}