package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.domain.models.Participant

fun ParticipantDto.toParticipant(): Participant {
    return Participant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        classImageUrl = classImageUrl,
    )
}