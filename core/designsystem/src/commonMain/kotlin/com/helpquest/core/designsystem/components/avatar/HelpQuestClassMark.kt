package com.helpquest.core.designsystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.default_class
import org.jetbrains.compose.resources.stringResource

@Composable
fun HelpQuestClassMark(
    sizeAvatarPhoto: AvatarSize,
    modifier: Modifier = Modifier,
    classImageUrl: String? = null, //TODO maybe resource or drawable
) {
    val sizeClassMark = sizeAvatarPhoto.dp.div(3)
    Box(
        modifier = modifier
            .size(sizeClassMark)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.extended.secondaryFill)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = classImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .matchParentSize()
                .padding(2.dp),
            error = {
                //LocalInspectionMode.current is used to simulate the Composable Preview
                if (LocalInspectionMode.current) {
                    Icon(
                        imageVector = Icons.Filled.Build,
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person, //TODO define default icon
                        contentDescription = stringResource(Res.string.default_class),
                        tint = MaterialTheme.colorScheme.extended.cakeRed,
                    )
                }
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestClassMarkLargeLightPreview() {
    HelpQuestTheme {
        HelpQuestClassMark(
            sizeAvatarPhoto = AvatarSize.LARGE,
            classImageUrl = "test",
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestClassMarkLargeDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestClassMark(
            sizeAvatarPhoto = AvatarSize.LARGE,
            classImageUrl = "test",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestClassMarkSmallLightPreview() {
    HelpQuestTheme {
        HelpQuestClassMark(
            sizeAvatarPhoto = AvatarSize.SMALL,
            classImageUrl = "test",
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestClassMarkSmallDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestClassMark(
            sizeAvatarPhoto = AvatarSize.SMALL,
            classImageUrl = "test",
        )
    }
}