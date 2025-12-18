package com.helpquest.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.helpquest.core.database.entities.ParticipantEntity

@Dao
interface ParticipantDao {

    @Upsert
    suspend fun upsertParticipant(participant: ParticipantEntity)

    @Upsert
    suspend fun upsertParticipants(participants: List<ParticipantEntity>)

    @Query(
        """
        UPDATE participantentity
        SET profilePictureUrl = :newUrl
        WHERE userId = :userId
    """
    )
    suspend fun updateProfilePictureUrl(userId: String, newUrl: String?)

    @Query("SELECT * FROM participantentity WHERE userId = :userId")
    suspend fun getParticipantById(userId: String): ParticipantEntity?

    @Query("SELECT * FROM participantentity")
    suspend fun getAllParticipants(): List<ParticipantEntity>
}