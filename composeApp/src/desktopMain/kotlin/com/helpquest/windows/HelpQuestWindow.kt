package com.helpquest.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.helpquest.App
import com.helpquest.core.designsystem.theme.appName
import com.helpquest.theme.AppTheme
import helpquest.core.designsystem.generated.resources.compose_multiplatform
import helpquest.core.designsystem.generated.resources.file
import helpquest.core.designsystem.generated.resources.new_window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun HelpQuestWindow(
    appTheme: AppTheme,
    onCloseRequest: () -> Unit,
    onAddWindowClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onDeepLinkListenerSetup: () -> Unit,
) {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )
    Window(
        onCloseRequest = onCloseRequest,
        state = windowState,
        title = appName(),
        icon = painterResource(DesignSystemRes.drawable.compose_multiplatform)
    ) {
        FocusObserver(
            onFocusChanged = onFocusChanged
        )

        MenuBar {
            Menu(
                text = stringResource(DesignSystemRes.string.file),
                mnemonic = 'F'
            ) {
                Item(
                    text = stringResource(DesignSystemRes.string.new_window),
                    mnemonic = 'N',
                    shortcut = KeyShortcut(
                        key = Key.N,
                        ctrl = true,
                        shift = true
                    ),
                    onClick = onAddWindowClick
                )
            }
        }

        App(
            isDarkTheme = appTheme == AppTheme.DARK,
            onDeepLinkListenerSetup = onDeepLinkListenerSetup
        )
    }
}