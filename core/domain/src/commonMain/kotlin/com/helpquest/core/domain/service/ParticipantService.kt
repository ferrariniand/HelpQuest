package com.helpquest.core.domain.service

import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.ProfilePictureUploadUrls
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

interface ParticipantService {
    suspend fun searchParticipants(
        query: String
    ): Result<List<Participant>, DataError.Remote>

    suspend fun getLocalParticipant(): Result<Participant, DataError.Remote>

    suspend fun getProfilePictureUploadUrl(
        mimeType: String
    ): Result<ProfilePictureUploadUrls, DataError.Remote>

    suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote>

    suspend fun confirmProfilePictureUpload(
        publicUrl: String
    ): EmptyResult<DataError.Remote>
}