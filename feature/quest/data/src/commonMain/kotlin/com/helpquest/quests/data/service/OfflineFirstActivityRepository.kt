package com.helpquest.quests.data.service


import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quests.data.mappers.toActivityWitActor
import com.helpquest.quests.data.mappers.toQuestActivityEntity
import com.helpquest.quests.domain.models.ActivityWithActor
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.service.ActivityRepository
import com.helpquest.quests.domain.service.QuestActivityService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineFirstActivityRepository(
    private val database: HelpQuestDatabase,
    private val questActivityService: QuestActivityService
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

    override suspend fun fetchActivities(
        questId: String,
        before: String?
    ): Result<List<QuestActivity>, DataError> {
        return questActivityService
            .fetchActivities(questId, before)
            .onSuccess { activities ->
                return safeDatabaseUpdate {
                    database.questActivityDao.upsertActivitiesAndSyncIfNecessary(
                        questId = questId,
                        serverActivities = activities.map { it.toQuestActivityEntity() },
                        shouldSync = before == null // Only sync for most recent page
                    )
                    activities
                }
            }
    }

    override fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithActor>> {
        return database
            .questActivityDao
            .getActivitiesWithActorsByQuestId(questId)
            .map { activities ->
                activities.map { it.toActivityWitActor() }
            }
    }
}