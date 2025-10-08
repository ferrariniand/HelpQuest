package com.helpquest.core.data.networking

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException

class HttpClientExtAndroidTest {

    val mockHttpResponse = mockk<HttpResponse>(relaxed = true)


    @Test
    fun `Successful call with successful response handling`() = runBlocking {
        // Given the 'execute' block returns a successful HttpResponse,
        // and the 'handleResponse' block successfully processes it into a Result.Success,
        // When platformSafeCall is invoked,
        // Then it should return the Result.Success object from 'handleResponse'.
        val execute: suspend () -> HttpResponse = {
            mockHttpResponse
        }
        val expected = Result.Success("test")
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            expected
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Successful call with failed response handling`() = runBlocking {
        // Given the 'execute' block returns a successful HttpResponse,
        // but the 'handleResponse' block processes it into a Result.Failure,
        // When platformSafeCall is invoked,
        // Then it should return the Result.Failure object from 'handleResponse'.
        val execute: suspend () -> HttpResponse = {
            mockHttpResponse
        }
        val expected = Result.Failure(DataError.Remote.SERVER_ERROR)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            expected
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Network error  UnknownHostException`() = runBlocking {
        // Given the 'execute' block throws an UnknownHostException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val execute: suspend () -> HttpResponse = {
            throw UnknownHostException()
        }
        val expected = Result.Failure(DataError.Remote.NO_INTERNET)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Network error  UnresolvedAddressException`() = runBlocking {
        // Given the 'execute' block throws an UnresolvedAddressException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val execute: suspend () -> HttpResponse = {
            throw UnresolvedAddressException()
        }
        val expected = Result.Failure(DataError.Remote.NO_INTERNET)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Network error  ConnectException`() = runBlocking {
        // Given the 'execute' block throws a ConnectException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val execute: suspend () -> HttpResponse = {
            throw ConnectException()
        }
        val expected = Result.Failure(DataError.Remote.NO_INTERNET)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Timeout error  SocketTimeoutException`() = runBlocking {
        // Given the 'execute' block throws a SocketTimeoutException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.REQUEST_TIMEOUT).
        val execute: suspend () -> HttpResponse = {
            throw SocketTimeoutException()
        }
        val expected = Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Timeout error  HttpRequestTimeoutException`() = runBlocking {
        // Given the 'execute' block throws an HttpRequestTimeoutException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.REQUEST_TIMEOUT).
        val execute: suspend () -> HttpResponse = {
            throw HttpRequestTimeoutException(
                url = "test",
                timeoutMillis = 5_000L,
                cause = Exception()
            )
        }
        val expected = Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Serialization error during request execution`() = runBlocking {
        // Given the 'execute' block throws a SerializationException (e.g., while serializing the request body),
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.SERIALIZATION).
        val execute: suspend () -> HttpResponse = {
            throw SerializationException()
        }
        val expected = Result.Failure(DataError.Remote.SERIALIZATION)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Serialization error during response handling`() = runBlocking {
        // Given the 'execute' block succeeds but the 'handleResponse' block throws a SerializationException,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.SERIALIZATION).
        val execute: suspend () -> HttpResponse = {
            mockHttpResponse
        }
        val expected = Result.Failure(DataError.Remote.SERIALIZATION)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            throw SerializationException()
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Generic exception during request execution`() = runBlocking {
        // Given the 'execute' block throws a generic Exception (e.g., IllegalStateException),
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.UNKNOWN).
        val execute: suspend () -> HttpResponse = {
            throw Exception()
        }
        val expected = Result.Failure(DataError.Remote.UNKNOWN)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            Result.Success("test")
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `Generic exception during response handling`() = runBlocking {
        // Given the 'execute' block succeeds but the 'handleResponse' block throws a generic Exception,
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.UNKNOWN).
        val execute: suspend () -> HttpResponse = {
            mockHttpResponse
        }
        val expected = Result.Failure(DataError.Remote.UNKNOWN)
        val handleResponse: suspend (HttpResponse) -> Result<String, DataError.Remote> = {
            throw Exception()
        }
        val result = platformSafeCall(
            execute = execute,
            handleResponse = handleResponse
        )
        assertThat(result).isEqualTo(expected)
    }

}