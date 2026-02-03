package com.helpquest.core.data.service

import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.data.dto.request.profile.ConfirmProfilePictureRequest
import com.helpquest.core.data.dto.response.ProfilePictureUploadUrlsResponse
import com.helpquest.core.data.mappers.toParticipant
import com.helpquest.core.data.mappers.toProfilePictureUploadUrls
import com.helpquest.core.data.networking.hqDelete
import com.helpquest.core.data.networking.hqGet
import com.helpquest.core.data.networking.hqPost
import com.helpquest.core.data.networking.put
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.ProfilePictureUploadUrls
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import io.ktor.client.HttpClient

class KtorParticipantService(
    private val httpClient: HttpClient
) : ParticipantService {

    override suspend fun searchParticipants(query: String): Result<List<Participant>, DataError.Remote> {
        return httpClient.hqGet<List<ParticipantDto>>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { list ->
            list.map {
                it.toParticipant()
            }
        }
    }

    override suspend fun getLocalParticipant(): Result<Participant, DataError.Remote> {
        return httpClient.hqGet<ParticipantDto>(
            route = "/local-participant",
        ).map { it.toParticipant() }
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return httpClient.hqPost<Unit, ProfilePictureUploadUrlsResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map { it.toProfilePictureUploadUrls() }
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        //we don't use the custom put because the url is not to our url, but to supabase
        return httpClient.put<ByteArray, Unit>(
            url = uploadUrl,
            body = imageBytes,
            headers = headers
        )
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.hqPost<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl)
        )
    }


    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.hqDelete(
            route = "/participants/profile-picture"
        )
    }
}