package com.helpquest.core.database.entities.quest

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.helpquest.core.database.db_view.LastActivityView
import com.helpquest.core.database.entities.ParticipantEntity

data class QuestWithParticipants(
    @Embedded
    val quest: QuestEntity,
    @Relation(
        parentColumn = "questId",
        entityColumn = "userId",
        associateBy = Junction(QuestParticipantCrossRef::class)
    )
    val participants: List<ParticipantEntity>,
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
    val participants: List<ParticipantEntity>,
    @Relation(
        parentColumn = "questId",
        entityColumn = "questId",
        entity = QuestActivityEntity::class
    )
    val activitiesWithCreators: List<ActivityWithCreatorEntity>
)



