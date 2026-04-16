package com.helpquest.quest.domain.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quest.domain.models.Quest

interface QuestService {

    suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote>

    suspend fun getQuestBoard(): Result<List<Quest>, DataError.Remote>
    suspend fun fetchQuestBoard(
        before: String? = null
    ): Result<List<Quest>, DataError.Remote>

    suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote>

    suspend fun getQuestById(questId: String): Result<Quest, DataError.Remote>
    suspend fun leaveQuest(questId: String): EmptyResult<DataError.Remote>


}
