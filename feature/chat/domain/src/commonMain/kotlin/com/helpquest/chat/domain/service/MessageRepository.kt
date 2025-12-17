package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult

interface MessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>
}