package com.helpquest.core.test.service.participant

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.service.participant.ParticipantRepository
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult

class FakeParticipantRepository : ParticipantRepository {

    val participant = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",

        )

    val participant2 = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
    )

    val participant3 = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = "test",
    )

    var fetchLocalParticipantResult: Result<Participant, DataError.Remote> =
        Result.Success(participant)

    var uploadProfilePictureResult: Result<Participant, DataError.Remote> =
        Result.Success(participant)

    var deleteProfilePictureResult: Result<Participant, DataError.Remote> =
        Result.Success(participant)

    override suspend fun fetchLocalParticipant(): Result<Participant, DataError.Remote> {
        return fetchLocalParticipantResult
    }

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        return uploadProfilePictureResult.asEmptyResult()
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return deleteProfilePictureResult.asEmptyResult()
    }
}