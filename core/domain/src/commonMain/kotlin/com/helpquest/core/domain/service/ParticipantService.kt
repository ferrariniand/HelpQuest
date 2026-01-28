package com.helpquest.core.domain.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

interface ParticipantService {
    suspend fun searchParticipants(
        query: String
    ): Result<List<Participant>, DataError.Remote>

    suspend fun getLocalParticipant(): Result<Participant, DataError.Remote>
}