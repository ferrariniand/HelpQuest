package com.helpquest.core.domain.models

data class Participant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean = false,
    val classImageUrl: String? = null,
) {
    val initials: String
        get() = username.take(2).uppercase()
}