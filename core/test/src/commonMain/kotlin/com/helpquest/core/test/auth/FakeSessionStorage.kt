package com.helpquest.core.test.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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

    val resultAuthInfoFlow = MutableStateFlow<AuthInfo?>(null)


    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return resultAuthInfoFlow
    }

    override suspend fun setAuthInfo(info: AuthInfo?) {
        resultAuthInfoFlow.value = info
    }
}