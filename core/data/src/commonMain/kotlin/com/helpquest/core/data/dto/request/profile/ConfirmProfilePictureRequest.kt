package com.helpquest.core.data.dto.request.profile

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmProfilePictureRequest(
    val publicUrl: String
)
