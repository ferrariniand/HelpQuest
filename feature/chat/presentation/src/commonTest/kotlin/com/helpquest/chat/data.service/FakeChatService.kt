@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeChatService : ChatService {

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

    val participantFull = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
        participantClass = Class.VILLAGER,
    )

    val participantNoClass = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
    )

    val participantNoImage = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = null,
        showParticipantIdentity = true,
        participantClass = Class.TECH_WIZARD,
        participantSubClass = SubClass.SOFTWARE_MAGE,
    )

    val participantDontShowID = Participant(
        userId = "id4",
        username = "quarto",
        profilePictureUrl = "test",
        showParticipantIdentity = false,
        participantClass = Class.VILLAGER,
    )

    val participantNoImageDontShowID = Participant(
        userId = "id5",
        username = "quinto",
        profilePictureUrl = null,
        showParticipantIdentity = false,
        participantClass = Class.VILLAGER,
    )

    val allPossibleParticipants = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
        participantDontShowID,
        participantNoImageDontShowID
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

    val chatList = mutableListOf(chat, chat2)

    var createChatResult: Result<Chat, DataError.Remote> =
        Result.Success(chat)

    var getChatsResult: Result<List<Chat>, DataError.Remote> =
        Result.Success(chatList)

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return createChatResult
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return getChatsResult
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return chatList.find { it.id == chatId }?.let {
            Result.Success(it)
        } ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return chatList.find { it.id == chatId }?.let {
            chatList.remove(it)
            Result.Success(Unit)
        }?.asEmptyResult() ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        val newParticipants = mutableListOf<Participant>()
        userIds.forEach { currentUserId ->
            allPossibleParticipants.find { it.userId == currentUserId }?.let { foundParticipant ->
                newParticipants.add(foundParticipant)
            }
        }

        return chatList.find { it.id == chatId }?.let { chat ->
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