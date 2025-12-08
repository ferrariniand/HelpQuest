@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatInfo
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import com.helpquest.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeChatRepository : ChatRepository {

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
    val chatId = Random.nextInt().toString()
    val messageId = Random.nextInt().toString()

    val message = ChatMessage(
        id = messageId,
        chatId = chatId,
        content = "test message content",
        createdAt = Clock.System.now(),
        senderId = "id2",
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now()
    )

    val messageWithSender = MessageWithSender(
        message = message,
        sender = participant2,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
    )

    val chat = Chat(
        id = chatId,
        participants = listOf(participant, participant2),
        lastActivityAt = Clock.System.now(),
        lastMessage = message
    )

    val chatInfo = ChatInfo(
        chat = chat,
        messages = listOf(messageWithSender, messageWithSender, messageWithSender)
    )


    val chat2 = Chat(
        id = Random.nextInt().toString(),
        participants = listOf(
            participant,
            participant2,
        ),
        lastActivityAt = Clock.System.now(),
        lastMessage = null
    )

    val chatInfo2 = ChatInfo(
        chat = chat2,
        messages = emptyList()
    )

    var chatList = mutableListOf(chat, chat2)
    val chatListFlow = MutableStateFlow(chatList)
    var chatInfoList = mutableListOf(chatInfo, chatInfo2)

    var fetchChatsResult: Result<List<Chat>, DataError.Remote> =
        Result.Success(chatList)

    var fetchChatByIdResult: EmptyResult<DataError.Remote> =
        Result.Success(chat).asEmptyResult()

    var createChatResult: Result<Chat, DataError.Remote> =
        Result.Success(chat)

    var leaveChatResult: EmptyResult<DataError.Remote> =
        Result.Success(chat).asEmptyResult()

    override fun getChats(): Flow<List<Chat>> {
        return chatListFlow
    }

    override fun getChatInfoById(chatId: String): Flow<ChatInfo> {
        return flowOf(chatInfoList).map { list -> list.find { it.chat.id == chatId } }
            .filterNotNull()
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return fetchChatsResult
    }

    override suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote> {
        return fetchChatByIdResult
    }

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return createChatResult
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return leaveChatResult
            .onSuccess {
                chatList.find { it.id == chatId }?.let {
                    chatList.remove(it)
                    Result.Success(Unit)
                }?.asEmptyResult()
            }
    }
}