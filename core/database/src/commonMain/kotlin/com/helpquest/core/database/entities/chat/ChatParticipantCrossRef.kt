package com.helpquest.core.database.entities.chat

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.helpquest.core.database.entities.ParticipantEntity

@Entity(
    primaryKeys = ["chatId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
        ForeignKey(
            entity = ParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.Companion.CASCADE
        ),
    ],
    indices = [
        Index(value = ["chatId"]),
        Index(value = ["userId"]),
    ]
)
data class ChatParticipantCrossRef(
    val chatId: String,
    val userId: String,
    val isActive: Boolean
)