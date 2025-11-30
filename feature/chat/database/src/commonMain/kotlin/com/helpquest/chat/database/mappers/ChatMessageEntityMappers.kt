package com.helpquest.chat.database.mappers

import com.helpquest.chat.database.db_view.LastMessageView
import com.helpquest.chat.database.entities.ChatMessageEntity

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