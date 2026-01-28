package com.helpquest.core.domain.models

data class Participant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?,
    val showParticipantIdentity: Boolean = false,
    val participantClass: Class = Class.VILLAGER,
    val participantSubClass: SubClass? = null,
    val isFriend: Boolean = false
) {
    val initials: String
        get() = username.take(2).uppercase()
}