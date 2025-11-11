@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeChatService : ChatService {

    val participant = Participant(
        userId = "id1",
        username = "primo",
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
            senderId = "id5"
        )
    )

    var createChatResult: Result<Chat, DataError.Remote> =
        Result.Success(chat)

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return createChatResult
    }
}