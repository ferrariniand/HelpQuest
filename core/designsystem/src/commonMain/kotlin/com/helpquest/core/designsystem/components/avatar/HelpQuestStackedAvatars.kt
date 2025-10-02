package com.helpquest.core.designsystem.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HelpQuestStackedAvatars(
    avatars: List<AvatarUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.SMALL,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    val overlapOffset = -(size.dp * overlapPercentage)

    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarUi ->
            HelpQuestAvatarPhoto(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl
            )
        }

        if (remainingCount > 0) {
            HelpQuestAvatarPhoto(
                displayText = "$remainingCount+",
                size = size,
                textColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestStackedAvatarsLightPreview() {
    HelpQuestTheme {
        HelpQuestStackedAvatars(
            avatars = listOf(
                AvatarUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                AvatarUi(
                    id = "2",
                    username = "Sabrina",
                    initials = "SA",
                ),
                AvatarUi(
                    id = "3",
                    username = "John",
                    initials = "JO",
                ),
                AvatarUi(
                    id = "4",
                    username = "Laura",
                    initials = "LA",
                ),
            )
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestStackedAvatarsDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestStackedAvatars(
            avatars = listOf(
                AvatarUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                AvatarUi(
                    id = "2",
                    username = "Sabrina",
                    initials = "SA",
                ),
                AvatarUi(
                    id = "3",
                    username = "John",
                    initials = "JO",
                ),
                AvatarUi(
                    id = "4",
                    username = "Laura",
                    initials = "LA",
                ),
            )
        )
    }
}