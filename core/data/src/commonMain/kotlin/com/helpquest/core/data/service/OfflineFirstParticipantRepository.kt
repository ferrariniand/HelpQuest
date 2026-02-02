package com.helpquest.core.data.service

import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.service.ParticipantRepository
import com.helpquest.core.domain.service.ParticipantService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.onSuccess
import kotlinx.coroutines.flow.first

class OfflineFirstParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val participantService: ParticipantService
) : ParticipantRepository {
    override suspend fun fetchLocalParticipant(): Result<Participant, DataError.Remote> {
        return participantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.setAuthInfo(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl,
                            classId = participant.participantClass.classId,
                            subClassId = participant.participantSubClass?.subClassId
                        )
                    )
                )
            }
    }

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        val result = participantService.getProfilePictureUploadUrl(mimeType)

        if (result is Result.Failure) {
            return result
        }

        val uploadUrls = (result as Result.Success).data
        val uploadResult = participantService.uploadProfilePicture(
            uploadUrl = uploadUrls.uploadUrl,
            imageBytes = imageBytes,
            headers = uploadUrls.headers
        )

        if (uploadResult is Result.Failure) {
            return uploadResult
        }

        return participantService
            .confirmProfilePictureUpload(uploadUrls.publicUrl)
            .onSuccess {
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.setAuthInfo(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            profilePictureUrl = uploadUrls.publicUrl
                        )
                    )
                )
            }
    }
}