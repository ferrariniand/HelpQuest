package com.helpquest.chat.presentation.chat_details

import androidx.compose.foundation.text.input.TextFieldState
import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageListUiElement> = emptyList(),
    val messageWithOpenMenu: MessageListUiElement.LocalUserMessage? = null,
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)