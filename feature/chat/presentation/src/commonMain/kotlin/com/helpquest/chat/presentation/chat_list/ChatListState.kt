package com.helpquest.chat.presentation.chat_list

import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText

data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ParticipantUi? = null,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false
)