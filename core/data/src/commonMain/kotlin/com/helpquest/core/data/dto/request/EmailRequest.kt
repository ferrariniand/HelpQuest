package com.helpquest.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)
