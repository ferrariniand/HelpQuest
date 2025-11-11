package com.helpquest.chat.domain.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result


interface ChatParticipantService {
    suspend fun searchParticipant(
        query: String
    ): Result<List<Participant>, DataError.Remote>
}