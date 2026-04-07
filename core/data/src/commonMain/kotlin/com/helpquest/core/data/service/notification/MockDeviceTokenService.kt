package com.helpquest.core.data.service.notification

import com.helpquest.core.domain.service.notification.DeviceTokenService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.core.domain.util.Result


class MockDeviceTokenService() : DeviceTokenService {

    var registerTokenResult: EmptyResult<DataError.Remote> = Result.Success(Unit)
    var unregisterTokenResult: EmptyResult<DataError.Remote> = Result.Success(Unit)

    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return registerTokenResult
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return unregisterTokenResult
    }
}