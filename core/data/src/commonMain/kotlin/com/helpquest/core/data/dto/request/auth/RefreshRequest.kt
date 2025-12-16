package com.helpquest.core.data.dto.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String
)