package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.ParticipantDto
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