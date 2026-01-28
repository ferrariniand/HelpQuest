package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.database.entities.ParticipantEntity
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.ClassUtils

fun ParticipantDto.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        participantClass = ClassUtils.findClassById(participantClassId),
        participantSubClass = ClassUtils.findSubClassById(participantSubClassId),
    )
}

fun ParticipantEntity.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        participantClass = ClassUtils.findClassById(classId),
        participantSubClass = ClassUtils.findSubClassById(subClassId),
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
        classId = participantClass.classId,
        subClassId = participantSubClass?.subClassId,
        isFriend = isFriend
    )
}