package com.helpquest.quest.domain.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quest.domain.models.Quest
import com.helpquest.quest.domain.models.QuestInfo
import kotlinx.coroutines.flow.Flow

interface QuestRepository {
    fun getQuestLog(): Flow<List<Quest>>
    fun getQuestBoard(): Flow<List<Quest>>
    fun getQuestInfoById(questId: String): Flow<QuestInfo>

    suspend fun fetchQuestLog(): Result<List<Quest>, DataError.Remote>
    suspend fun fetchQuestBoard(
        before: String? = null
    ): Result<List<Quest>, DataError>

    suspend fun fetchQuestById(questId: String): EmptyResult<DataError.Remote>

    suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote>

    suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote>

}