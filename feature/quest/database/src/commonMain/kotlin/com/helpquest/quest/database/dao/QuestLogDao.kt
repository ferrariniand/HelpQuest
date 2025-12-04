package com.helpquest.quest.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.quest.database.entities.QuestActivityEntity
import com.helpquest.quest.database.entities.QuestEntity
import com.helpquest.quest.database.entities.QuestInfoEntity
import com.helpquest.quest.database.entities.QuestParticipantCrossRef
import com.helpquest.quest.database.entities.QuestParticipantEntity
import com.helpquest.quest.database.entities.QuestWithParticipants
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
        FROM questparticipantentity p
        JOIN questparticipantcrossref qpcr ON p.userId = qpcr.userId
        WHERE qpcr.questId = :questId AND qpcr.isActive = true
        ORDER BY p.username
    """
    )
    fun getActiveParticipantsByQuestId(questId: String): Flow<List<QuestParticipantEntity>>

    @Query(
        "SELECT * FROM questentity WHERE questId = :questId"
    )
    @Transaction
    fun getQuestInfoById(questId: String): Flow<QuestInfoEntity?>

    @Transaction
    suspend fun upsertQuestWithParticipantsAndCrossRefs(
        quest: QuestEntity,
        participants: List<QuestParticipantEntity>,
        participantDao: QuestParticipantDao,
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
        participantDao: QuestParticipantDao,
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
                        actorId = actorId,
                        content = content,
                        activityStatus = activityStatus,
                        startTimestamp = startTimestamp,
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
}