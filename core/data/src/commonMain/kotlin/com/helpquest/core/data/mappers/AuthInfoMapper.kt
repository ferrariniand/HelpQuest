package com.helpquest.core.data.mappers


import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.data.dto.UserDto
import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.User

fun AuthInfoDto.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toUser()
    )
}

fun UserDto.toUser(): User {
    return User(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}


fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}

fun AuthInfo.toAuthInfoDto(): AuthInfoDto {
    return AuthInfoDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toUserDto()
    )
}