package com.helpquest.core.designsystem.components.result_layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.auth.AuthPageHeaderSection
import com.helpquest.core.designsystem.components.brand.BrandLogo
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.containers_layouts.HelpQuestSurface
import com.helpquest.core.designsystem.components.icons.HelpQuestFailureIcon
import com.helpquest.core.designsystem.components.icons.HelpQuestPageLoadingIndicator
import com.helpquest.core.designsystem.components.icons.HelpQuestSuccessIcon
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HelpQuestAdaptiveResultLayout(
    title: String? = null,
    description: String,
    primaryButton: (@Composable () -> Unit)? = null,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    brandLogo: @Composable () -> Unit = { BrandLogo() },
    resultLogo: @Composable ColumnScope.() -> Unit,
    isLoadings: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val configuration = currentDeviceConfiguration()

    when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            HelpQuestSurface(
                modifier = modifier,
                header = {
                    Spacer(modifier = Modifier.height(32.dp))
                    brandLogo()
                    Spacer(modifier = Modifier.height(32.dp))
                },
                content = {
                    if (isLoadings) {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                    resultLogo()
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.extended.textPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    HelpQuestResultBodyLayout(
                        description = description,
                        primaryButton = primaryButton,
                        secondaryButton = secondaryButton,
                        secondaryError = secondaryError,
                        isMobileAndNotLoading = true
                    )
                }
            )
        }

        DeviceConfiguration.MOBILE_SPLIT_SCREEN, //TODO: define a UI for MOBILE_SPLIT_SCREEN
        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.8f),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    brandLogo()
                    if (title != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        AuthPageHeaderSection(
                            headerText = title,
                            headerColor = MaterialTheme.colorScheme.onBackground,
                            errorText = secondaryError
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (isLoadings) Arrangement.Center else Arrangement.Top
                ) {
                    resultLogo()
                    HelpQuestResultBodyLayout(
                        description = description,
                        primaryButton = primaryButton,
                        secondaryButton = secondaryButton,
                        isMobileAndNotLoading = !isLoadings
                    )
                }
            }
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                brandLogo()
                Spacer(modifier = Modifier.height(32.dp))
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .heightIn(min = 300.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (isLoadings) Arrangement.Center else Arrangement.Top

                ) {
                    resultLogo()
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.extended.textPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    HelpQuestResultBodyLayout(
                        description = description,
                        primaryButton = primaryButton,
                        secondaryButton = secondaryButton,
                        secondaryError = secondaryError
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
fun HelpQuestAdaptiveResultSuccessLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            title = "Registration successful!",
            description = "Test description disposed on more than one line and showing success message",
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
            resultLogo = {
                HelpQuestSuccessIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveResultSuccessLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveResultLayout(
            title = "Registration successful!",
            description = "Test description disposed on more than one line and showing success message",
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
            resultLogo = {
                HelpQuestSuccessIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAdaptiveResultSuccessWithErrorLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            title = "Registration successful!",
            description = "Test description disposed on more than one line and showing success message",
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
            secondaryError = "This is an error",
            resultLogo = {
                HelpQuestSuccessIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveResultSuccessWithErrorLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveResultLayout(
            title = "Registration successful!",
            description = "Test description disposed on more than one line and showing success message",
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
            secondaryError = "This is an error",
            resultLogo = {
                HelpQuestSuccessIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAdaptiveResultFailureLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            title = "Registration Failure!",
            description = "Test description disposed on more than one line and showing failure message",
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
            resultLogo = {
                HelpQuestFailureIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveResultFailureLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveResultLayout(
            title = "Registration Failure!",
            description = "Test description disposed on more than one line and showing failure message",
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
            resultLogo = {
                HelpQuestFailureIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAdaptiveResultFailureWithErrorLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            title = "Registration Failure!",
            description = "Test description disposed on more than one line and showing failure message",
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
            secondaryError = "This is an error",
            resultLogo = {
                HelpQuestFailureIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveResultFailureWithErrorLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveResultLayout(
            title = "Registration Failure!",
            description = "Test description disposed on more than one line and showing failure message",
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
            secondaryError = "This is an error",
            resultLogo = {
                HelpQuestFailureIcon()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAdaptiveResultLoadingLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveResultLayout(
            description = "Test description disposed on more than one line and showing loading message",
            resultLogo = {
                HelpQuestPageLoadingIndicator()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveResultLoadingLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveResultLayout(
            description = "Test description disposed on more than one line and showing loading message",
            resultLogo = {
                HelpQuestPageLoadingIndicator()
            },
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}