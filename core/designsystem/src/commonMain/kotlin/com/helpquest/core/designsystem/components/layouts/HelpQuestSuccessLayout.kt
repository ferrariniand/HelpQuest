package com.helpquest.core.designsystem.components.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.icons.HelpQuestSuccessIcon
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HelpQuestSuccessLayout(
    title: String,
    description: String,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    modifier: Modifier = Modifier
) {
    val configuration = currentDeviceConfiguration()
    val iconModifier = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        Modifier.offset(y = -(25).dp)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HelpQuestSuccessIcon(
            modifier = iconModifier
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(
                    y = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
                        -(75).dp
                    } else {
                        -(25).dp
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            primaryButton()

            if (secondaryButton != null) {
                Spacer(modifier = Modifier.height(8.dp))
                secondaryButton()
                AnimatedVisibility(
                    visible = secondaryError != null
                ) {
                    if (secondaryError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = secondaryError,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestSuccessLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestSuccessLayout(
            title = "Hello world!",
            description = "Test description",
            primaryButton = {
                HelpQuestButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestSuccessLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestSuccessLayout(
            title = "Hello world!",
            description = "Test description",
            primaryButton = {
                HelpQuestButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestSuccessLayoutErrorLightPreview() {
    HelpQuestTheme {
        HelpQuestSuccessLayout(
            title = "Hello world!",
            description = "Test description",
            primaryButton = {
                HelpQuestButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryError = "This is an error"
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestSuccessLayoutErrorDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestSuccessLayout(
            title = "Hello world!",
            description = "Test description",
            primaryButton = {
                HelpQuestButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                HelpQuestButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = HelpQuestButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryError = "This is an error"
        )
    }
}