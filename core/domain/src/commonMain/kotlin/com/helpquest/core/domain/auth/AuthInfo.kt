package com.helpquest.core.domain.auth

import com.helpquest.core.domain.models.User

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)
