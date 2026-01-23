package com.helpquest.chat.presentation.chat_list_detail

sealed interface ChatListDetailAction {
    data class OnSelectChat(val chatId: String?) : ChatListDetailAction
    data object OnCreateChatClick : ChatListDetailAction
    data object OnManageChatClick : ChatListDetailAction
    data class OnDismissCurrentDialog(val isCreateChatDialog: Boolean) : ChatListDetailAction
}