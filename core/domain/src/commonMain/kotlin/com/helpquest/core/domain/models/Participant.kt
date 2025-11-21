package com.helpquest.core.domain.models

data class Participant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean = false,
    val participantClass: Class? = null,
    val participantSubClass: SubClass? = null,
) {
    val initials: String
        get() = username.take(2).uppercase()
}