package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlin.random.Random
import kotlin.time.Clock

object MockChatResponseElements {

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

    val chatId1 = Random.nextInt().toString()
    val chatId2 = Random.nextInt().toString()
    val messageId1 = Random.nextInt().toString()
    val messageId2 = Random.nextInt().toString()
    val messageId3 = Random.nextInt().toString()
    val message1 = ChatMessage(
        id = messageId1,
        chatId = chatId1,
        content = "this is the first message sent in the chat",
        createdAt = Clock.System.now().minus(24, DateTimeUnit.HOUR),
        senderId = participantFull.userId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now().minus(24, DateTimeUnit.HOUR),
    )
    val message2 = ChatMessage(
        id = messageId2,
        chatId = chatId1,
        content = "this is the second message sent in the chat",
        createdAt = Clock.System.now().minus(20, DateTimeUnit.HOUR),
        senderId = participantFull.userId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now().minus(20, DateTimeUnit.HOUR),
    )

    val message3 = ChatMessage(
        id = messageId3,
        chatId = chatId1,
        content = "message 3",
        createdAt = Clock.System.now().minus(2, DateTimeUnit.HOUR),
        senderId = participantFull.userId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now().minus(2, DateTimeUnit.HOUR),
    )

    val chat1 = Chat(
        id = chatId1,
        participants = participantList,
        lastActivityAt = Clock.System.now(),
        lastMessage = message3
    )

    val chat2 = Chat(
        id = chatId2,
        participants = listOf(
            participantFull,
            participantDontShowID,
        ),
        lastActivityAt = Clock.System.now(),
        lastMessage = null
    )
    val chatList = mutableListOf(
        chat1,
        chat2
    )
}