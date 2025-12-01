package com.helpquest.quests.domain.service

import com.helpquest.core.domain.models.Category
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.Quest

interface QuestService {

    suspend fun createQuest(
        questTitle: String,
        questDescription: String,
        questCategory: Category,
        questCreatorId: String,
    ): Result<Quest, DataError.Remote>

    suspend fun getQuestBoard(): Result<List<Quest>, DataError.Remote>
    suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote>

    suspend fun getQuestById(questId: String): Result<Quest, DataError.Remote>

}
