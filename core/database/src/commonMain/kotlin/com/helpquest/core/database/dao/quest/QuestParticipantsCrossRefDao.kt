package com.helpquest.core.database.dao.quest

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.database.entities.quest.QuestParticipantCrossRef

@Dao
interface QuestParticipantsCrossRefDao {

    @Upsert
    suspend fun upsertCrossRefs(crossRefs: List<QuestParticipantCrossRef>)

    @Query("SELECT userId FROM questparticipantcrossref WHERE questId = :questId AND isActive = 1 ")
    suspend fun getActiveParticipantIdsByQuest(questId: String): List<String>

    @Query("SELECT userId FROM questparticipantcrossref WHERE questId = :questId")
    suspend fun getAllParticipantIdsByQuest(questId: String): List<String>

    @Query(
        """
        UPDATE questparticipantcrossref
        SET isActive = 0
        WHERE questId = :questId AND userId IN (:userIds)
    """
    )
    suspend fun markParticipantsAsInactive(questId: String, userIds: List<String>)

    @Query(
        """
        UPDATE questparticipantcrossref
        SET isActive = 1
        WHERE questId = :questId AND userId IN (:userIds)
    """
    )
    suspend fun reactivateParticipants(questId: String, userIds: List<String>)

    @Transaction
    suspend fun syncQuestParticipants(
        questId: String,
        participants: List<ParticipantEntity>
    ) {
        if (participants.isEmpty()) {
            return
        }

        val serverParticipantIds = participants.map { it.userId }.toSet()
        val allLocalParticipantIds = getAllParticipantIdsByQuest(questId).toSet()
        val activeLocalParticipantIds = getActiveParticipantIdsByQuest(questId).toSet()
        val inactiveLocalParticipantIds = allLocalParticipantIds - activeLocalParticipantIds

        val participantsToReactivate = serverParticipantIds.intersect(inactiveLocalParticipantIds)
        val participantsToDeactivate = activeLocalParticipantIds - serverParticipantIds

        reactivateParticipants(questId, participantsToReactivate.toList())
        markParticipantsAsInactive(questId, participantsToDeactivate.toList())

        val completelyNewParticipantIds = serverParticipantIds - allLocalParticipantIds
        val newCrossRefs = completelyNewParticipantIds.map { userId ->
            QuestParticipantCrossRef(
                questId = questId,
                userId = userId,
                isActive = true
            )
        }
        upsertCrossRefs(newCrossRefs)
    }
}