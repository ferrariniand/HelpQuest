package com.helpquest.chat.presentation.create_manage_chat

import com.helpquest.core.presentation.modelsUi.ParticipantUi

sealed interface ManageChatAction {
    data object OnDebounceSearchTextField : ManageChatAction
    data class OnAddClick(val participant: ParticipantUi) : ManageChatAction
    data object OnDismissDialog : ManageChatAction
    data object OnPrimaryActionClick : ManageChatAction
}