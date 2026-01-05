package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.domain.models.OutgoingNewMessage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>

    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError>

    fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>>

    suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError>

    suspend fun retrySendMessage(messageId: String): EmptyResult<DataError>

}