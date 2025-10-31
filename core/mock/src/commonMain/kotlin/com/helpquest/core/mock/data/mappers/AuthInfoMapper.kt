package com.helpquest.core.mock.data.mappers


import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.User
import com.helpquest.core.mock.data.dto.AuthInfoDto
import com.helpquest.core.mock.data.dto.UserDto

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