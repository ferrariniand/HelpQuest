package com.helpquest.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class HelpQuestButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}

@Composable
fun HelpQuestButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: HelpQuestButtonStyle = HelpQuestButtonStyle.PRIMARY,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val colors = when (style) {
        HelpQuestButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        HelpQuestButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        HelpQuestButtonStyle.DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        HelpQuestButtonStyle.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    val defaultBorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )
    val border = when {
        style == HelpQuestButtonStyle.PRIMARY && !enabled -> defaultBorderStroke
        style == HelpQuestButtonStyle.SECONDARY -> defaultBorderStroke
        style == HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorderStroke
        style == HelpQuestButtonStyle.DESTRUCTIVE_SECONDARY -> {
            val borderColor = if (enabled) {
                MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
            BorderStroke(
                width = 1.dp,
                color = borderColor
            )
        }

        else -> null
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp), //TODO: Customize this button
        colors = colors,
        border = border
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(6.dp)
        ) {
            CircularProgressIndicator( //TODO: Customize this loader
                modifier = Modifier
                    .size(15.dp)
                    .alpha(
                        alpha = if (isLoading) 1f else 0f
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if (isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
@Preview
fun HelpQuestPrimaryButtonLightPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.PRIMARY
        )
    }
}

@Composable
@Preview
fun HelpQuestPrimaryButtonDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.PRIMARY
        )
    }
}

@Composable
@Preview
fun HelpQuestPrimaryButtonLoadingPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.PRIMARY,
            isLoading = true
        )
    }
}

@Composable
@Preview
fun HelpQuestPrimaryButtonDisabledPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.PRIMARY,
            enabled = false
        )
    }
}

@Composable
@Preview
fun HelpQuestSecondaryButtonLightPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.SECONDARY
        )
    }
}

@Composable
@Preview
fun HelpQuestSecondaryButtonDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.SECONDARY
        )
    }
}

@Composable
@Preview
fun HelpQuestSecondaryButtonDarkLoadingPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.SECONDARY,
            isLoading = true
        )
    }
}

@Composable
@Preview
fun HelpQuestSecondaryButtonDarkDisabledPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.SECONDARY,
            enabled = false
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructivePrimaryLightButtonPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructivePrimaryDarkButtonPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructivePrimaryLoadingButtonPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY,
            isLoading = true
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructivePrimaryDisabledButtonPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_PRIMARY,
            enabled = false
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructiveSecondaryLightButtonPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_SECONDARY
        )
    }
}

@Composable
@Preview
fun HelpQuestDestructiveSecondaryDarkButtonPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.DESTRUCTIVE_SECONDARY
        )
    }
}

@Composable
@Preview
fun HelpQuestTextButtonLightPreview() {
    HelpQuestTheme {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.TEXT
        )
    }
}

@Composable
@Preview
fun HelpQuestTextButtonDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestButton(
            text = "Hello world!",
            onClick = {},
            style = HelpQuestButtonStyle.TEXT
        )
    }
}