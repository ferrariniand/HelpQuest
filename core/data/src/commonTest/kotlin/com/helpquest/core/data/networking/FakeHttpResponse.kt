@file:OptIn(ExperimentalCoroutinesApi::class, InternalAPI::class)

package com.helpquest.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.engine.HttpClientEngineBase
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.takeFrom
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
    private val shouldThrowException: Boolean = false,
    @InternalAPI
    override val rawContent: ByteReadChannel = ByteReadChannel.Empty,
    override val requestTime: GMTDate = GMTDate(),
    override val responseTime: GMTDate = GMTDate(),
    override val version: HttpProtocolVersion = HttpProtocolVersion.HTTP_2_0,
    override val headers: Headers = Headers.Empty,
    override val coroutineContext: CoroutineContext = UnconfinedTestDispatcher(),

    ) : HttpResponse() {

    val httpResponseData = HttpResponseData(
        statusCode = status,
        requestTime = requestTime,
        headers = headers,
        version = version,
        body = bodyContent ?: Any(),
        callContext = coroutineContext
    )
    private val httpClient: HttpClient = HttpClient(
        FakeHttpClientEngine(
            httpResponseData,
        )
    )

    override val call: HttpClientCall = HttpClientCall(
        httpClient,
        requestData = HttpRequestBuilder().takeFrom(
            FakeHttpRequest(
                call = HttpClientCall(httpClient)
            )
        ).build(),
        responseData = httpResponseData
    )
}

data class FakeHttpRequest(
    override val call: HttpClientCall,
    override val content: OutgoingContent = FakeOutgoingContent(),
    override val attributes: Attributes = Attributes(false),
    override val method: HttpMethod = HttpMethod.Post,
    override val url: Url = Url(""),
    override val headers: Headers = Headers.Empty,
) : HttpRequest

data class FakeOutgoingContent(
    val content: Any = Any()
) : OutgoingContent.NoContent()

@OptIn(InternalAPI::class)
class FakeHttpClientEngine(
    val responseData: HttpResponseData,
) : HttpClientEngineBase("fake-engine") {

    // A configuration block to tell the engine what to do for the next request.
    override var config: HttpClientEngineConfig = HttpClientEngineConfig()

    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        return responseData
    }
}
