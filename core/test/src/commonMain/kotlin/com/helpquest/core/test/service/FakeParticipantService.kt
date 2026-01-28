package com.helpquest.core.test.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

class FakeParticipantService : ParticipantService {

    val participant = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",

        )

    val participant3 = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = "test",

        )

    var searchParticipantResult: Result<List<Participant>, DataError.Remote> =
        Result.Success(listOf(participant3))

    var getLocalParticipantResult: Result<Participant, DataError.Remote> =
        Result.Success(participant)

    override suspend fun searchParticipants(query: String): Result<List<Participant>, DataError.Remote> {
        return searchParticipantResult
    }

    override suspend fun getLocalParticipant(): Result<Participant, DataError.Remote> {
        return getLocalParticipantResult
    }
}