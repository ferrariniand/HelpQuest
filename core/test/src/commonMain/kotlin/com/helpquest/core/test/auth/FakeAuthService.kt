package com.helpquest.core.test.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.User
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

class FakeAuthService : AuthService {
    var loginResult: Result<AuthInfo, DataError.Remote> = Result.Success(
        AuthInfo(
            accessToken = "accessToken",
            refreshToken = "refreshToken",
            user = User(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true
            )
        )
    )

    var registerResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var resendVerificationResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var verifyEmailResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return loginResult
    }

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

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return verifyEmailResult
    }
}