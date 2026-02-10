package com.helpquest.core.test.service.auth

import com.helpquest.core.domain.service.auth.AuthRepository
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

class FakeAuthRepository : AuthRepository {

    var logoutResult: EmptyResult<DataError.Remote> =
        Result.Success(Unit)

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return logoutResult
    }
}