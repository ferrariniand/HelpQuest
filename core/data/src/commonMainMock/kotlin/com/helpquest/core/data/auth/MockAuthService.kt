package com.helpquest.core.data.auth

import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.auth.User
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.data.mappers.toAuthInfoDto

class MockAuthService() : AuthService {

    var savedAuthInfo: AuthInfo? = null
    var savedPassword: String? = null
    private val mockUser = User(
        id = "id",
        email = "email",
        username = "username",
        hasVerifiedEmail = true
    )
    private val mockAuthInfo = AuthInfo(
        accessToken = "accessToken",
        refreshToken = "refreshToken",
        user = mockUser
    )

    private var loginResult: Result<AuthInfo, DataError.Remote> =
        Result.Success(savedAuthInfo ?: mockAuthInfo)
    private var refreshTokenResult: Result<AuthInfoDto, DataError.Remote> =
        Result.Success(savedAuthInfo?.toAuthInfoDto() ?: mockAuthInfo.toAuthInfoDto())

    private var registerResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    private var resendVerificationEmailResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    private var verifyEmailResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    private var forgotPasswordResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    private var resetPasswordResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

    fun setLoginResult(
        error: DataError.Remote? = null
    ) {
        loginResult = when {

            error != null -> {
                Result.Failure(error)
            }

            else -> {
                savedAuthInfo?.let {
                    Result.Success(it)
                } ?: Result.Failure(DataError.Remote.UNKNOWN)
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        savedAuthInfo = mockAuthInfo.copy(
            user = mockUser.copy(
                email = email
            )
        )
        savedPassword = password
        return loginResult
    }

    fun setRegisterResult(
        error: DataError.Remote? = null
    ) {
        registerResult = if (error != null) {
            Result.Failure(error)
        } else {
            Result.Success(Unit)
        }
    }


    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        savedAuthInfo = mockAuthInfo.copy(
            user = mockUser.copy(
                email = email,
                username = username
            )
        )
        savedPassword = password
        return registerResult
    }

    fun setResendVerificationEmailResult(
        error: DataError.Remote? = null
    ) {
        resendVerificationEmailResult = if (error != null) {
            Result.Failure(error)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return resendVerificationEmailResult
    }

    fun setVerifyEmailResult(
        error: DataError.Remote? = null
    ) {
        verifyEmailResult = if (error != null) {
            Result.Failure(error)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        savedAuthInfo = mockAuthInfo.copy(
            accessToken = token,
            refreshToken = token
        )
        return verifyEmailResult
    }

    fun setForgotPasswordResult(
        error: DataError.Remote? = null
    ) {
        forgotPasswordResult = if (error != null) {
            Result.Failure(error)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return forgotPasswordResult
    }

    fun setResetPasswordResult(
        error: DataError.Remote? = null
    ) {
        resetPasswordResult = if (error != null) {
            Result.Failure(error)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        savedPassword = newPassword
        return resetPasswordResult
    }

    fun setRefreshTokenResult(
        error: DataError.Remote? = null
    ) {
        refreshTokenResult = when {

            error != null -> {
                Result.Failure(error)
            }

            else -> {
                savedAuthInfo?.let {
                    Result.Success(it.toAuthInfoDto())
                } ?: Result.Failure(DataError.Remote.UNKNOWN)
            }
        }
    }

    fun refreshToken(refreshToken: String): Result<AuthInfoDto, DataError.Remote> {
        savedAuthInfo = mockAuthInfo.copy(
            accessToken = refreshToken,
            refreshToken = refreshToken
        )
        return refreshTokenResult
    }

}