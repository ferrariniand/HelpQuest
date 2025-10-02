package com.helpquest.core.designsystem.components.dialogs

import androidx.compose.runtime.Composable
import com.helpquest.core.presentation.util.currentDeviceConfiguration

@Composable
fun HelpQuestAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if (configuration.isMobile) {
        HelpQuestBottomSheet(
            onDismiss = onDismiss,
            content = content
        )
    } else {
        HelpQuestContentDialog(
            onDismiss = onDismiss,
            content = content
        )
    }
}