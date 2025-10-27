@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.auth.presentation.reset_password.ResetPasswordAction
import com.helpquest.auth.presentation.reset_password.ResetPasswordState
import com.helpquest.auth.presentation.reset_password.ResetPasswordViewModel
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.di.coreTestModule
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import helpquest.feature.auth.presentation.generated.resources.error_same_password
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


class ResetPasswordViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()

    private lateinit var viewModel: ResetPasswordViewModel

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
    fun `all the conditions to submit reset password request are valid`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        viewModel = ResetPasswordViewModel(fakeAuthService, savedStateHandle)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.canSubmit).isFalse()
        }
        val stateWithValidPassword = ResetPasswordState(
            passwordTextState = TextFieldState(
                initialText = "Password123"
            ),
            passwordError = null,
            confirmPasswordTextState = TextFieldState(
                initialText = "Password123"
            ),
            confirmPasswordError = null,
            isLoading = false,
            submitError = null
        )
        viewModel = ResetPasswordViewModel(
            fakeAuthService,
            savedStateHandle,
            initialState = stateWithValidPassword
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canSubmit).isTrue()
        }
    }

    @Test
    fun `not all the conditions to submit reset password request are valid because is still loading`() =
        runBlocking {
            val savedStateHandle = SavedStateHandle(
                initialState = mapOf("token" to "token123")
            )
            viewModel = ResetPasswordViewModel(fakeAuthService, savedStateHandle)

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.canSubmit).isFalse()
            }
            val stateIsLoading = ResetPasswordState(
                passwordTextState = TextFieldState(
                    initialText = "Password123"
                ),
                passwordError = null,
                confirmPasswordTextState = TextFieldState(
                    initialText = "Password123"
                ),
                confirmPasswordError = null,
                isLoading = true,
                submitError = null
            )
            viewModel = ResetPasswordViewModel(
                fakeAuthService,
                savedStateHandle,
                initialState = stateIsLoading
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canSubmit).isFalse()
            }
        }

    @Test
    fun `not all the conditions to submit reset password request are valid because password not valid`() =
        runBlocking {
            val savedStateHandle = SavedStateHandle(
                initialState = mapOf("token" to "token123")
            )
            viewModel = ResetPasswordViewModel(fakeAuthService, savedStateHandle)
            val stateNotValidPassword = ResetPasswordState(
                passwordTextState = TextFieldState(
                    initialText = "Pas"
                ),
                passwordError = null,
                confirmPasswordTextState = TextFieldState(
                    initialText = "Pas"
                ),
                confirmPasswordError = null,
                isLoading = false,
                submitError = null
            )
            viewModel = ResetPasswordViewModel(
                fakeAuthService,
                savedStateHandle,
                initialState = stateNotValidPassword
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canSubmit).isFalse()
            }
        }

    @Test
    fun `not all the conditions to submit reset password request are valid because confirm password is different`() =
        runBlocking {
            val savedStateHandle = SavedStateHandle(
                initialState = mapOf("token" to "token123")
            )
            viewModel = ResetPasswordViewModel(fakeAuthService, savedStateHandle)
            val stateDifferentConfirmPassword = ResetPasswordState(
                passwordTextState = TextFieldState(
                    initialText = "Password123"
                ),
                passwordError = null,
                confirmPasswordTextState = TextFieldState(
                    initialText = "Pas"
                ),
                confirmPasswordError = null,
                isLoading = false,
                submitError = null
            )
            viewModel = ResetPasswordViewModel(
                fakeAuthService,
                savedStateHandle,
                initialState = stateDifferentConfirmPassword
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canSubmit).isFalse()
            }
        }

    @Test
    fun `submit reset password request success`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        val stateWithValidPassword = ResetPasswordState(
            passwordTextState = TextFieldState(
                initialText = "Password123"
            ),
            passwordError = null,
            confirmPasswordTextState = TextFieldState(
                initialText = "Password123"
            ),
            confirmPasswordError = null,
            isLoading = false,
            submitError = null
        )
        fakeAuthService.resetPasswordResult = Result.Success(Unit)
        viewModel = ResetPasswordViewModel(
            fakeAuthService,
            savedStateHandle,
            initialState = stateWithValidPassword
        )

        viewModel.state.test {
            viewModel.onAction(ResetPasswordAction.OnSubmitClick)
//TODO: how to test this?
//            val isLoadingState = awaitItem()
//            assertThat(isLoadingState.isLoading).isTrue()
            val resultState = viewModel.state.first()
            assertThat(resultState.isLoading).isFalse()
            assertThat(resultState.submitError).isNull()
            assertThat(resultState.isResetSuccessful).isTrue()
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `submit reset password request UNAUTHORIZED error`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        val stateWithValidPassword = ResetPasswordState(
            passwordTextState = TextFieldState(
                initialText = "Password123"
            ),
            passwordError = null,
            confirmPasswordTextState = TextFieldState(
                initialText = "Password123"
            ),
            confirmPasswordError = null,
            isLoading = false,
            submitError = null
        )
        viewModel = ResetPasswordViewModel(
            fakeAuthService,
            savedStateHandle,
            initialState = stateWithValidPassword
        )

        val error = DataError.Remote.UNAUTHORIZED
        fakeAuthService.resetPasswordResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(ResetPasswordAction.OnSubmitClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.submitError
            assertThat(errorResult).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)
            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_reset_password_token_invalid)
            assertThat(resultState.isLoading).isFalse()
            assertThat(resultState.isResetSuccessful).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `submit reset password request CONFLICT error`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        val stateWithValidPassword = ResetPasswordState(
            passwordTextState = TextFieldState(
                initialText = "Password123"
            ),
            passwordError = null,
            confirmPasswordTextState = TextFieldState(
                initialText = "Password123"
            ),
            confirmPasswordError = null,
            isLoading = false,
            submitError = null
        )
        viewModel = ResetPasswordViewModel(
            fakeAuthService,
            savedStateHandle,
            initialState = stateWithValidPassword
        )

        val error = DataError.Remote.CONFLICT
        fakeAuthService.resetPasswordResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(ResetPasswordAction.OnSubmitClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.submitError
            assertThat(errorResult).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)
            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_same_password)
            assertThat(resultState.isLoading).isFalse()
            assertThat(resultState.isResetSuccessful).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `submit reset password request generic error`() = runBlocking {
        val savedStateHandle = SavedStateHandle(
            initialState = mapOf("token" to "token123")
        )
        val stateWithValidPassword = ResetPasswordState(
            passwordTextState = TextFieldState(
                initialText = "Password123"
            ),
            passwordError = null,
            confirmPasswordTextState = TextFieldState(
                initialText = "Password123"
            ),
            confirmPasswordError = null,
            isLoading = false,
            submitError = null
        )
        viewModel = ResetPasswordViewModel(
            fakeAuthService,
            savedStateHandle,
            initialState = stateWithValidPassword
        )

        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.resetPasswordResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(ResetPasswordAction.OnSubmitClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.submitError
            assertThat(errorResult).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)
            assertThat(resultState.isLoading).isFalse()
            assertThat(resultState.isResetSuccessful).isFalse()


            cancelAndConsumeRemainingEvents()
        }
    }
}