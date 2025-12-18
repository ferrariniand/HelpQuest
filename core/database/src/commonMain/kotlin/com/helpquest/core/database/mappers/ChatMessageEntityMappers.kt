package com.helpquest.core.database.mappers

import com.helpquest.core.database.db_view.LastMessageView
import com.helpquest.core.database.entities.chat.ChatMessageEntity

fun LastMessageView.toChatMessageEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        deliveryStatus = deliveryStatus,
        deliveryStatusTimestamp = deliveryStatusTimestamp
    )
}