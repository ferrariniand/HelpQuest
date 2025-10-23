@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.auth.presentation

import androidx.compose.foundation.text.input.TextFieldState
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.helpquest.auth.presentation.di.authPresentationModule
import com.helpquest.auth.presentation.register.RegisterAction
import com.helpquest.auth.presentation.register.RegisterState
import com.helpquest.auth.presentation.register.RegisterViewModel
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.di.coreTestModule
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_account_exists
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


class RegisterViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()

    private lateinit var viewModel: RegisterViewModel

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
    fun `all the conditions to register are valid`() = runBlocking {

        viewModel = RegisterViewModel(fakeAuthService)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.canRegister).isFalse()
        }
        val stateWithPopulatedTextFields = RegisterState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(
            fakeAuthService,
            initialState = stateWithPopulatedTextFields
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canRegister).isTrue()
        }
    }

    @Test
    fun `not all the conditions to register are valid because is still registering`() =
        runBlocking {
            viewModel = RegisterViewModel(fakeAuthService)

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.canRegister).isFalse()
            }
            val stateIsRegistering = RegisterState(
                emailTextState = TextFieldState(
                    initialText = "test@test.com"
                ),
                passwordTextState = TextFieldState(
                    initialText = "tesT12345"
                ),
                usernameTextState = TextFieldState(
                    initialText = "test"
                ),
                isRegistering = true,
                emailError = null,
                usernameError = null,
                passwordError = null,
                registrationError = null
            )
            viewModel = RegisterViewModel(
                fakeAuthService,
                initialState = stateIsRegistering
            )
            viewModel.state.test {
                val newState = awaitItem()
                assertThat(newState.canRegister).isFalse()
            }
        }

    @Test
    fun `not all the conditions to register are valid because email not valid`() = runBlocking {
        viewModel = RegisterViewModel(fakeAuthService)
        val stateNotValidEmail = RegisterState(
            emailTextState = TextFieldState(
                initialText = "notValidEmail"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(
            fakeAuthService,
            initialState = stateNotValidEmail
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canRegister).isFalse()
        }
    }

    @Test
    fun `not all the conditions to register are valid because password not valid`() = runBlocking {
        viewModel = RegisterViewModel(fakeAuthService)
        val stateNotValidPassword = RegisterState(
            emailTextState = TextFieldState(
                initialText = "notValidEmail"
            ),
            passwordTextState = TextFieldState(
                initialText = "tes"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(
            fakeAuthService,
            initialState = stateNotValidPassword
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canRegister).isFalse()
        }
    }

    @Test
    fun `not all the conditions to register are valid because username not valid`() = runBlocking {
        viewModel = RegisterViewModel(fakeAuthService)

        val stateNotValidUsername = RegisterState(
            emailTextState = TextFieldState(
                initialText = "notValidEmail"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "t"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(
            fakeAuthService,
            initialState = stateNotValidUsername
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canRegister).isFalse()
        }
    }

    @Test
    fun `register success`() = runBlocking {
        val validRegistrationState = RegisterState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(fakeAuthService, validRegistrationState)

        viewModel.state.test {
            fakeAuthService.registerResult = Result.Success(Unit)
            viewModel.onAction(RegisterAction.OnRegisterClick)
//TODO: how to test this?
//            val isRegisteringState = awaitItem()
//            assertThat(isRegisteringState.isRegistering).isTrue()
            val successState = awaitItem()
            assertThat(successState.isRegistering).isFalse()
            assertThat(successState.registrationError).isNull()
        }

    }

    @Test
    fun `register error CONFLICT`() = runBlocking {
        val validRegistrationState = RegisterState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
        )
        viewModel = RegisterViewModel(fakeAuthService, validRegistrationState)

        val error = DataError.Remote.CONFLICT
        fakeAuthService.registerResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(RegisterAction.OnRegisterClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.registrationError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_account_exists)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `register error generic`() = runBlocking {
        val validRegistrationState = RegisterState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            usernameTextState = TextFieldState(
                initialText = "test"
            ),
            isRegistering = false,
            emailError = null,
            usernameError = null,
            passwordError = null,
            registrationError = null
        )
        viewModel = RegisterViewModel(fakeAuthService, validRegistrationState)

        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.registerResult = Result.Failure(error)

        viewModel.state.test {
            viewModel.onAction(RegisterAction.OnRegisterClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.registrationError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isNotEqualTo(Res.string.error_account_exists)
            cancelAndConsumeRemainingEvents()
        }
    }
}