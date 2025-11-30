package com.helpquest.quest.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestEntity(
    @PrimaryKey
    val questId: String,
    val questTitle: String,
    val questDescription: String,
    val questCreatorId: String,
    val createdTimestamp: Long,
    val questCategory: String?,
    val questStatus: String?,
)
