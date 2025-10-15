package com.helpquest.core.data.auth

import com.helpquest.core.data.dto.request.EmailRequest
import com.helpquest.core.data.dto.request.RegisterRequestDto
import com.helpquest.core.data.networking.get
import com.helpquest.core.data.networking.post
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequestDto(
                email = email,
                username = username,
                password = password
            )
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(email),
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }
}