package com.helpquest.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val username: String,
    val password: String
)
