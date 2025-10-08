@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.core.data.networking

import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.request.HttpRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.content.OutgoingContent
import io.ktor.util.Attributes
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.InternalAPI
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

/**
 * A fake HttpResponse for testing purposes.
 * It allows setting the status code, body content, and whether it should fail on body transformation.
 */
data class FakeHttpResponse<out T>(
    override val status: HttpStatusCode,
    private val bodyContent: T? = null, // Store the body content
    private val shouldFailTransformation: Boolean = false,
    override val call: HttpClientCall = mockk<HttpClientCall>(relaxed = true),
    @InternalAPI
    override val rawContent: ByteReadChannel = ByteReadChannel.Empty,
    override val requestTime: GMTDate = mockk<GMTDate>(),
    override val responseTime: GMTDate = mockk<GMTDate>(),
    override val version: HttpProtocolVersion = mockk<HttpProtocolVersion>(),
    override val headers: Headers = Headers.Empty,
    override val coroutineContext: CoroutineContext = UnconfinedTestDispatcher(),

    ) : HttpResponse() {
    @Suppress("UNCHECKED_CAST")
    fun <T> body(): T {
        if (shouldFailTransformation) {
            throw NoTransformationFoundException(
                this,
                from = Any::class,
                to = Any::class
            )
        } else {
            return bodyContent as T
        }
    }
}

data class FakeHttpRequest(
    override val attributes: Attributes = Attributes(false),
    override val call: HttpClientCall = mockk<HttpClientCall>(relaxed = true),
    override val content: OutgoingContent = mockk<OutgoingContent>(relaxed = true),
    override val method: HttpMethod = HttpMethod.Get,
    override val url: Url = Url(""),
    override val headers: Headers = Headers.Empty
) : HttpRequest
