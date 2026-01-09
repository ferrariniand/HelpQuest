package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.entities.quest.ACTIVITY_STATUS_ERROR
import com.helpquest.core.database.entities.quest.ActivityWithCreatorEntity
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface QuestActivityDao {

    @Upsert
    suspend fun upsertActivity(activity: QuestActivityEntity)

    @Upsert
    suspend fun upsertActivities(activities: List<QuestActivityEntity>)

    @Transaction
    suspend fun upsertActivitiesAndSyncIfNecessary(
        questId: String,
        serverActivities: List<QuestActivityEntity>,
        shouldSync: Boolean = false
    ) {
        val localActivities = getActivitiesByQuestId(
            questId = questId,
        ).first()

        upsertActivities(serverActivities)

        if (!shouldSync) {
            return
        }

        val serverIds = serverActivities.map { it.activityId }.toSet()

        val activitiesToDelete = localActivities.filter { localActivity ->
            val missingOnServer = localActivity.activityId !in serverIds
            val isNotError = localActivity.activityStatus != ACTIVITY_STATUS_ERROR

            missingOnServer && isNotError
        }

        val activityIds = activitiesToDelete.map { it.activityId }
        deleteActivitiesByIds(activityIds)
    }


    @Query("DELETE FROM questactivityentity WHERE activityId = :activityId")
    suspend fun deleteActivityById(activityId: String)

    @Query("DELETE FROM questactivityentity WHERE activityId IN (:activityIds)")
    suspend fun deleteActivitiesByIds(activityIds: List<String>)

    @Query("DELETE FROM questactivityentity WHERE questId = :questId")
    suspend fun deleteAllActivities(questId: String)

    @Query("SELECT * FROM questactivityentity WHERE questId = :questId ORDER BY startTimestamp DESC")
    fun getActivitiesByQuestId(questId: String): Flow<List<QuestActivityEntity>>

    @Query("SELECT * FROM questactivityentity WHERE questId = :questId ORDER BY startTimestamp DESC")
    fun getActivitiesWithCreatorsByQuestId(questId: String): Flow<List<ActivityWithCreatorEntity>>

    @Query("SELECT * FROM questactivityentity WHERE activityId = :activityId")
    suspend fun getActivityById(activityId: String): QuestActivityEntity?


    @Query(
        """
        UPDATE questactivityentity
        SET activityStatus = :status
        WHERE activityId = :activityId
    """
    )
    suspend fun updateActivityStatus(activityId: String, status: String)
}