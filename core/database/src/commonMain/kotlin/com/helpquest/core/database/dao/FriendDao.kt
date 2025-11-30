package com.helpquest.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.helpquest.core.database.entities.ParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Upsert
    suspend fun upsertFriend(friend: ParticipantEntity)

    @Upsert
    suspend fun upsertFriends(friends: List<ParticipantEntity>)

    @Query("SELECT * FROM participantentity")
    suspend fun getAllFriends(): List<ParticipantEntity>

    @Query("SELECT * FROM participantentity WHERE userId = :userId")
    suspend fun getFriendById(userId: String): ParticipantEntity?

    @Query("DELETE FROM participantentity WHERE userId = :userId")
    suspend fun deleteFriendById(userId: String)

    @Query("DELETE FROM participantentity")
    suspend fun deleteAllFriends()

    @Query("SELECT COUNT(*) FROM participantentity")
    fun getFriendCount(): Flow<Int>

}