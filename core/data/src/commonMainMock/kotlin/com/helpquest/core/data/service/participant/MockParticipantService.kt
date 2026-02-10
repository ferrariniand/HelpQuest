package com.helpquest.core.data.service.participant

import com.helpquest.core.data.service.MockCoreResponseElements
import com.helpquest.core.domain.service.participant.ParticipantService
import com.helpquest.core.domain.models.ProfilePictureUploadUrls
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.SubClass
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.asEmptyResult

class MockParticipantService(
    val mockResponse: MockCoreResponseElements
) : ParticipantService {

    var savedParticipantList: List<Participant>? = null

    override suspend fun searchParticipants(query: String): Result<List<Participant>, DataError.Remote> {
        if (query.contains("!")) {
            return Result.Failure(DataError.Remote.UNKNOWN)
        }

        val resultList =
            mockResponse.allPossibleParticipants.filter {
                (it.username == query) || it.username.contains(
                    query
                )
            }
        return if (resultList.isEmpty()) {
            Result.Failure(DataError.Remote.NOT_FOUND)
        } else {
            Result.Success(resultList)
        }
    }

    override suspend fun getLocalParticipant(): Result<Participant, DataError.Remote> {
        return Result.Success(mockResponse.participantFull)
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return Result.Success(mockResponse.profilePictureUploadUrls)
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return Result.Success(mockResponse.profilePictureUploadUrls).asEmptyResult()
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return Result.Success(mockResponse.profilePictureUploadUrls).asEmptyResult()
    }


    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return Result.Success(mockResponse.profilePictureUploadUrls).asEmptyResult()
    }
}