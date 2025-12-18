package com.helpquest.core.database.entities.chat

import androidx.room.Embedded
import androidx.room.Relation
import com.helpquest.core.database.entities.ParticipantEntity

data class MessageWithSenderEntity(
    @Embedded
    val message: ChatMessageEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId"
    )
    val sender: ParticipantEntity
)