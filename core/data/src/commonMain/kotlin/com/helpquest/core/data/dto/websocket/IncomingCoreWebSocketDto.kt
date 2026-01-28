package com.helpquest.core.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingCoreWebSocketType {
    PROFILE_PICTURE_UPDATED,
    CLASS_UPDATED,
    SUBCLASS_UPDATED,
}

@Serializable
sealed interface IncomingCoreWebSocketDto {

    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?,
        val type: IncomingCoreWebSocketType = IncomingCoreWebSocketType.PROFILE_PICTURE_UPDATED
    ) : IncomingCoreWebSocketDto

    @Serializable
    data class ClassUpdated(
        val userId: String,
        val newClassId: String,
        val type: IncomingCoreWebSocketType = IncomingCoreWebSocketType.CLASS_UPDATED
    ) : IncomingCoreWebSocketDto

    @Serializable
    data class SubClassUpdated(
        val userId: String,
        val newSubClassId: String?,
        val type: IncomingCoreWebSocketType = IncomingCoreWebSocketType.SUBCLASS_UPDATED
    ) : IncomingCoreWebSocketDto
}