package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.OutgoingNewMessage
import com.helpquest.chat.domain.service.ChatMessageService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

class MockChatMessageService(
    val mockResponse: MockChatResponseElements
) : ChatMessageService {

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        val messageList = listOf(
            mockResponse.message1.copy(
                chatId = chatId
            ),
            mockResponse.message2.copy(
                chatId = chatId
            ),
            mockResponse.message3.copy(
                chatId = chatId
            ),
        )
        return Result.Success(messageList)
    }

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError.Connection> {
        return Result.Success(Unit)
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return Result.Success(Unit)
    }

}