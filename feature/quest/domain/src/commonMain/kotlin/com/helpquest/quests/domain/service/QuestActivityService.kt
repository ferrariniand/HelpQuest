package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.OutgoingNewActivity
import com.helpquest.quests.domain.models.QuestActivity

interface QuestActivityService {
    suspend fun fetchActivities(
        questId: String,
        before: String? = null
    ): Result<List<QuestActivity>, DataError.Remote>

    suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError.Connection>
}