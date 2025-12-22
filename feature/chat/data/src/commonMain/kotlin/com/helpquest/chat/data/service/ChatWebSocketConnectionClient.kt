package com.helpquest.chat.data.service


import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketDto
import com.helpquest.chat.data.dto.websocket.IncomingChatWebSocketType
import com.helpquest.chat.data.mappers.toChatMessage
import com.helpquest.chat.data.mappers.toChatMessageEntity
import com.helpquest.chat.data.mappers.toNewMessage
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.util.ConnectionError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class ChatWebSocketConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: HelpQuestDatabase,
    private val json: Json,
    private val messageRepository: MessageRepository,
    private val applicationScope: CoroutineScope
) : ChatConnectionClient {

    override val chatMessages = webSocketConnector
        .messages
        .mapNotNull { parseIncomingMessage(it) }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingChatWebSocketDto.NewMessageDto>()
        .mapNotNull {
            database.chatMessageDao.getMessageById(it.id)?.toChatMessage()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000)
        )

    override val connectionState = webSocketConnector.connectionState

    override suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError> {
        val outgoingDto = message.toNewMessage()
        val webSocketMessage = WebSocketMessageDto(
            type = outgoingDto.type.name,
            payload = json.encodeToString(outgoingDto)
        )
        val rawJsonPayload = json.encodeToString(webSocketMessage)

        return webSocketConnector
            .sendMessage(rawJsonPayload)
            .onFailure { error ->
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = ChatMessageDeliveryStatus.FAILED
                )
            }
    }

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingChatWebSocketDto? {
        return when (message.type) {
            IncomingChatWebSocketType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.NewMessageDto>(message.payload)
            }

            IncomingChatWebSocketType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.MessageDeletedDto>(message.payload)
            }

            IncomingChatWebSocketType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingChatWebSocketDto.ChatParticipantsChangedDto>(message.payload)
            }

            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingChatWebSocketDto) {
        when (message) {
            is IncomingChatWebSocketDto.ChatParticipantsChangedDto -> refreshChat(message)
            is IncomingChatWebSocketDto.MessageDeletedDto -> deleteMessage(message)
            is IncomingChatWebSocketDto.NewMessageDto -> handleNewMessage(message)
        }
    }

    private suspend fun refreshChat(message: IncomingChatWebSocketDto.ChatParticipantsChangedDto) {
        chatRepository.fetchChatById(message.chatId)
    }

    private suspend fun deleteMessage(message: IncomingChatWebSocketDto.MessageDeletedDto) {
        database.chatMessageDao.deleteMessageById(message.messageId)
    }

    private suspend fun handleNewMessage(message: IncomingChatWebSocketDto.NewMessageDto) {
        val chatExists = database.chatDao.getChatById(message.chatId) != null
        if (!chatExists) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toChatMessageEntity()
        database.chatMessageDao.upsertMessage(entity)
    }
}