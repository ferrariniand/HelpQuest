package com.helpquest.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingChatWebSocketType {
    NEW_MESSAGE
}

@Serializable
sealed interface OutgoingChatWebSocketDto {

    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String,
        val type: OutgoingChatWebSocketType = OutgoingChatWebSocketType.NEW_MESSAGE
    ) : OutgoingChatWebSocketDto
}