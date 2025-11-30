package com.helpquest.chat.domain.service

import com.helpquest.chat.domain.models.Chat
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
}