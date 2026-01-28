package com.helpquest.core.domain.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

interface ParticipantRepository {
    suspend fun fetchLocalParticipant(): Result<Participant, DataError.Remote>
}