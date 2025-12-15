package com.helpquest.core.data.networking

import com.helpquest.core.domain.util.ConnectionState

actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        TODO("Not yet implemented")
    }

    actual fun transformException(exception: Throwable): Throwable {
        TODO("Not yet implemented")
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        TODO("Not yet implemented")
    }
}