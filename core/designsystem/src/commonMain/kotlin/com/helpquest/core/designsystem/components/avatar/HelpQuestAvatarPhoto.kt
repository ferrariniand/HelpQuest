package com.helpquest.core.designsystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.error_content_unavailable
import helpquest.core.designsystem.generated.resources.error_user_image_unavailable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AvatarSize(val dp: Dp) {
    SMALL(40.dp), LARGE(60.dp)
}


@Composable
fun HelpQuestAvatarPhoto(
    modifier: Modifier = Modifier,
    displayText: String? = null,
    size: AvatarSize = AvatarSize.SMALL,
    userImageUrl: String? = null,
    classImageUrl: String? = null,
    showUserIdentity: Boolean = false,
    onClick: (() -> Unit)? = null,
    textColor: Color = MaterialTheme.colorScheme.extended.textPlaceholder
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .clickable(
                onClick = { onClick?.invoke() },
                enabled = onClick != null
            )
            .background(MaterialTheme.colorScheme.extended.secondaryFill)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (displayText != null) {
            Text(
                text = displayText.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
        SubcomposeAsyncImage(
            model = if (showUserIdentity) userImageUrl else classImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .matchParentSize()
                .padding(10.dp),
            error = {
                if (displayText == null) {
                    //LocalInspectionMode.current is used to simulate the Composable Preview
                    if (LocalInspectionMode.current) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = if (showUserIdentity && userImageUrl == null) {
                                stringResource(Res.string.error_user_image_unavailable)
                            } else {
                                stringResource(Res.string.error_content_unavailable)
                            },
                            tint = MaterialTheme.colorScheme.extended.cakeRed,
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAvatarPhotoLargeLightPreview() {
    HelpQuestTheme {
        HelpQuestAvatarPhoto(
            displayText = "AF",
            size = AvatarSize.LARGE
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAvatarPhotoLargeDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAvatarPhoto(
            displayText = "AF",
            size = AvatarSize.LARGE
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAvatarPhotoSmallLightPreview() {
    HelpQuestTheme {
        HelpQuestAvatarPhoto(
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAvatarPhotoSmallDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAvatarPhoto(
            userImageUrl = "https://picsum.photos/200"
        )
    }
}