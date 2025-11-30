package com.helpquest.quest.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.quest.database.entities.QuestEntity
import com.helpquest.quest.database.entities.QuestWithParticipants
import kotlinx.coroutines.flow.Flow

//TODO just a DRAFT for QuestBoardDao
@Dao
interface QuestBoardDao {

    @Upsert
    suspend fun upsertQuest(quest: QuestEntity)

    @Upsert
    suspend fun upsertQuests(quests: List<QuestEntity>)

    @Query("SELECT * FROM questentity WHERE questId = :id")
    suspend fun getQuestById(id: String): QuestWithParticipants?

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