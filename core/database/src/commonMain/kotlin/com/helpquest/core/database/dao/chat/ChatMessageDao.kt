package com.helpquest.core.database.dao.chat

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.entities.chat.ChatMessageEntity
import com.helpquest.core.database.entities.chat.DELIVERY_STATUS_SENT
import com.helpquest.core.database.entities.chat.MessageWithSenderEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface ChatMessageDao {

    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Transaction
    suspend fun upsertMessagesAndSyncIfNecessary(
        chatId: String,
        serverMessages: List<ChatMessageEntity>,
        pageSize: Int,
        shouldSync: Boolean = false
    ) {
        val localMessages = getMessagesByChatIdLimited(
            chatId = chatId,
            limit = pageSize
        ).first()

        upsertMessages(serverMessages)

        if (!shouldSync) {
            return
        }

        val serverIds = serverMessages.map { it.messageId }.toSet()

        val messagesToDelete = localMessages.filter { localMessage ->
            val missingOnServer = localMessage.messageId !in serverIds
            val isSent = localMessage.deliveryStatus == DELIVERY_STATUS_SENT

            missingOnServer && isSent
        }

        val messageIds = messagesToDelete.map { it.messageId }
        deleteMessagesByIds(messageIds)
    }

    @Query("SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesWithSendersByChatId(chatId: String): Flow<List<MessageWithSenderEntity>>

    @Query(
        """
        SELECT *
        FROM chatmessageentity
        WHERE chatId = :chatId
        ORDER BY timestamp DESC
        LIMIT :limit
    """
    )
    fun getMessagesByChatIdLimited(chatId: String, limit: Int): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    @Query("DELETE FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM chatmessageentity WHERE messageId IN (:messageIds)")
    suspend fun deleteMessagesByIds(messageIds: List<String>)

    @Query(
        """
        UPDATE chatmessageentity
        SET deliveryStatus = :status, deliveryStatusTimestamp = :timestamp
        WHERE messageId = :messageId
    """
    )
    suspend fun updateDeliveryStatus(messageId: String, status: String, timestamp: Long)
}