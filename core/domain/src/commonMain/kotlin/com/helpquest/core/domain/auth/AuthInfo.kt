package com.helpquest.core.domain.auth

import com.helpquest.core.domain.models.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
