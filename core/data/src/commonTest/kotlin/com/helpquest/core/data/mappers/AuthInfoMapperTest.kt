package com.helpquest.core.data.mappers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.data.dto.UserDto
import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.models.User
import kotlin.test.Test

class AuthInfoMapperTest {

    @Test
    fun `AuthInfo to AuthInfoDto`() {
        val input = AuthInfo(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            user = User(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true
            )
        )

        val result = AuthInfoDto(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            user = UserDto(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true
            )
        )

        assertThat(input.toAuthInfoDto()).isEqualTo(result)
    }

    @Test
    fun `AuthInfoDto to AuthInfo`() {
        val input = AuthInfoDto(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            user = UserDto(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true
            )
        )

        val result = AuthInfo(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            user = User(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true
            )
        )

        assertThat(input.toAuthInfo()).isEqualTo(result)
    }

}
