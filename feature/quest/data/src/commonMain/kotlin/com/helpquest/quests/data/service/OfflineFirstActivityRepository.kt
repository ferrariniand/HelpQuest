package com.helpquest.quests.data.service


import com.helpquest.core.data.database.safeDatabaseUpdate
import com.helpquest.core.data.dto.websocket.WebSocketMessageDto
import com.helpquest.core.data.networking.KtorWebSocketConnector
import com.helpquest.core.database.HelpQuestDatabase
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onFailure
import com.helpquest.core.domain.util.onSuccess
import com.helpquest.quests.data.dto.websocket.OutgoingQuestWebSocketDto
import com.helpquest.quests.data.mappers.toActivityWitActor
import com.helpquest.quests.data.mappers.toNewActivityEntity
import com.helpquest.quests.data.mappers.toQuestActivityEntity
import com.helpquest.quests.data.mappers.toWebSocketNewActivityDto
import com.helpquest.quests.domain.models.ActivityWithActor
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
import kotlinx.serialization.json.Json

class OfflineFirstActivityRepository(
    private val database: HelpQuestDatabase,
    private val questActivityService: QuestActivityService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
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

    override fun getActivitiesForQuest(questId: String): Flow<List<ActivityWithActor>> {
        return database
            .questActivityDao
            .getActivitiesWithActorsByQuestId(questId)
            .map { activities ->
                activities.map { it.toActivityWitActor() }
            }
    }

    override suspend fun addActivity(activity: OutgoingNewActivity): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = activity.toWebSocketNewActivityDto()

            val localUser = sessionStorage.observeAuthInfo().first()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = dto.toNewActivityEntity(
                actorId = localUser.id,
                activityStatus = QuestActivityStatus.CREATING
            )
            database.questActivityDao.upsertActivity(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.questActivityDao.upsertActivity(
                            dto.toNewActivityEntity(
                                actorId = localUser.id,
                                activityStatus = QuestActivityStatus.ERROR
                            )
                        )
                    }.join()
                }
        }
    }

    private fun OutgoingQuestWebSocketDto.NewActivity.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}