package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.entities.quest.QuestEntity
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
    suspend fun upsertQuestsAndSyncIfNecessary(
        serverQuests: List<QuestEntity>,
        pageSize: Int,
        shouldSync: Boolean = false
    ) {
        val localQuests = getQuestsLimited(
            limit = pageSize
        ).first()

        upsertQuests(serverQuests)

        if (!shouldSync) {
            return
        }

        val serverIds = serverQuests.map { it.questId }.toSet()

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

    @Query("SELECT * FROM questentity ORDER BY createdTimestamp DESC")
    fun getQuestsWithParticipants(): Flow<List<QuestWithParticipants>>

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