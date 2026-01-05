@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.data.service

import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.core.domain.util.ConnectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class FakeChatConnectionClient : ChatConnectionClient {

    val chatId = Random.nextInt().toString()
    val messageId1 = Random.nextInt().toString()
    var chatMessage = ChatMessage(
        id = messageId1,
        chatId = chatId,
        content = "test message content",
        createdAt = Clock.System.now(),
        senderId = "id2",
        deliveryStatus = ChatMessageDeliveryStatus.SENT,
        deliveredAt = Clock.System.now()
    )

    override val chatMessages = flowOf<ChatMessage>()

    var conState = ConnectionState.DISCONNECTED
    override val connectionState = MutableStateFlow(conState)
}