@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.mappers

import com.helpquest.chat.data.dto.ChatMessageDto
import com.helpquest.chat.data.dto.websocket.OutgoingChatWebSocketDto
import com.helpquest.chat.database.db_view.LastMessageView
import com.helpquest.chat.database.entities.ChatMessageEntity
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun ChatMessageDto.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(createdAt),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(deliveryStatus),
        deliveredAt = Instant.parse(deliveredAt),
    )
}

fun ChatMessageEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.deliveryStatus),
        deliveredAt = Instant.fromEpochMilliseconds(deliveryStatusTimestamp),
    )
}

fun LastMessageView.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(deliveryStatus),
        deliveredAt = Instant.fromEpochMilliseconds(deliveryStatusTimestamp),
    )
}

fun ChatMessage.toChatMessageEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name,
        deliveryStatusTimestamp = deliveredAt.toEpochMilliseconds()
    )
}

fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name,
        deliveryStatusTimestamp = deliveredAt.toEpochMilliseconds()
    )
}

fun ChatMessage.toNewMessage(): OutgoingChatWebSocketDto.NewMessage {
    return OutgoingChatWebSocketDto.NewMessage(
        messageId = id,
        chatId = chatId,
        content = content,
    )
}