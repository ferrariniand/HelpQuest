package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.ActivityWithActor
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun updateActivityStatus(
        activityId: String,
        status: QuestActivityStatus
    ): EmptyResult<DataError.Local>


    suspend fun fetchActivities(
        questId: String,
        before: String? = null
    ): Result<List<QuestActivity>, DataError>

    fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithActor>>
}