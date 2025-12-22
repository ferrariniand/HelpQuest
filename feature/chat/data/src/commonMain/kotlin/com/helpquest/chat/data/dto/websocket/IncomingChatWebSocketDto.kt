package com.helpquest.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingChatWebSocketType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    CHAT_PARTICIPANTS_CHANGED
}

@Serializable
sealed class IncomingChatWebSocketDto(
    val type: IncomingChatWebSocketType
) {

    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String
    ) : IncomingChatWebSocketDto(IncomingChatWebSocketType.NEW_MESSAGE)

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String
    ) : IncomingChatWebSocketDto(IncomingChatWebSocketType.MESSAGE_DELETED)

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String
    ) : IncomingChatWebSocketDto(IncomingChatWebSocketType.CHAT_PARTICIPANTS_CHANGED)
}