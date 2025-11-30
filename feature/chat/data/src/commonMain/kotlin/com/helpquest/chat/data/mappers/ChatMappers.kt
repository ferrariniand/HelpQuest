@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.mappers

import com.helpquest.chat.data.dto.ChatDto
import com.helpquest.chat.database.entities.ChatEntity
import com.helpquest.chat.database.entities.ChatWithParticipants
import com.helpquest.chat.domain.models.Chat
import com.helpquest.core.data.mappers.toParticipant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun ChatDto.toChat(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toParticipant() },
        lastActivityAt = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toChatMessage()
    )
}

fun ChatWithParticipants.toChat(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toParticipant() },
        lastActivityAt = Instant.fromEpochMilliseconds(chat.lastActivityTimestamp),
        lastMessage = lastMessage?.toChatMessage()
    )
}

fun Chat.toChatEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityTimestamp = lastActivityAt.toEpochMilliseconds()
    )
}