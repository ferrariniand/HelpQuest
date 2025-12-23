package com.helpquest.core.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingCoreWebSocketType {
    PROFILE_PICTURE_UPDATED
}

@Serializable
sealed interface IncomingCoreWebSocketDto {

    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?,
        val type: IncomingCoreWebSocketType = IncomingCoreWebSocketType.PROFILE_PICTURE_UPDATED
    ) : IncomingCoreWebSocketDto
}