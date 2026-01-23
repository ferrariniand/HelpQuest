package com.helpquest.chat.presentation.create_manage_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.dialogs.HelpQuestAdaptiveDialogSheetLayout
import com.helpquest.core.presentation.util.ObserveAsEvents
import helpquest.core.designsystem.generated.resources.save
import helpquest.feature.chat.presentation.generated.resources.Res
import helpquest.feature.chat.presentation.generated.resources.chat_members
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ManageChatRoot(
    chatId: String?,
    onDismiss: () -> Unit,
    onMembersAdded: () -> Unit,
    viewModel: ManageChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ManageChatEvent.OnMembersAdded -> onMembersAdded()
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ManageChatAction.OnSelectChat(chatId))
    }

    HelpQuestAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ManageChatScreen(
            headerText = stringResource(Res.string.chat_members),
            primaryButtonText = stringResource(DesignSystemRes.string.save),
            state = state,
            onAction = { action ->
                when (action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}