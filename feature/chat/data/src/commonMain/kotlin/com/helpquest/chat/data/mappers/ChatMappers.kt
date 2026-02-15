@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.mappers

import com.helpquest.chat.data.dto.ChatDto
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatInfo
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.database.entities.chat.ChatEntity
import com.helpquest.core.database.entities.chat.ChatInfoEntity
import com.helpquest.core.database.entities.chat.ChatWithParticipants
import com.helpquest.core.database.entities.chat.MessageWithSenderEntity
import com.helpquest.core.domain.models.Participant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun ChatDto.toChat(): Chat {
    val lastMessageSenderUsername = lastMessage?.let { message ->
        participants.find { it.userId == message.senderId }?.username
    }
    return Chat(
        id = id,
        participants = participants.map { it.toParticipant() },
        lastActivityAt = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toChatMessage(),
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}

fun ChatEntity.toChat(
    participants: List<Participant>,
    lastMessage: ChatMessage? = null
): Chat {
    val lastMessageSenderUsername = lastMessage?.let { message ->
        participants.find { it.userId == message.senderId }?.username
    }
    return Chat(
        id = chatId,
        participants = participants,
        lastActivityAt = Instant.fromEpochMilliseconds(lastActivityTimestamp),
        lastMessage = lastMessage,
        lastMessageSenderUsername = lastMessageSenderUsername
    )
}

fun ChatWithParticipants.toChat(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toParticipant() },
        lastActivityAt = Instant.fromEpochMilliseconds(chat.lastActivityTimestamp),
        lastMessage = lastMessage?.toChatMessage(),
        lastMessageSenderUsername = lastMessage?.senderUsername
    )
}

fun Chat.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityTimestamp = lastActivityAt.toEpochMilliseconds()
    )
}

fun MessageWithSenderEntity.toMessageWithSender(): MessageWithSender {
    return MessageWithSender(
        message = message.toChatMessage(),
        sender = sender.toParticipant(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.message.deliveryStatus)
    )
}

fun ChatInfoEntity.toChatInfo(): ChatInfo {
    return ChatInfo(
        chat = chat.toChat(
            participants = participants.map { it.toParticipant() }
        ),
        messages = messagesWithSenders.map { it.toMessageWithSender() }
    )
}