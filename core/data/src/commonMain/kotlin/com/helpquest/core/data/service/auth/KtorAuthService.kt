package com.helpquest.core.data.service.auth

import com.helpquest.core.data.dto.AuthInfoDto
import com.helpquest.core.data.dto.request.auth.ChangePasswordRequest
import com.helpquest.core.data.dto.request.auth.EmailRequest
import com.helpquest.core.data.dto.request.auth.LoginRequest
import com.helpquest.core.data.dto.request.auth.RefreshRequest
import com.helpquest.core.data.dto.request.auth.RegisterRequest
import com.helpquest.core.data.dto.request.auth.ResetPasswordRequest
import com.helpquest.core.data.mappers.toAuthInfo
import com.helpquest.core.data.networking.hqGet
import com.helpquest.core.data.networking.hqPost
import com.helpquest.core.domain.auth.AuthInfo
import com.helpquest.core.domain.service.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result
import com.helpquest.core.domain.util.map
import com.helpquest.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.hqPost<LoginRequest, AuthInfoDto>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authInfoDto ->
            authInfoDto.toAuthInfo()
        }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.hqPost(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.hqPost(
            route = "/auth/resend-verification",
            body = EmailRequest(email),
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.hqGet(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.hqPost<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email),
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.hqPost(
            route = "/auth/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            ),
        )
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.hqPost(
            route = "/auth/change-password",
            body = ChangePasswordRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return httpClient.hqPost<RefreshRequest, Unit>(
            route = "/auth/logout",
            body = RefreshRequest(refreshToken)
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
    }
}