package com.helpquest.core.data.dto.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)