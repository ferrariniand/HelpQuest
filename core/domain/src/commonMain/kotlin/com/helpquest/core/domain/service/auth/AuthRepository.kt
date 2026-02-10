package com.helpquest.core.domain.service.auth

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote>
}