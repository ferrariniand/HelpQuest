package com.helpquest.core.designsystem.components.avatar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended

enum class AvatarSize(val dp: Dp) {
    SMALL(40.dp), LARGE(60.dp)
}

/** User image + Class icon
 * if classImageUrl is null and showClass is true, will be shown the default class
 */
@Composable
fun HelpQuestAvatar(
    displayText: String,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.SMALL,
    userImageUrl: String? = null,
    showUserIdentity: Boolean = false,
    classImageUrl: String? = null,
    showClass: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null
            )
    ) {
        HelpQuestAvatarPhoto(
            displayText = displayText,
            modifier = Modifier.align(Alignment.Center),
            size = size,
            userImageUrl = userImageUrl,
            showUserIdentity = showUserIdentity,
            textColor = MaterialTheme.colorScheme.extended.textPlaceholder
        )
        if (showClass) {
            HelpQuestClassMark(
                sizeAvatarPhoto = size,
                modifier = Modifier.align(Alignment.BottomStart),
                classImageUrl = classImageUrl,
            )
        }
    }
}


@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAvatarLargeWithMarkLightPreview() {
    HelpQuestTheme {
        HelpQuestAvatar(
            displayText = "AF",
            size = AvatarSize.LARGE,
            showClass = true
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAvatarLargeWithMarkDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAvatar(
            displayText = "AF",
            size = AvatarSize.LARGE,
            showClass = true
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAvatarSmallWithMarkLightPreview() {
    HelpQuestTheme {
        HelpQuestAvatar(
            displayText = "AF",
            userImageUrl = "test",
            showUserIdentity = true,
            classImageUrl = "test",
            showClass = true
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAvatarSmallWithMarkDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAvatar(
            displayText = "AF",
            userImageUrl = "test",
            showUserIdentity = true,
            classImageUrl = "test",
            showClass = true
        )
    }
}