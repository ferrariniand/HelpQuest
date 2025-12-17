package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.quests.domain.models.QuestActivityStatus

interface ActivityRepository {
    suspend fun updateActivityStatus(
        activityId: String,
        status: QuestActivityStatus
    ): EmptyResult<DataError.Local>
}