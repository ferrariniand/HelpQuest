package com.helpquest.core.data.auth

import io.ktor.client.HttpClient
import io.mockk.mockk

class KtorAuthServiceAndroidTest {

    val mockHttpClient = mockk<HttpClient>(relaxed = true)

//TODO infinite run for the tests. Maybe configuration problem related to mockk
//    @Test
//    fun `Successful registration with valid inputs`() = runBlocking {
//        // Test the happy path where a user provides a valid, unique email, a valid username, and a strong password.
//        // The test should mock the HttpClient to return a successful (e.g., 200 OK or 201 Created) response and verify the function returns a success result.
//        val email = "test@example.com"
//        val username = "testuser"
//        val password = "StrongPassword123!"
//        val expectedResult = Result.Success(Unit)
//
//        val authService = KtorAuthService(mockHttpClient)
//
//        coEvery {
//            mockHttpClient.post<Any, Any>(any(), any())
//        } returns expectedResult
//        assertThat(
//            authService.register(email, username, password)
//        ).isEqualTo(expectedResult)
//
//        val routeSlot = slot<String>()
//        val bodySlot = slot<RegisterRequestDto>()
//
//        coVerify {
//            mockHttpClient.post<RegisterRequestDto, Unit>(
//                capture(routeSlot),
//                capture(bodySlot)
//            )
//        }
//
//        assertThat(routeSlot.captured).isEqualTo("/auth/register")
//        assertThat(bodySlot.captured.email).isEqualTo(email)
//        assertThat(bodySlot.captured.username).isEqualTo(username)
//        assertThat(bodySlot.captured.password).isEqualTo(password)
//    }
//
//    @Test
//    fun `Error during registration`() = runBlocking {
//        // Test how the function handles an error.
//        // The mock HttpClient should be configured to return a Error, and the function should return a DataError.Remote.ServerError.
//        val email = "test@example.com"
//        val username = "testuser"
//        val password = "StrongPassword123!"
//        val expectedResult = Result.Failure(DataError.Remote.SERVER_ERROR)
//
//        val authService = KtorAuthService(mockHttpClient)
//
//        coEvery {
//            mockHttpClient.post<Any, Any>(any(), any())
//        } returns expectedResult
//        assertThat(
//            authService.register(email, username, password)
//        ).isEqualTo(expectedResult)
//
//        val routeSlot = slot<String>()
//        val bodySlot = slot<RegisterRequestDto>()
//
//        coVerify {
//            mockHttpClient.post<RegisterRequestDto, Unit>(
//                capture(routeSlot),
//                capture(bodySlot)
//            )
//        }
//
//        assertThat(routeSlot.captured).isEqualTo("/auth/register")
//        assertThat(bodySlot.captured.email).isEqualTo(email)
//        assertThat(bodySlot.captured.username).isEqualTo(username)
//        assertThat(bodySlot.captured.password).isEqualTo(password)
//    }


}