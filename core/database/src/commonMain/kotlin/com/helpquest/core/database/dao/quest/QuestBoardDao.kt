package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.dao.ParticipantDao
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import com.helpquest.core.database.entities.quest.QuestEntity
import com.helpquest.core.database.entities.quest.QuestParticipantCrossRef
import com.helpquest.core.database.entities.quest.QuestWithParticipants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

//TODO just a DRAFT for QuestBoardDao, MAYBE SHOULD BE MERGED WITH QUESTLOG DAO
@Dao
interface QuestBoardDao {

    @Upsert
    suspend fun upsertQuest(quest: QuestEntity)

    @Upsert
    suspend fun upsertQuests(quests: List<QuestEntity>)

    @Transaction
    suspend fun upsertQuestsWithParticipantsAndCrossRefsAndSyncIfNecessary(
        serverQuests: List<QuestWithParticipants>,
        participantDao: ParticipantDao,
        crossRefDao: QuestParticipantsCrossRefDao,
        activityDao: QuestActivityDao,
        pageSize: Int,
        shouldSync: Boolean = false
    ) {
        val localQuests = getQuestsLimited(
            limit = pageSize
        ).first()

        upsertQuests(serverQuests.map { it.quest })

        if (!shouldSync) {
            return
        }

        val serverIds = serverQuests.map { it.quest.questId }.toSet()

        serverQuests.forEach { quest ->
            quest.lastActivity?.run {
                activityDao.upsertActivity(
                    QuestActivityEntity(
                        activityId = activityId,
                        questId = questId,
                        creatorId = creatorId,
                        actorId = actorId,
                        content = content,
                        activityStatus = activityStatus,
                        startTimestamp = startTimestamp,
                        lastActivityUpdateTimestamp = lastActivityUpdateTimestamp,
                        endTimestamp = endTimestamp,
                    )
                )

            }
        }

        val allParticipants = serverQuests.flatMap { it.participants }
        participantDao.upsertParticipants(allParticipants)

        val allCrossRefs = serverQuests.flatMap { questWithParticipants ->
            questWithParticipants.participants.map { participant ->
                QuestParticipantCrossRef(
                    questId = questWithParticipants.quest.questId,
                    userId = participant.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(allCrossRefs)

        serverQuests.forEach { quest ->
            crossRefDao.syncQuestParticipants(
                questId = quest.quest.questId,
                participants = quest.participants
            )
        }

        val questsToDelete = localQuests.filter { localQuest ->
            val missingOnServer = localQuest.questId !in serverIds
            //TODO the DRAFT quest probably should NOT be deleted

            missingOnServer
        }

        val questIds = questsToDelete.map { it.questId }
        deleteQuestsByIds(questIds)
    }

    @Query("SELECT * FROM questentity WHERE questId = :id")
    suspend fun getQuestById(id: String): QuestWithParticipants?

    @Query(
        """
        SELECT *
        FROM questentity
        ORDER BY createdTimestamp DESC
        LIMIT :limit
    """
    )
    fun getQuestsLimited(limit: Int): Flow<List<QuestEntity>>

    @Query("SELECT questId FROM questentity")
    suspend fun getAllQuestIds(): List<String>

    @Query(
        """
        SELECT * 
        FROM questentity 
        WHERE questStatus = :questStatus 
        ORDER BY createdTimestamp DESC
        """
    )
    fun getQuestsWithParticipantsByStatus(questStatus: String): Flow<List<QuestWithParticipants>>

    @Query("DELETE FROM questentity WHERE questId = :questId")
    suspend fun deleteQuestById(questId: String)

    @Query("DELETE FROM questentity")
    suspend fun deleteAllQuests()

    @Transaction
    suspend fun deleteQuestsByIds(questIds: List<String>) {
        questIds.forEach { questId ->
            deleteQuestById(questId)
        }
    }

    @Query("SELECT COUNT(*) FROM questentity")
    fun getQuestCount(): Flow<Int>
}