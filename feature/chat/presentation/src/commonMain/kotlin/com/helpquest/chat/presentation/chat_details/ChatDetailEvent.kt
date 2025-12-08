package com.helpquest.chat.presentation.chat_details

import com.helpquest.core.presentation.util.UiText

sealed interface ChatDetailEvent {
    data object OnChatLeft : ChatDetailEvent
    data class OnError(val error: UiText) : ChatDetailEvent
}