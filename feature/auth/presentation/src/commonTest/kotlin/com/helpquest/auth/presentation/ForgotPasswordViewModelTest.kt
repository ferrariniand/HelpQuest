@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordAction
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordState
import com.helpquest.auth.presentation.forgot_password.ForgotPasswordViewModel
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


class ForgotPasswordViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()

    private lateinit var viewModel: ForgotPasswordViewModel

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
    fun `all the conditions to submit forgot password request are valid`() = runBlocking {

        viewModel = ForgotPasswordViewModel(fakeAuthService)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.canSubmit).isFalse()
        }
        val stateWithPopulatedEmail = ForgotPasswordState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            isLoading = false,
            emailError = null,
            submitError = null
        )
        viewModel = ForgotPasswordViewModel(
            fakeAuthService,
            initialState = stateWithPopulatedEmail
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canSubmit).isTrue()
        }
    }

    @Test
    fun `not all the conditions to submit forgot password request are valid because is still loading`() =
        runBlocking {
            viewModel = ForgotPasswordViewModel(fakeAuthService)

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.canSubmit).isFalse()
            }
            val stateIsLoading = ForgotPasswordState(
                emailTextState = TextFieldState(
                    initialText = "test@test.com"
                ),
                isLoading = true,
                emailError = null,
                submitError = null
            )
            viewModel = ForgotPasswordViewModel(
                fakeAuthService,
                initialState = stateIsLoading
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canSubmit).isFalse()
            }
        }

    @Test
    fun `not all the conditions to submit forgot password request are valid because email not valid`() =
        runBlocking {
            viewModel = ForgotPasswordViewModel(fakeAuthService)
            val stateNotValidEmail = ForgotPasswordState(
                emailTextState = TextFieldState(
                    initialText = "notValidEmail"
                ),
                isLoading = false,
                emailError = null,
                submitError = null
            )
            viewModel = ForgotPasswordViewModel(
                fakeAuthService,
                initialState = stateNotValidEmail
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canSubmit).isFalse()
            }
        }

    @Test
    fun `submit forgot password request success`() = runBlocking {
        val stateWithPopulatedEmail = ForgotPasswordState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            isLoading = false,
            emailError = null,
            submitError = null
        )
        fakeAuthService.forgotPasswordResult = Result.Success(Unit)
        viewModel = ForgotPasswordViewModel(
            fakeAuthService,
            initialState = stateWithPopulatedEmail
        )

        viewModel.state.test {
            viewModel.onAction(ForgotPasswordAction.OnSubmitClick)
//TODO: how to test this?
//            val isLoadingState = awaitItem()
//            assertThat(isLoadingState.isLoading).isTrue()
            val resultState = viewModel.state.first()
            assertThat(resultState.isLoading).isFalse()
            assertThat(resultState.submitError).isNull()
            assertThat(resultState.isEmailSentSuccessfully).isTrue()
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `submit forgot password request error`() = runBlocking {
        val validForgotPasswordState = ForgotPasswordState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            isLoading = false,
            emailError = null,
            submitError = null
        )
        viewModel = ForgotPasswordViewModel(fakeAuthService, validForgotPasswordState)

        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.forgotPasswordResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(ForgotPasswordAction.OnSubmitClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.submitError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)
            assertThat(resultState.isEmailSentSuccessfully).isFalse()


            cancelAndConsumeRemainingEvents()
        }
    }
}