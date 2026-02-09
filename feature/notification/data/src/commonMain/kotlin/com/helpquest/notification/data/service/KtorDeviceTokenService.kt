package com.helpquest.notification.data.service


import com.helpquest.core.data.networking.hqDelete
import com.helpquest.core.data.networking.hqPost
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.notification.data.dto.request.RegisterDeviceTokenRequest
import com.helpquest.notification.domain.service.DeviceTokenService
import io.ktor.client.HttpClient

class KtorDeviceTokenService(
    private val httpClient: HttpClient
) : DeviceTokenService {

    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.hqPost(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.hqDelete(
            route = "/notification/$token"
        )
    }
}