package com.helpquest.core.presentation.mappers

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.presentation.modelsUi.ParticipantUi

fun Participant.toParticipantUi(): ParticipantUi {
    return ParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl,
        showParticipantIdentity = showParticipantIdentity,
        classImageUrl = classImageUrl
    )
}