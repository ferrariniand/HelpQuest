package com.helpquest.quest.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.helpquest.quest.database.db_view.LastActivityView

data class QuestWithParticipants(
    @Embedded
    val quest: QuestEntity,
    @Relation(
        parentColumn = "questId",
        entityColumn = "userId",
        associateBy = Junction(QuestParticipantCrossRef::class)
    )
    val participants: List<QuestParticipantEntity>,
    @Relation(
        parentColumn = "questId",
        entityColumn = "questId",
        entity = LastActivityView::class
    )
    val lastActivity: LastActivityView?
)

data class QuestInfoEntity(
    @Embedded
    val quest: QuestEntity,
    @Relation(
        parentColumn = "questId",
        entityColumn = "userId",
        associateBy = Junction(QuestParticipantCrossRef::class)
    )
    val participants: List<QuestParticipantEntity>,
    @Relation(
        parentColumn = "questId",
        entityColumn = "questId",
        entity = QuestActivityEntity::class
    )
    val activityWithActors: List<ActivityWithActor>
)



