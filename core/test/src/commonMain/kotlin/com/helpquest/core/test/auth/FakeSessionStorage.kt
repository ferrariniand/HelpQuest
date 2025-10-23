package com.helpquest.core.test.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.auth.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSessionStorage : SessionStorage {

    val fakeAuthInfo = AuthInfo(
        accessToken = "accessToken",
        refreshToken = "refreshToken",
        user = User(
            id = "id",
            email = "email",
            username = "username",
            hasVerifiedEmail = false
        )
    )

    var resultAuthInfo: AuthInfo? = null

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return flowOf(resultAuthInfo)
    }

    override suspend fun set(info: AuthInfo?) {
        resultAuthInfo = info
    }
}