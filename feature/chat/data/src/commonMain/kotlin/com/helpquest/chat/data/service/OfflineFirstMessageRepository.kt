package com.helpquest.chat.data.service


import com.helpquest.chat.data.dto.websocket.OutgoingChatWebSocketDto
import com.helpquest.chat.data.mappers.toChatMessageEntity
import com.helpquest.chat.data.mappers.toMessageWithSender
import com.helpquest.chat.data.mappers.toNewMessageEntity
import com.helpquest.chat.data.mappers.toWebSocketNewMessageDto
import com.helpquest.chat.data.message.ChatMessageConstants
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.domain.models.OutgoingNewMessage
import com.helpquest.chat.domain.service.ChatMessageService
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: HelpQuestDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }


    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toChatMessageEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null // Only sync for most recent page
                    )
                    messages
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database
            .chatMessageDao
            .getMessagesWithSendersByChatId(chatId)
            .map { messages ->
                messages.map { it.toMessageWithSender() }
            }
    }

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketNewMessageDto()

            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = dto.toNewMessageEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )
            database.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.chatMessageDao.upsertMessage(
                            dto.toNewMessageEntity(
                                senderId = localUser.id,
                                deliveryStatus = ChatMessageDeliveryStatus.FAILED
                            )
                        )
                    }.join()
                }
        }
    }


    private fun OutgoingChatWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}