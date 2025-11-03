package com.helpquest.core.designsystem.components.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.brand.HelpQuestBrandLogo
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.DeviceConfiguration
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.core.presentation.util.isKeyboardOpen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HelpQuestAdaptiveFormLayout(
    topHeaderText: String? = null,
    headerText: String,
    errorText: String? = null,
    successText: String? = null,
    logo: @Composable () -> Unit = { HelpQuestBrandLogo() },
    modifier: Modifier = Modifier,
    formContent: @Composable ColumnScope.() -> Unit,
    buttonsContent: @Composable ColumnScope.() -> Unit,
    isLongScreen: Boolean = true
) {
    val configuration = currentDeviceConfiguration()
    val headerColor = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.extended.textPrimary
    }
    val isKeyboardOpen by isKeyboardOpen()

    when (configuration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            HelpQuestSurface(
                modifier = modifier
                    .clearFocusOnTap()
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .consumeWindowInsets(WindowInsets.displayCutout),
                header = {
                    Spacer(modifier = Modifier.height(24.dp))
                    logo()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            ) {
                AnimatedVisibility(
                    visible = !isKeyboardOpen,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HeaderSection(
                            topHeaderText = topHeaderText,
                            headerText = headerText,
                            headerColor = headerColor,
                            errorText = errorText,
                            successText = successText,
                            headerTextAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                formContent()
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(10.dp))
                buttonsContent()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .clearFocusOnTap()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.8f),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    logo()
                    Spacer(modifier = Modifier.height(16.dp))
                    HeaderSection(
                        topHeaderText = topHeaderText,
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                        successText = successText,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (isLongScreen) {
                    HelpQuestSurface(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        formContent()
                        Spacer(modifier = Modifier.height(24.dp))
                        buttonsContent()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = if (!isKeyboardOpen) 16.dp else 0.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            formContent()
                            Spacer(modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.height(10.dp))
                            buttonsContent()
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
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
                    .clearFocusOnTap()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                logo()
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderSection(
                        topHeaderText = topHeaderText,
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                        headerTextAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    formContent()
                    Spacer(modifier = Modifier.height(24.dp))
                    buttonsContent()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun HelpQuestAdaptiveFormLayoutLightPreview() {
    HelpQuestTheme {
        HelpQuestAdaptiveFormLayout(
            topHeaderText = "Welcome to",
            headerText = "Help Quest!",
            errorText = "Login failed!",
            logo = { HelpQuestBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            buttonsContent = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun HelpQuestAdaptiveFormLayoutDarkPreview() {
    HelpQuestTheme(
        darkTheme = true
    ) {
        HelpQuestAdaptiveFormLayout(
            topHeaderText = "Welcome to",
            headerText = "Help Quest!",
            errorText = "Login failed!",
            logo = { HelpQuestBrandLogo() },
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            buttonsContent = {}
        )
    }
}