package com.helpquest.chat.data.service

import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

class FakeChatParticipantService : ChatParticipantService {

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

    override suspend fun searchParticipant(query: String): Result<List<Participant>, DataError.Remote> {
        return searchParticipantResult
    }
}