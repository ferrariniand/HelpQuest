package com.helpquest.chat.domain.models

import com.helpquest.core.domain.models.Participant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Chat(
    val id: String,
    val participants: List<Participant>,
    val lastActivityAt: Instant,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String? = null
)
