package com.helpquest.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val classId: String = "0000",
    val subClassId: String?,
    val showParticipantIdentity: Boolean,
    val isFriend: Boolean,
)
