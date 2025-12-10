@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalUuidApi::class, ExperimentalTime::class)

package com.helpquest.chat.presentation.chat_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.presentation.chat_details.components.ChatDetailHeader
import com.helpquest.chat.presentation.chat_details.components.MessageList
import com.helpquest.chat.presentation.chat_details.components.SendMessageBox
import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.designsystem.components.containers_layouts.DynamicRoundedCornerColumn
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.components.for_scrollables.EmptyListSection
import com.helpquest.core.designsystem.components.generic.GenericPageHeaderSection
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import helpquest.feature.chat.presentation.generated.resources.Res
import helpquest.feature.chat.presentation.generated.resources.empty_chat
import helpquest.feature.chat.presentation.generated.resources.no_chat_selected
import helpquest.feature.chat.presentation.generated.resources.select_a_chat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ChatDetailRoot(
    chatId: String?,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onChatMembersClick: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ChatDetailEvent.OnChatLeft -> onBack()
            is ChatDetailEvent.OnError -> {
                snackbarState.showSnackbar(
                    event.error.asStringAsync()
                )
            }
        }
    }
    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    BackHandler(
        enabled = showBackButton
    ) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        onBack()
    }

    ChatDetailScreen(
        state = state,
        showBackButton = showBackButton,
        snackbarState = snackbarState,
        onAction = { action ->
            when (action) {
                is ChatDetailAction.OnChatMembersClick -> onChatMembersClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    showBackButton: Boolean,
    snackbarState: SnackbarHostState,
    onAction: (ChatDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val messageListState = rememberLazyListState()

    SnackbarScaffold(
        snackbarHostState = snackbarState,
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (!configuration.isWideScreen) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.extended.surfaceLower
        }
    ) {
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.chatUi == null) {
                        EmptyListSection(
                            title = stringResource(Res.string.no_chat_selected),
                            description = stringResource(Res.string.select_a_chat),
                            icon = painterResource(Res.drawable.empty_chat),
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        GenericPageHeaderSection {
                            ChatDetailHeader(
                                chatUi = state.chatUi,
                                isBackVisible = showBackButton,
                                isChatOptionsDropDownOpen = state.isChatOptionsOpen,
                                onChatOptionsClick = {
                                    onAction(ChatDetailAction.OnChatOptionsClick)
                                },
                                onDismissChatOptions = {
                                    onAction(ChatDetailAction.OnDismissChatOptions)
                                },
                                onManageChatClick = {
                                    onAction(ChatDetailAction.OnChatMembersClick)
                                },
                                onLeaveChatClick = {
                                    onAction(ChatDetailAction.OnLeaveChatClick)
                                },
                                onBackClick = {
                                    onAction(ChatDetailAction.OnBackClick)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        MessageList(
                            messages = state.messages,
                            listState = messageListState,
                            onMessageLongClick = { message ->
                                onAction(ChatDetailAction.OnMessageLongClick(message))
                            },
                            onMessageRetryClick = { message ->
                                onAction(ChatDetailAction.OnRetryClick(message))
                            },
                            onDismissMessageMenu = {
                                onAction(ChatDetailAction.OnDismissMessageMenu)
                            },
                            onDeleteMessageClick = { message ->
                                onAction(ChatDetailAction.OnDeleteMessageClick(message))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        AnimatedVisibility(
                            visible = !configuration.isWideScreen
                        ) {
                            //Send Message Box is inside the same rounded box of the Message list
                            SendMessageBox(
                                messageTextFieldState = state.messageTextFieldState,
                                isTextInputEnabled = state.canSendMessage,
                                connectionState = state.connectionState,
                                onSendClick = {
                                    onAction(ChatDetailAction.OnSendMessageClick)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    )
                            )
                        }
                    }
                }

                if (configuration.isWideScreen) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null
                ) {
                    //Send Message Box is in a different rounded box of the Message list
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen,
                    ) {
                        SendMessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isTextInputEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun ChatDetailScreenEmptyLightPreview() {
    HelpQuestTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun ChatDetailScreenEmptyDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ChatDetailScreen(
            state = ChatDetailState(),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun ChatDetailScreenMessagesLightPreview() {
    HelpQuestTheme {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = rememberTextFieldState(
                    initialText = "This is a new message!"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "1",
                    localParticipant = ParticipantUi(
                        id = "1",
                        username = "Philipp",
                        initials = "PH",
                    ),
                    otherParticipants = listOf(
                        ParticipantUi(
                            id = "2",
                            username = "Cinderella",
                            initials = "CI",
                        ),
                        ParticipantUi(
                            id = "3",
                            username = "Josh",
                            initials = "JO",
                        )
                    ),
                    lastMessage = ChatMessage(
                        id = "1",
                        chatId = "1",
                        content = "This is a last chat message that was sent by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        createdAt = Clock.System.now(),
                        senderId = "1",
                        deliveryStatus = ChatMessageDeliveryStatus.SENT,
                        deliveredAt = Clock.System.now()
                    ),
                    lastMessageSenderUsername = "Philipp"
                ),
                messages = (1..20).map {
                    if (it % 2 == 0) {
                        MessageListUiElement.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            isMenuOpen = false,
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20")
                        )
                    } else {
                        MessageListUiElement.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            sender = ParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JO"
                            ),
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20"),
                        )
                    }
                }
            ),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun ChatDetailScreenMessagesDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = rememberTextFieldState(
                    initialText = "This is a new message!"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "1",
                    localParticipant = ParticipantUi(
                        id = "1",
                        username = "Philipp",
                        initials = "PH",
                    ),
                    otherParticipants = listOf(
                        ParticipantUi(
                            id = "2",
                            username = "Cinderella",
                            initials = "CI",
                        ),
                        ParticipantUi(
                            id = "3",
                            username = "Josh",
                            initials = "JO",
                        )
                    ),
                    lastMessage = ChatMessage(
                        id = "1",
                        chatId = "1",
                        content = "This is a last chat message that was sent by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        createdAt = Clock.System.now(),
                        senderId = "1",
                        deliveryStatus = ChatMessageDeliveryStatus.SENT,
                        deliveredAt = Clock.System.now()
                    ),
                    lastMessageSenderUsername = "Philipp"
                ),
                messages = (1..20).map {
                    if (it % 2 == 0) {
                        MessageListUiElement.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            isMenuOpen = false,
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20")
                        )
                    } else {
                        MessageListUiElement.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            sender = ParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JO"
                            ),
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20"),
                        )
                    }
                }
            ),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}