package com.helpquest.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.result_layouts.Loader
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended

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
    reduceVerticalPadding: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

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

    if (style == HelpQuestButtonStyle.TEXT) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = if (enabled) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.extended.textDisabled
            },
            modifier = modifier
                .clickable(
                    enabled = enabled,
                    onClick = {
                        focusManager.clearFocus()
                        onClick()
                    }
                )
        )
    } else {
        Button(
            onClick = {
                focusManager.clearFocus()
                onClick()
            },
            modifier = modifier,
            enabled = enabled,
            shape = RoundedCornerShape(8.dp), //TODO: Customize this button
            colors = colors,
            border = border
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(
                        horizontal = 6.dp,
                        vertical = if (reduceVerticalPadding) 4.dp else 6.dp
                    )
            ) {
                Loader(
                    isLoading,
                    size = 22.dp,
                    color = colors.contentColor
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
}

@Composable
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true,
    backgroundColor = 1
)
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
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true,
    backgroundColor = 1
)
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
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true
)
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
@Preview(
    showBackground = true,
    backgroundColor = 1
)
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