package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestActivityDao {

    @Upsert
    suspend fun upsertActivity(activity: QuestActivityEntity)

    @Upsert
    suspend fun upsertActivities(activities: List<QuestActivityEntity>)

    @Query("DELETE FROM questactivityentity WHERE activityId = :activityId")
    suspend fun deleteActivityById(activityId: String)

    @Query("DELETE FROM questactivityentity WHERE questId = :questId")
    suspend fun deleteAllActivities(questId: String)

    @Query("SELECT * FROM questactivityentity WHERE questId = :questId ORDER BY startTimestamp DESC")
    fun getActivitiesByQuestId(questId: String): Flow<List<QuestActivityEntity>>

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