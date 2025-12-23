package com.helpquest.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingChatWebSocketType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    CHAT_PARTICIPANTS_CHANGED
}

@Serializable
sealed interface IncomingChatWebSocketDto {

    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String,
        val type: IncomingChatWebSocketType = IncomingChatWebSocketType.NEW_MESSAGE
    ) : IncomingChatWebSocketDto

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String,
        val type: IncomingChatWebSocketType = IncomingChatWebSocketType.MESSAGE_DELETED
    ) : IncomingChatWebSocketDto

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String,
        val type: IncomingChatWebSocketType = IncomingChatWebSocketType.CHAT_PARTICIPANTS_CHANGED
    ) : IncomingChatWebSocketDto
}