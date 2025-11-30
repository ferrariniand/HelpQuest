package com.helpquest.chat.data.mappers

import com.helpquest.chat.database.entities.ChatParticipantEntity
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass

fun ChatParticipantEntity.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        participantClass = Class.entries.find { it.classId == classId },
        participantSubClass = SubClass.entries.find { it.subClassId == subClassId },
    )
}

fun Participant.toChatParticipantEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        classId = participantClass?.classId,
        subClassId = participantSubClass?.subClassId,
    )
}