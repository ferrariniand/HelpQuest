package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.dao.ParticipantDao
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.database.entities.quest.QuestActivityEntity
import com.helpquest.core.database.entities.quest.QuestEntity
import com.helpquest.core.database.entities.quest.QuestInfoEntity
import com.helpquest.core.database.entities.quest.QuestParticipantCrossRef
import com.helpquest.core.database.entities.quest.QuestWithParticipants
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestLogDao {

    @Upsert
    suspend fun upsertQuest(quest: QuestEntity)

    @Upsert
    suspend fun upsertQuests(quests: List<QuestEntity>)

    @Query("SELECT * FROM questentity WHERE questId = :id")
    @Transaction
    suspend fun getQuestById(id: String): QuestWithParticipants?

    @Query("SELECT * FROM questentity ORDER BY createdTimestamp ASC")
    @Transaction
    fun getQuestsWithParticipants(): Flow<List<QuestWithParticipants>>

    @Query("SELECT questId FROM questentity")
    suspend fun getAllQuestIds(): List<String>

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

    @Query(
        """
        SELECT p.*
        FROM participantentity p
        JOIN questparticipantcrossref qpcr ON p.userId = qpcr.userId
        WHERE qpcr.questId = :questId AND qpcr.isActive = true
        ORDER BY p.username
    """
    )
    fun getActiveParticipantsByQuestId(questId: String): Flow<List<ParticipantEntity>>

    @Query(
        "SELECT * FROM questentity WHERE questId = :questId"
    )
    @Transaction
    fun getQuestInfoById(questId: String): Flow<QuestInfoEntity?>

    @Transaction
    suspend fun upsertQuestWithParticipantsAndCrossRefs(
        quest: QuestEntity,
        participants: List<ParticipantEntity>,
        participantDao: ParticipantDao,
        crossRefDao: QuestParticipantsCrossRefDao
    ) {
        upsertQuest(quest)
        participantDao.upsertParticipants(participants)

        val crossRefs = participants.map {
            QuestParticipantCrossRef(
                questId = quest.questId,
                userId = it.userId,
                isActive = true
            )
        }
        crossRefDao.upsertCrossRefs(crossRefs)
        crossRefDao.syncQuestParticipants(quest.questId, participants)
    }

    @Transaction
    suspend fun upsertQuestsWithParticipantsAndCrossRefs(
        quests: List<QuestWithParticipants>,
        participantDao: ParticipantDao,
        crossRefDao: QuestParticipantsCrossRefDao,
        activityDao: QuestActivityDao
    ) {
        upsertQuests(quests.map { it.quest })

        val serverQuestIds = quests.map { it.quest.questId }
        val localQuestIds = getAllQuestIds()
        val staleQuestIds = localQuestIds - serverQuestIds

        quests.forEach { quest ->
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
                        endTimestamp = endTimestamp
                    )
                )

            }
        }

        val allParticipants = quests.flatMap { it.participants }
        participantDao.upsertParticipants(allParticipants)

        val allCrossRefs = quests.flatMap { questWithParticipants ->
            questWithParticipants.participants.map { participant ->
                QuestParticipantCrossRef(
                    questId = questWithParticipants.quest.questId,
                    userId = participant.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(allCrossRefs)

        quests.forEach { quest ->
            crossRefDao.syncQuestParticipants(
                questId = quest.quest.questId,
                participants = quest.participants
            )
        }

        deleteQuestsByIds(staleQuestIds)

    }

    @Query(
        """
        UPDATE questentity
        SET lastUpdateTimestamp = CASE
                WHEN :timestamp > lastUpdateTimestamp THEN :timestamp
                ELSE lastUpdateTimestamp
                END
        WHERE questId = :questId
            """
    )
    suspend fun updateLastUpdateTimestamp(questId: String, timestamp: Long)
}