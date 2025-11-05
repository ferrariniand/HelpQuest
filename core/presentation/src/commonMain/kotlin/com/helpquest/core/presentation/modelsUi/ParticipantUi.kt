package com.helpquest.core.presentation.modelsUi

data class ParticipantUi(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null,
    val showParticipantIdentity: Boolean = false,
    val classImageUrl: String? = null,
)