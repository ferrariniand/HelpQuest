package com.helpquest.core.test.service.participant

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.ProfilePictureUploadUrls
import com.helpquest.core.domain.service.participant.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.asEmptyResult

class FakeParticipantService : ParticipantService {

    val profilePictureUploadUrls = ProfilePictureUploadUrls(
        uploadUrl = "uploadUrl",
        publicUrl = "publicUrl",
        headers = mapOf("header1" to "value1", "header2" to "value2")
    )

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

    var getProfilePictureUploadUrlResult: Result<ProfilePictureUploadUrls, DataError.Remote> =
        Result.Success(profilePictureUploadUrls)

    var uploadProfilePictureResult: Result<ProfilePictureUploadUrls, DataError.Remote> =
        Result.Success(profilePictureUploadUrls)

    var confirmProfilePictureUploadResult: Result<ProfilePictureUploadUrls, DataError.Remote> =
        Result.Success(profilePictureUploadUrls)

    var deleteProfilePictureResult: Result<ProfilePictureUploadUrls, DataError.Remote> =
        Result.Success(profilePictureUploadUrls)

    override suspend fun searchParticipants(query: String): Result<List<Participant>, DataError.Remote> {
        return searchParticipantResult
    }

    override suspend fun getLocalParticipant(): Result<Participant, DataError.Remote> {
        return getLocalParticipantResult
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return getProfilePictureUploadUrlResult
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return uploadProfilePictureResult.asEmptyResult()
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return confirmProfilePictureUploadResult.asEmptyResult()
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return deleteProfilePictureResult.asEmptyResult()
    }
}