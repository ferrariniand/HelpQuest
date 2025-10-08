package networking

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.data.networking.platformSafeCall
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut
import kotlin.test.Test

class HttpClientExtIosTest {

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
    fun `Network error DarwinHttpRequestException with code NSURLErrorNotConnectedToInternet`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorNotConnectedToInternet
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorNotConnectedToInternet,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorNetworkConnectionLost`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorNetworkConnectionLost
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorNetworkConnectionLost,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorCannotFindHost`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorCannotFindHost
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorCannotFindHost,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorDNSLookupFailed`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorDNSLookupFailed
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorDNSLookupFailed,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorResourceUnavailable`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorResourceUnavailable
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorResourceUnavailable,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorInternationalRoamingOff`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorInternationalRoamingOff
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorInternationalRoamingOff,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorCallIsActive`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorCallIsActive
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorCallIsActive,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorDataNotAllowed`() =
        runBlocking {
            // Given the 'execute' block throws an DarwinHttpRequestException,
            // with error domain NSURLErrorDomain and error code NSURLErrorDataNotAllowed
            // When platformSafeCall is invoked,
            // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
            val error = NSError(
                domain = NSURLErrorDomain,
                code = NSURLErrorDataNotAllowed,
                userInfo = null

            )
            val execute: suspend () -> HttpResponse = {
                throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code NSURLErrorTimedOut`() = runBlocking {
        // Given the 'execute' block throws an DarwinHttpRequestException,
        // with error domain NSURLErrorDomain and error code NSURLErrorTimedOut
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val error = NSError(
            domain = NSURLErrorDomain,
            code = NSURLErrorTimedOut,
            userInfo = null

        )
        val execute: suspend () -> HttpResponse = {
            throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with code unknown`() = runBlocking {
        // Given the 'execute' block throws an DarwinHttpRequestException,
        // with error domain NSURLErrorDomain and error code unknown
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val error = NSError(
            domain = NSURLErrorDomain,
            code = 999L,
            userInfo = null

        )
        val execute: suspend () -> HttpResponse = {
            throw DarwinHttpRequestException(error)
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
    fun `Network error DarwinHttpRequestException with domain null`() = runBlocking {
        // Given the 'execute' block throws an DarwinHttpRequestException,
        // with error domain null and error code NSURLErrorTimedOut
        // When platformSafeCall is invoked,
        // Then it should catch the exception and return Result.Failure(DataError.Remote.NO_INTERNET).
        val error = NSError(
            domain = null,
            code = NSURLErrorTimedOut,
            userInfo = null

        )
        val execute: suspend () -> HttpResponse = {
            throw DarwinHttpRequestException(error)
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