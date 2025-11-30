package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.Quest
import kotlinx.coroutines.flow.Flow

interface QuestLogRepository {
    fun getQuestLog(): Flow<List<Quest>>
    suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote>
}