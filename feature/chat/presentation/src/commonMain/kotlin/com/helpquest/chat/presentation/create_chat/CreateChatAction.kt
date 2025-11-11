package com.helpquest.chat.presentation.create_chat

import com.helpquest.core.presentation.modelsUi.ParticipantUi

sealed interface CreateChatAction {
    data object OnDebounceSearchTextField : CreateChatAction
    data class OnAddClick(val participant: ParticipantUi) : CreateChatAction
    data object OnDismissDialog : CreateChatAction
    data object OnCreateChatClick : CreateChatAction
}