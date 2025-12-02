package com.helpquest.chat.presentation.chat_list

import com.helpquest.chat.presentation.model.ChatUi

sealed interface ChatListAction {
    data object OnProfileSettingsClick : ChatListAction
    data object OnCreateChatClick : ChatListAction
    data class OnChatClick(val chat: ChatUi) : ChatListAction
    data class OnSelectChat(val chatId: String?) : ChatListAction
}