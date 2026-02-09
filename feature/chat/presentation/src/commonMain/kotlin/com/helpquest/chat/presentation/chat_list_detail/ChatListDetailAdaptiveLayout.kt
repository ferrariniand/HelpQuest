@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)

package com.helpquest.chat.presentation.chat_list_detail

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.chat.presentation.chat_details.ChatDetailRoot
import com.helpquest.chat.presentation.chat_list.ChatListRoot
import com.helpquest.chat.presentation.create_manage_chat.CreateChatRoot
import com.helpquest.chat.presentation.create_manage_chat.ManageChatRoot
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.DialogSheetScopedViewModelContainer
import com.helpquest.core.presentation.util.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListDetailAdaptiveLayout(
    initialChatId: String?,
    onProfileSettingsClick: () -> Unit,
    chatListDetailViewModel: ChatListDetailViewModel = koinViewModel()
) {
    val sharedState by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )
    val scope = rememberCoroutineScope()

    fun navigateBackInternally() {
        scope.launch {
            scaffoldNavigator.navigateBack()
        }
    }

    LaunchedEffect(initialChatId) {
        if (initialChatId != null) {
            chatListDetailViewModel.onAction(ChatListDetailAction.OnSelectChat(initialChatId))
            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
        navigateBackInternally()
        chatListDetailViewModel.onAction(ChatListDetailAction.OnSelectChat(null))
    }

    ObserveAsEvents(chatListDetailViewModel.events) { event ->
        when (event) {
            ChatListDetailEvent.CreateChatDialogDismissed -> if (scaffoldNavigator.canNavigateBack()) {
                navigateBackInternally()
            }
        }
    }

    val listPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List]
    val detailPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail]

    LaunchedEffect(detailPane, sharedState.selectedChatId) {
        if (detailPane == PaneAdaptedValue.Hidden && sharedState.selectedChatId != null) {
            chatListDetailViewModel.onAction(ChatListDetailAction.OnSelectChat(null))
        }
    }

    ListDetailPaneScaffold(
        directive = scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        listPane = {
            AnimatedPane {
                ChatListRoot(
                    selectedChatId = sharedState.selectedChatId,
                    onSelectChat = {
                        chatListDetailViewModel.onAction(ChatListDetailAction.OnSelectChat(it))
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    },
                    onCreateChatClick = {
                        chatListDetailViewModel.onAction(ChatListDetailAction.OnCreateChatClick)
                    },
                    onProfileSettingsClick = onProfileSettingsClick,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                ChatDetailRoot(
                    chatId = sharedState.selectedChatId,
                    showBackButton = detailPane == PaneAdaptedValue.Hidden || listPane == PaneAdaptedValue.Hidden,
                    onBack = {
                        scope.launch {
                            if (scaffoldNavigator.canNavigateBack()) {
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    },
                    onChatMembersClick = {
                        chatListDetailViewModel.onAction(ChatListDetailAction.OnManageChatClick)
                    }
                )
            }
        }
    )

    DialogSheetScopedViewModelContainer(
        visible = sharedState.dialogState is DialogState.CreateChat
    ) {
        CreateChatRoot(
            onDismiss = {
                chatListDetailViewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(true))
            },
            onChatCreated = { chat ->
                chatListDetailViewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(true))
                chatListDetailViewModel.onAction(ChatListDetailAction.OnSelectChat(chat.id))
                scope.launch {
                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            },
        )
    }

    DialogSheetScopedViewModelContainer(
        visible = sharedState.dialogState is DialogState.ManageChat
    ) {
        ManageChatRoot(
            chatId = sharedState.selectedChatId,
            onMembersAdded = {
                chatListDetailViewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(false))
            },
            onDismiss = {
                chatListDetailViewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(false))
            }
        )
    }
}