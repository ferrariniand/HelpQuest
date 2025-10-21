package com.helpquest.core.data

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSessionStorage : SessionStorage {

    var resultAuthInfo: AuthInfo? = null

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return flowOf(resultAuthInfo)
    }

    override suspend fun set(info: AuthInfo?) {
        resultAuthInfo = info
    }
}