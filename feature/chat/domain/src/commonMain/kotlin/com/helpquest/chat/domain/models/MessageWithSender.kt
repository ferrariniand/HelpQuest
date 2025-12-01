package com.helpquest.chat.domain.models

import com.helpquest.core.domain.models.Participant

data class MessageWithSender(
    val message: ChatMessage,
    val sender: Participant,
    val deliveryStatus: ChatMessageDeliveryStatus?
)
