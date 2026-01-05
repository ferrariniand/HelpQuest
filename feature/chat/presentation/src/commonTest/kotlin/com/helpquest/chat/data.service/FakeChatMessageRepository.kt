@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.domain.models.OutgoingNewMessage
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeChatMessageRepository : MessageRepository {

    val participant = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",

        )

    val participant2 = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
    )

    val participant3 = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = "test",
    )
    val chatId = Random.nextInt().toString()
    val messageId1 = Random.nextInt().toString()
    val messageId2 = Random.nextInt().toString()

    val message1 = ChatMessage(
        id = messageId1,
        chatId = chatId,
        content = "test message content",
        createdAt = Clock.System.now(),
        senderId = "id2",
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now()
    )

    val message2 = ChatMessage(
        id = messageId2,
        chatId = chatId,
        content = "test message content",
        createdAt = Clock.System.now(),
        senderId = "id1",
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now()
    )

    val messageList = mutableListOf(
        message1,
        message2
    )

    val messageWithSender1 = MessageWithSender(
        message = message1,
        sender = participant2,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
    )
    val messageWithSender2 = MessageWithSender(
        message = message2,
        sender = participant,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
    )

    val messageWithSenderList = mutableListOf(
        messageWithSender1,
        messageWithSender2
    )


    var fetchMessagesResult: Result<List<ChatMessage>, DataError.Remote> =
        Result.Success(
            messageList.filter { it.chatId == chatId }
        )

    var updateMessageDeliveryStatusResult: EmptyResult<DataError.Local> =
        Result.Success(message1.deliveryStatus).asEmptyResult()

    var sendMessageResult: EmptyResult<DataError> =
        Result.Success(message1).asEmptyResult()

    var retryMessageResult: EmptyResult<DataError> =
        Result.Success(message1).asEmptyResult()

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return updateMessageDeliveryStatusResult
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return fetchMessagesResult
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return flowOf(messageWithSenderList)
    }

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return sendMessageResult
    }

    override suspend fun retrySendMessage(messageId: String): EmptyResult<DataError> {
        return retryMessageResult
    }

}