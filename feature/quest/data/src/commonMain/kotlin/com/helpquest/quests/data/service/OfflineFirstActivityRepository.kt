package com.helpquest.quests.data.service


import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quests.data.mappers.toActivityWitCreator
import com.helpquest.quests.data.mappers.toNewActivityEntity
import com.helpquest.quests.data.mappers.toQuestActivityEntity
import com.helpquest.quests.domain.models.ActivityWithCreator
import com.helpquest.quests.domain.models.OutgoingNewActivity
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.service.ActivityRepository
import com.helpquest.quests.domain.service.QuestActivityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OfflineFirstActivityRepository(
    private val database: HelpQuestDatabase,
    private val questActivityService: QuestActivityService,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
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

    override fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithCreator>> {
        return database
            .questActivityDao
            .getActivitiesWithCreatorsByQuestId(questId)
            .map { activities ->
                activities.map { it.toActivityWitCreator() }
            }
    }

    override suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError> {
        return safeDatabaseUpdate {

            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = activity.toNewActivityEntity(
                creatorId = localUser.id,
                activityStatus = QuestActivityStatus.CREATING
            )
            database.questActivityDao.upsertActivity(entity)

            return questActivityService
                .addActivity(activity)
                .onFailure { error ->
                    applicationScope.launch {
                        database.questActivityDao.updateActivityStatus(
                            activityId = entity.activityId,
                            status = QuestActivityStatus.ERROR.name
                            )
                    }.join()
                }
        }
    }

    override suspend fun retryAddActivity(activityId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val activity = database.questActivityDao.getActivityById(activityId)
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            database.questActivityDao.updateActivityStatus(
                activityId = activityId,
                status = QuestActivityStatus.CREATING.name
            )

            val outgoingNewActivity = OutgoingNewActivity(
                questId = activity.questId,
                activityId = activityId,
                content = activity.content
            )
            return questActivityService
                .addActivity(outgoingNewActivity)
                .onFailure {
                    applicationScope.launch {
                        database.questActivityDao.updateActivityStatus(
                            activityId = activityId,
                            status = QuestActivityStatus.ERROR.name
                        )
                    }.join()
                }
        }
    }

    override suspend fun deleteActivity(
        activityId: String,
        activityStatus: QuestActivityStatus
    ): EmptyResult<DataError> {
        return if (activityStatus == QuestActivityStatus.ERROR) {
            //if activity is not sent to the server, should be removed just locally
            return safeDatabaseUpdate {
                applicationScope.launch {
                    database.questActivityDao.deleteActivityById(activityId)
                }.join()
            }
        } else {
            questActivityService
                .deleteActivity(activityId)
                .onSuccess {
                    applicationScope.launch {
                        database.questActivityDao.deleteActivityById(activityId)
                    }.join()
                }

        }
    }
}