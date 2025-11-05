package com.helpquest.core.data.mappers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.data.dto.UserDto
import com.helpquest.core.domain.models.User
import kotlin.test.Test

class UserMapperTest {

    @Test
    fun `User to UserDto`() {
        val input = User(
            id = "id",
            email = "email",
            username = "username",
            hasVerifiedEmail = true
        )

        val result = UserDto(
            id = "id",
            email = "email",
            username = "username",
            hasVerifiedEmail = true
        )

        assertThat(input.toUserDto()).isEqualTo(result)
    }

    @Test
    fun `UserDto to User`() {
        val input = UserDto(
            id = "id",
            email = "email",
            username = "username",
            hasVerifiedEmail = true
        )

        val result = User(
            id = "id",
            email = "email",
            username = "username",
            hasVerifiedEmail = true
        )

        assertThat(input.toUser()).isEqualTo(result)
    }

}
