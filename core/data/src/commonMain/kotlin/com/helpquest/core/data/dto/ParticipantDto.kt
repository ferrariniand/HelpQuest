package com.helpquest.core.data.dto

import com.helpquest.core.domain.models.Class
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDto(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean = false, //TODO: Create a logic to show or not identity? (maybe in the backend)
    val participantClassId: String = Class.VILLAGER.classId,
    val participantSubClassId: String? = null,
)