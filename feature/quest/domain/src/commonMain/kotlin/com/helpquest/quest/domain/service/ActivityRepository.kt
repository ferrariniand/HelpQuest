package com.helpquest.quest.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quest.domain.models.ActivityWithCreator
import com.helpquest.quest.domain.models.OutgoingNewActivity
import com.helpquest.quest.domain.models.QuestActivity
import com.helpquest.quest.domain.models.QuestActivityStatus
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

    fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithCreator>>

    suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError>

    suspend fun retryAddActivity(activityId: String): EmptyResult<DataError>

    suspend fun deleteActivity(
        activityId: String,
        activityStatus: QuestActivityStatus
    ): EmptyResult<DataError>

}