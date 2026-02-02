package com.helpquest.core.data.mappers

import com.helpquest.core.data.dto.response.ProfilePictureUploadUrlsResponse
import com.helpquest.core.domain.models.ProfilePictureUploadUrls

fun ProfilePictureUploadUrlsResponse.toProfilePictureUploadUrls(): ProfilePictureUploadUrls {
    return ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}