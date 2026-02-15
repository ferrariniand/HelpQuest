package com.helpquest.chat.presentation.mappers

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.core.presentation.mappers.toParticipantUi


fun Chat.toChatUi(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUi(
        id = id,
        localParticipant = local.first().toParticipantUi(),
        otherParticipants = other.map { it.toParticipantUi() },
        lastMessage = lastMessage,
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}