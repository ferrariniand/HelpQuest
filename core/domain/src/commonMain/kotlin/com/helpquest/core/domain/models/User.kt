package com.helpquest.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null,
    val classId: String = Class.VILLAGER.classId,
    val subClassId: String? = null,
) {
    val initials: String
        get() = username.take(2).uppercase()
}