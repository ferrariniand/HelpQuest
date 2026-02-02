package com.helpquest.core.domain.models

data class ProfilePictureUploadUrls(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
