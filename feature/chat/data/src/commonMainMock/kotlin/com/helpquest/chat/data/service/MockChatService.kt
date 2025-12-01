package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.SubClass
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import kotlin.String
import kotlin.random.Random
import kotlin.time.Clock

class MockChatService() : ChatService {

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

    val participantList = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
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
    val lastMessage = ChatMessage(
        id = messageId,
        chatId = chatId,
        content = "this is the last message sent in the chat",
        createdAt = Clock.System.now(),
        senderId = participantFull.userId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now(),
    )

    val chat1 = Chat(
        id = chatId,
        participants = participantList,
        lastActivityAt = Clock.System.now(),
        lastMessage = lastMessage
    )

    val chat2 = Chat(
        id = Random.nextInt().toString(),
        participants = listOf(
            participantFull,
            participantDontShowID,
        ),
        lastActivityAt = Clock.System.now(),
        lastMessage = null
    )
    val chatList = listOf(
        chat1,
        chat2
    )


    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return if (otherUserIds.isEmpty()) {
            Result.Failure(DataError.Remote.SERIALIZATION)
        } else {
            val participants = emptyList<Participant>()
            for (id in otherUserIds) {
                val participant = participantList.find { it.userId == id }
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
        return Result.Success(chatList)
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return chatList.find { it.id == chatId }?.let {
            Result.Success(it)
        } ?: Result.Failure(DataError.Remote.NOT_FOUND)
    }
}