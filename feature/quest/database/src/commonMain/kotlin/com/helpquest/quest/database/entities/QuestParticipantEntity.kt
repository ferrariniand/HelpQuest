package com.helpquest.quest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean,
    val classId: String?,
    val subClassId: String?,
)
