package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass

fun ParticipantDto.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        participantClass = Class.entries.find { it.classId == participantClassId },
        participantSubClass = SubClass.entries.find { it.subClassId == participantSubClassId },
    )
}

fun ParticipantEntity.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        participantClass = Class.entries.find { it.classId == classId },
        participantSubClass = SubClass.entries.find { it.subClassId == subClassId },
        showParticipantIdentity = showParticipantIdentity,
        isFriend = isFriend
    )
}

fun Participant.toParticipantEntity(): ParticipantEntity {
    return ParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        classId = participantClass?.classId,
        subClassId = participantSubClass?.subClassId,
        isFriend = isFriend
    )
}