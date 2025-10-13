package com.helpquest.core.data

import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

class FakeAuthService : AuthService {
    var registerResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var resendVerificationResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return registerResult
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return resendVerificationResult
    }
}