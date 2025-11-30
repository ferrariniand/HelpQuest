package com.helpquest.chat.domain.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class ChatMessage(
    val id: String,
    val chatId: String,
    val content: String,
    val createdAt: Instant,
    val senderId: String,
    val deliveryStatus: ChatMessageDeliveryStatus,
    val deliveredAt: Instant
)
