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
import helpquest.core.designsystem.generated.resources.compose_multiplatform
import helpquest.core.designsystem.generated.resources.file
import helpquest.core.designsystem.generated.resources.new_window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun HelpQuestWindow(
    onCloseRequest: () -> Unit,
    onAddWindowClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit
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

        App()
    }
}