package com.helpquest.quest.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.helpquest.quest.database.entities.QuestParticipantEntity

@Dao
interface QuestParticipantDao {

    @Upsert
    suspend fun upsertParticipant(participant: QuestParticipantEntity)

    @Upsert
    suspend fun upsertParticipants(participants: List<QuestParticipantEntity>)

    @Query("SELECT * FROM questparticipantentity")
    suspend fun getAllParticipants(): List<QuestParticipantEntity>
}