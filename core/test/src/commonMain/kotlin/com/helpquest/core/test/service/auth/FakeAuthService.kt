package com.helpquest.core.test.service.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.User
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result

class FakeAuthService : AuthService {

    var user = User(
        id = "id",
        email = "email",
        username = "username",
        hasVerifiedEmail = true,
        profilePictureUrl = null,
        classId = Class.VILLAGER.classId,
    )

    var authInfo = AuthInfo(
        accessToken = "accessToken",
        refreshToken = "refreshToken",
        user = user
    )
    var loginResult: Result<AuthInfo, DataError.Remote> = Result.Success(authInfo)

    var logoutResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

    var registerResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var resendVerificationResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var verifyEmailResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var forgotPasswordResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var resetPasswordResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var changePasswordResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

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

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return forgotPasswordResult
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return resetPasswordResult
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return changePasswordResult
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return logoutResult
    }
}