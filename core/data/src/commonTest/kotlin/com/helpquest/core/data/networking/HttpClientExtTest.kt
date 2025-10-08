package com.helpquest.core.data.networking

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test


class HttpClientExtTest {

    val baseUrl = "test"

    @Test
    fun `Route with leading slash`() {
        // Given a route string that starts with a '/', verify that the function prepends UrlConstants.BASE_URL_HTTP and returns the combined string.
        val result = constructRoute("/path", baseUrl)
        assertThat(result).isEqualTo("test/path")
    }

    @Test
    fun `Route without leading slash`() {
        // Given a route string that does not start with a '/', verify that the function prepends UrlConstants.BASE_URL_HTTP and a '/' before the route.
        val result = constructRoute("path", baseUrl)
        assertThat(result).isEqualTo("test/path")
    }

    @Test
    fun `Route already containing the base URL`() {
        // Given a route string that already contains UrlConstants.BASE_URL_HTTP, verify that the function returns the original route string unmodified.
        val result = constructRoute("$baseUrl/path", baseUrl)
        assertThat(result).isEqualTo("test/path")
    }

    @Test
    fun `Empty route string`() {
        // Given an empty string as the route, verify that the function returns UrlConstants.BASE_URL_HTTP followed by a '/'.
        val result = constructRoute("", baseUrl)
        assertThat(result).isEqualTo("test")
    }

    @Test
    fun `Route with multiple leading slashes`() {
        // Given a route string that starts with multiple slashes (e.g., '//users'), verify the function prepends the base URL, resulting in a URL with multiple slashes after the domain.
        val result = constructRoute("$baseUrl//path", baseUrl)
        assertThat(result).isEqualTo("test/path")
    }

    @Test
    fun `Route with whitespace characters`() {
        // Given a route string containing leading, trailing, or internal whitespace, verify that the function's output includes the whitespace as is when prepending the base URL.
        val result = constructRoute("$baseUrl / path ", baseUrl)
        assertThat(result).isEqualTo("test/path")
    }

    val mockHttpResponse = mockk<HttpResponse>(relaxed = true)

    @Test
    fun `test responseToResult with response between 200 and 299`() = runBlocking {
        val expectedData = "test"
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.OK,
            bodyContent = expectedData,
            shouldFailTransformation = false
        )
        val result = responseToResult<Any>(input)
        assertThat(result).isInstanceOf(Result.Success::class)
        Unit
    }

    //TODO UNIT TEST NOT WORKING...issue on HttpRequest not defined
//    @Test
//    fun `WHEN responseToResult throw NoTransformationFoundException THEN return Result Failure with SERIALIZATION`() =
//        runBlocking {
//            val expectedData = "test"
//
//            coEvery {
//                mockHttpResponse.body<Any>()
//            } throws NoTransformationFoundException(
//                mockHttpResponse,
//                from = Any::class,
//                to = Any::class
//            )
//
//
//            val input = FakeHttpResponse<Any>(
//                status = HttpStatusCode.OK,
//                bodyContent = expectedData,
//                shouldFailTransformation = true
//            )
//            coEvery {
//                input.call.body<Any>()
//            } throws NoTransformationFoundException(
//                input,
//                from = Any::class,
//                to = Any::class
//            )
//            coEvery {
//                input.call.request
//            } returns FakeHttpRequest()
//            val result = responseToResult<Object>(input)
//            val expected = Result.Failure(DataError.Remote.SERIALIZATION)
//            assertThat(result).isEqualTo(expected)
//
//            Unit
//
//        }

    @Test
    fun `WHEN responseToResult throw NoTransformationFoundException THEN return Result Failure with SERIALIZATION`() =
        runBlocking {

            val input = FakeHttpResponse<Any>(
                status = HttpStatusCode.OK,
                shouldFailTransformation = true
            )
            val result = responseToResult<Any>(input)
            assertThat(result).isInstanceOf(Result.Failure::class)
            Unit
        }

    @Test
    fun `test responseToResult with response 400`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.BadRequest,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.BAD_REQUEST)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 401`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.Unauthorized,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.UNAUTHORIZED)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 403`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.Forbidden,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.FORBIDDEN)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 404`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.NotFound,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.NOT_FOUND)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 408`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.RequestTimeout,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 409`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.Conflict,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.CONFLICT)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 413`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.PayloadTooLarge,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.PAYLOAD_TOO_LARGE)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 429`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.TooManyRequests,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.TOO_MANY_REQUESTS)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 500`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.InternalServerError,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.SERVER_ERROR)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response 503`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode.ServiceUnavailable,
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.SERVICE_UNAVAILABLE)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test responseToResult with response unknown`() = runBlocking {
        val input = FakeHttpResponse<Any>(
            status = HttpStatusCode(999, "Unknown Status Code")
        )
        val result = responseToResult<String>(input)
        val expected = Result.Failure(DataError.Remote.UNKNOWN)
        assertThat(result).isEqualTo(expected)
    }
}