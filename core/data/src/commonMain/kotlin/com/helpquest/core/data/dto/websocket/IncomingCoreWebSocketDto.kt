package com.helpquest.core.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingCoreWebSocketType {
    PROFILE_PICTURE_UPDATED
}

@Serializable
sealed class IncomingCoreWebSocketDto(
    val type: IncomingCoreWebSocketType
) {

    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?
    ) : IncomingCoreWebSocketDto(IncomingCoreWebSocketType.PROFILE_PICTURE_UPDATED)
}