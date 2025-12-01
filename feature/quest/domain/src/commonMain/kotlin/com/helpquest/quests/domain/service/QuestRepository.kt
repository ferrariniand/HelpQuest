package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.domain.models.QuestInfo
import kotlinx.coroutines.flow.Flow

interface QuestRepository {
    fun getQuestLog(): Flow<List<Quest>>
    fun getQuestBoard(): Flow<List<Quest>>
    fun getQuestInfoById(questId: String): Flow<QuestInfo>

    suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote>
    suspend fun fetchQuestBoard(): Result<List<Quest>, DataError.Remote>

    suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote>

}