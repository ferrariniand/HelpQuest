package com.helpquest.chat.presentation.create_chat

import com.helpquest.chat.domain.models.Chat


sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat) : CreateChatEvent
}