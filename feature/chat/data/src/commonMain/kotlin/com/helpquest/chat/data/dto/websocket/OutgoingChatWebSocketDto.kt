package com.helpquest.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingChatWebSocketType {
    NEW_MESSAGE
}

@Serializable
sealed class OutgoingChatWebSocketDto(
    val type: OutgoingChatWebSocketType
) {

    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String
    ) : OutgoingChatWebSocketDto(OutgoingChatWebSocketType.NEW_MESSAGE)
}