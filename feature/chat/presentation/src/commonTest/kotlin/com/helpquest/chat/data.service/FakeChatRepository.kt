@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
    val chat = Chat(
        id = chatId,
        participants = listOf(participant),
        lastActivityAt = Clock.System.now(),
        lastMessage = ChatMessage(
            id = messageId,
            chatId = chatId,
            content = "test message content",
            createdAt = Clock.System.now(),
            senderId = "id5",
            deliveryStatus = ChatMessageDeliveryStatus.SENT,
            deliveredAt = Clock.System.now()
        )
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

    var chatList = listOf(chat, chat2)

    var fetchChatsResult: Result<List<Chat>, DataError.Remote> =
        Result.Success(chatList)

    override fun getChats(): Flow<List<Chat>> {
        return flowOf(chatList)
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return fetchChatsResult
    }
}