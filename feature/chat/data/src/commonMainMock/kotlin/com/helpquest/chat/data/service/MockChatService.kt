package com.helpquest.chat.data.service

import com.helpquest.chat.data.service.MockChatResponseElements
import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.SubClass
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import kotlin.String
import kotlin.random.Random
import kotlin.time.Clock

class MockChatService(
    val mockResponse: MockChatResponseElements
) : ChatService {


    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return if (otherUserIds.isEmpty()) {
            Result.Failure(DataError.Remote.SERIALIZATION)
        } else {
            val participants = emptyList<Participant>()
            for (id in otherUserIds) {
                val participant = mockResponse.participantList.find { it.userId == id }
                if (participant != null) {
                    participants.plus(participant)
                }
            }
            val chat = Chat(
                id = Random.nextInt().toString(),
                participants = participants,
                lastActivityAt = Clock.System.now(),
                lastMessage = null
            )

            Result.Success(chat)
        }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return Result.Success(mockResponse.chatList)
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return mockResponse.chatList.find { it.id == chatId }?.let {
            Result.Success(it)
        } ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return mockResponse.chatList.find { it.id == chatId }?.let {
            mockResponse.chatList.remove(it)
            Result.Success(Unit)
        }?.asEmptyResult() ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        val newParticipants = mutableListOf<Participant>()
        userIds.forEach { currentUserId ->
            mockResponse.allPossibleParticipants.find { it.userId == currentUserId }
                ?.let { foundParticipant ->
                newParticipants.add(foundParticipant)
            }
        }

        return mockResponse.chatList.find { it.id == chatId }?.let { chat ->
            val totalParticipants = chat.participants + newParticipants
            Result.Success(
                Chat(
                    id = chatId,
                    participants = totalParticipants,
                    lastActivityAt = chat.lastActivityAt,
                    lastMessage = chat.lastMessage
                )
            )
        } ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }
}