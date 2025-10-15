@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.auth.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.auth.presentation.email_verification.EmailVerificationViewModel
import com.helpquest.core.data.FakeAuthService
import com.helpquest.core.domain.auth.AuthService
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class EmailVerificationViewModelTest : KoinTest {

    private val overrideModule = module {
        singleOf(::FakeAuthService) bind AuthService::class

    }
    private val fakeAuthService by inject<FakeAuthService>()

    private lateinit var viewModel: EmailVerificationViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                authPresentationModule,
                overrideModule
            )
        }
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }


    @Test
    fun `email verification success`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        fakeAuthService.verifyEmailResult = Result.Success(Unit)
        viewModel = EmailVerificationViewModel(fakeAuthService, savedStateHandle)

        viewModel.state.test {
//TODO: how to test this?
//            val isVerifyingEmailState = awaitItem()
//            assertThat(isVerifyingEmailState.isVerifying).isTrue()
            val successState = awaitItem()
            assertThat(successState.isVerifying).isFalse()
            assertThat(successState.isVerified).isTrue()
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `email verification error for null token`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to null)
        )
        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.verifyEmailResult = Result.Failure(error)
        viewModel = EmailVerificationViewModel(fakeAuthService, savedStateHandle)

        viewModel.state.test {
//TODO: how to test this?
//            val isVerifyingEmailState = awaitItem()
//            assertThat(isVerifyingEmailState.isVerifying).isTrue()
            val successState = awaitItem()
            assertThat(successState.isVerifying).isFalse()
            assertThat(successState.isVerified).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `email verification error`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.verifyEmailResult = Result.Failure(error)
        viewModel = EmailVerificationViewModel(fakeAuthService, savedStateHandle)

        viewModel.state.test {
//TODO: how to test this?
//            val isVerifyingEmailState = awaitItem()
//            assertThat(isVerifyingEmailState.isVerifying).isTrue()
            val successState = awaitItem()
            assertThat(successState.isVerifying).isFalse()
            assertThat(successState.isVerified).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }
}