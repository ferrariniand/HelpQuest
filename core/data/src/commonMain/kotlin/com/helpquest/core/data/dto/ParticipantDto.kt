package com.helpquest.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean = false, //TODO: Create a logic to show or not identity? (maybe in the backend)
    val classImageUrl: String? = null, //TODO: Create a logic to get Class Image or do it in the backend?
)