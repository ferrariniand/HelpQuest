package com.helpquest.quests.data.service


import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.quest.database.QuestDatabase
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.service.ActivityRepository

class OfflineFirstActivityRepository(
    private val database: QuestDatabase
) : ActivityRepository {

    override suspend fun updateActivityStatus(
        activityId: String,
        status: QuestActivityStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.questActivityDao.updateActivityStatus(
                activityId = activityId,
                status = status.name
            )
        }
    }
}