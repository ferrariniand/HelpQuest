package com.helpquest.chat.data.service


import com.helpquest.chat.database.ChatDatabase
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: ChatDatabase
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
}