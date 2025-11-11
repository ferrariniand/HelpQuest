package com.helpquest.chat.data.dto

import com.helpquest.core.data.dto.ParticipantDto
import kotlinx.serialization.Serializable

@Serializable
data class ChatDto(
    val id: String,
    val participants: List<ParticipantDto>,
    val lastActivityAt: String,
    val lastMessage: ChatMessageDto?
)
