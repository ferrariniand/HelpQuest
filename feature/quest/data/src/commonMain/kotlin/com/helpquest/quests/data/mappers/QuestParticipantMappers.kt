package com.helpquest.quests.data.mappers

import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass
import com.helpquest.quest.database.entities.QuestParticipantEntity

fun QuestParticipantEntity.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        participantClass = Class.entries.find { it.classId == classId },
        participantSubClass = SubClass.entries.find { it.subClassId == subClassId },
    )
}

fun Participant.toQuestParticipantEntity(): QuestParticipantEntity {
    return QuestParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        classId = participantClass?.classId,
        subClassId = participantSubClass?.subClassId,
    )
}