package com.helpquest.core.data.mappers


import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.domain.auth.AuthInfo

fun AuthInfoDto.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toUser()
    )
}

fun AuthInfo.toAuthInfoDto(): AuthInfoDto {
    return AuthInfoDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toUserDto()
    )
}