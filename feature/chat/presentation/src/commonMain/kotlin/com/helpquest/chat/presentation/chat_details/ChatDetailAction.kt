package com.helpquest.chat.presentation.chat_details

import com.helpquest.chat.presentation.model.MessageListUiElement

sealed interface ChatDetailAction {
    data object OnSendMessageClick : ChatDetailAction
    data object OnScrollToTop : ChatDetailAction
    data class OnSelectChat(val chatId: String?) : ChatDetailAction
    data class OnDeleteMessageClick(val message: MessageListUiElement.LocalUserMessage) :
        ChatDetailAction

    data class OnMessageLongClick(val message: MessageListUiElement.LocalUserMessage) :
        ChatDetailAction

    data object OnDismissMessageMenu : ChatDetailAction
    data class OnRetryClick(val message: MessageListUiElement.LocalUserMessage) : ChatDetailAction
    data object OnBackClick : ChatDetailAction
    data object OnChatOptionsClick : ChatDetailAction
    data object OnChatMembersClick : ChatDetailAction
    data object OnLeaveChatClick : ChatDetailAction
    data object OnDismissChatOptions : ChatDetailAction
}