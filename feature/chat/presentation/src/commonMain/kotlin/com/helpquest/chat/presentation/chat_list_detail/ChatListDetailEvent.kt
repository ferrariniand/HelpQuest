package com.helpquest.chat.presentation.chat_list_detail

sealed interface ChatListDetailEvent {
    data object CreateChatDialogDismissed : ChatListDetailEvent
}