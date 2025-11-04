package com.helpquest.core.presentation.modelsUi

data class ParticipantUi(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null,
    val showUserIdentity: Boolean = false, //TODO: Create a logic to show or not identity
    val classImageUrl: String? = null,
)