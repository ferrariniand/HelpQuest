package com.helpquest.core.designsystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.error_user_image_unavailable
import org.jetbrains.compose.resources.stringResource

//User image (or initials)
@Composable
fun HelpQuestAvatarPhoto(
    displayText: String,
    size: AvatarSize,
    modifier: Modifier = Modifier,
    userImageUrl: String? = null,
    showUserIdentity: Boolean = false,
    textColor: Color = MaterialTheme.colorScheme.extended.textPlaceholder
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.extended.secondaryFill)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!showUserIdentity || userImageUrl == null) {
            Text(
                text = displayText.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
        }
        SubcomposeAsyncImage(
            model = userImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .matchParentSize()
                .padding(10.dp),
            error = {
                if (showUserIdentity && userImageUrl != null) {
                    //LocalInspectionMode.current is used to simulate the Composable Preview
                    if (LocalInspectionMode.current) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = stringResource(Res.string.error_user_image_unavailable),
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
            displayText = "AF",
            size = AvatarSize.SMALL,
            userImageUrl = "test",
            showUserIdentity = true
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
            displayText = "AF",
            size = AvatarSize.SMALL,
            userImageUrl = "test",
            showUserIdentity = true
        )
    }
}