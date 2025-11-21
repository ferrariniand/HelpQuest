package com.helpquest.chat.presentation.model


import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.core.presentation.modelsUi.ParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ParticipantUi,
    val otherParticipants: List<ParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)
