package com.helpquest.core.domain.models

data class User(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null
) {
    val initials: String
        get() = username.take(2).uppercase()
}