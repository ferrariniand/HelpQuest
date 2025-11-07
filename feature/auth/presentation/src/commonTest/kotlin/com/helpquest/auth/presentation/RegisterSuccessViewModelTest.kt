@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.auth.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.auth.presentation.register_success.RegisterSuccessAction
import com.helpquest.auth.presentation.register_success.RegisterSuccessEvent
import com.helpquest.auth.presentation.register_success.RegisterSuccessViewModel
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.di.coreTestModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


class RegisterSuccessViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()

    private lateinit var viewModel: RegisterSuccessViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                coreTestModule,
                authPresentationModule,
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
    fun `resendVerification success`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("email" to "email@test.com")
        )
        viewModel = RegisterSuccessViewModel(fakeAuthService, savedStateHandle)

        viewModel.state.test {
            fakeAuthService.resendVerificationResult = Result.Success(Unit)
            viewModel.events.test {
                viewModel.onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                val successState = viewModel.state.first()
                val collectedEvent = awaitItem()
                assertThat(successState.isResendingVerificationEmail).isFalse()
                assertThat(successState.resendVerificationError).isNull()
                assertThat(collectedEvent).isEqualTo(RegisterSuccessEvent.ResendVerificationEmailSuccess)
            }
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `resendVerification error`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("email" to "email@test.com")
        )
        viewModel = RegisterSuccessViewModel(fakeAuthService, savedStateHandle)

        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.resendVerificationResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.resendVerificationError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)
            cancelAndConsumeRemainingEvents()
        }
    }
}