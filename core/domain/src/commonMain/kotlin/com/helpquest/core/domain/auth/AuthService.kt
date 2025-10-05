package com.helpquest.core.domain.auth

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.EmptyResult

interface AuthService {
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote>
}