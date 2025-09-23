package com.helpquest.core.data.networking

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> io.ktor.client.statement.HttpResponse,
    handleResponse: suspend (io.ktor.client.statement.HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    TODO("Not yet implemented")
}