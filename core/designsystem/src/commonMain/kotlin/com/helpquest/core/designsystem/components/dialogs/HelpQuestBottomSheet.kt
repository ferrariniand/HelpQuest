@file:OptIn(ExperimentalMaterial3Api::class)

package com.helpquest.core.designsystem.components.dialogs

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HelpQuestBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true  //TODO: maybe will be used in a different BottomSheet
    )
    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            sheetState.expand()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null, //TODO: maybe will be used in a different BottomSheet
        contentWindowInsets = { WindowInsets(0.dp) },
        modifier = modifier.statusBarsPadding()
    ) {
        content()
    }
}