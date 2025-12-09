package com.helpquest.chat.presentation.create_manage_chat


sealed interface ManageChatEvent {
    data object OnMembersAdded : ManageChatEvent
}