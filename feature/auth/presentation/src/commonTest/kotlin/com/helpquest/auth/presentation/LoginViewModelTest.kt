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
import com.helpquest.auth.presentation.login.LoginAction
import com.helpquest.auth.presentation.login.LoginState
import com.helpquest.auth.presentation.login.LoginViewModel
import com.helpquest.core.domain.auth.User
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.test.auth.FakeAuthService
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.di.coreTestModule
import helpquest.feature.auth.presentation.generated.resources.Res
import helpquest.feature.auth.presentation.generated.resources.error_email_not_verified
import helpquest.feature.auth.presentation.generated.resources.error_invalid_credentials
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


class LoginViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()
    private val fakeSessionStorage by inject<FakeSessionStorage>()

    private lateinit var viewModel: LoginViewModel

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
    fun `all the conditions to login are valid`() = runBlocking {
        val stateValid = LoginState(
            emailTextState = TextFieldState(
                initialText = "email@test.eu"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            initialState = stateValid
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canLogin).isTrue()
        }
    }

    @Test
    fun `not all the conditions to login are valid because is still loggingIn`() = runBlocking {
        val stateIsLoggingIn = LoginState(
            emailTextState = TextFieldState(
                initialText = "email@test.eu"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = true,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            initialState = stateIsLoggingIn
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canLogin).isFalse()
        }
    }

    @Test
    fun `not all the conditions to login are valid because email not valid`() = runBlocking {
        val stateNotValidEmail = LoginState(
            emailTextState = TextFieldState(
                initialText = "notValidEmail"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            initialState = stateNotValidEmail
        )
        viewModel.state.test {
            val newState = awaitItem()
            assertThat(newState.canLogin).isFalse()
        }
    }

    @Test
    fun `login success`() = runBlocking {
        fakeSessionStorage.setAuthInfo(null)
        val validLoginState = LoginState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            validLoginState
        )
        val result = fakeSessionStorage.fakeAuthInfo.copy(
            user = User(
                id = "id",
                email = "email",
                username = "username",
                hasVerifiedEmail = true,
            )
        )

        viewModel.state.test {
            fakeAuthService.loginResult = Result.Success(result)
            viewModel.onAction(LoginAction.OnLoginClick)
//TODO: how to test this?
//            val isRegisteringState = awaitItem()
//            assertThat(isRegisteringState.isRegistering).isTrue()
            val successState = awaitItem()
            assertThat(successState.canLogin).isTrue()
            assertThat(successState.isLoggingIn).isFalse()
            assertThat(successState.error).isNull()
            assertThat(fakeSessionStorage.resultAuthInfoFlow.value).isEqualTo(result)
        }

    }

    @Test
    fun `login error INVALID CREDENTIALS`() = runBlocking {
        fakeSessionStorage.setAuthInfo(null)
        val validLoginState = LoginState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            validLoginState
        )

        val error = DataError.Remote.UNAUTHORIZED
        fakeAuthService.loginResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.error
            assertThat(resultState.canLogin).isTrue()
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_invalid_credentials)
            assertThat(fakeSessionStorage.resultAuthInfoFlow.value).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `login error EMAIL NOT VERIFIED`() = runBlocking {
        fakeSessionStorage.setAuthInfo(null)
        val validLoginState = LoginState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            validLoginState
        )

        val error = DataError.Remote.FORBIDDEN
        fakeAuthService.loginResult = Result.Failure(error)


        viewModel.state.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.error
            assertThat(resultState.canLogin).isTrue()
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_email_not_verified)
            assertThat(fakeSessionStorage.resultAuthInfoFlow.value).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `login error generic`() = runBlocking {
        fakeSessionStorage.setAuthInfo(null)
        val validLoginState = LoginState(
            emailTextState = TextFieldState(
                initialText = "test@test.com"
            ),
            passwordTextState = TextFieldState(
                initialText = "tesT12345"
            ),
            isLoggingIn = false,
            error = null
        )
        viewModel = LoginViewModel(
            fakeAuthService,
            fakeSessionStorage,
            validLoginState
        )

        val error = DataError.Remote.SERVER_ERROR
        fakeAuthService.loginResult = Result.Failure(error)

        viewModel.state.test {
            viewModel.onAction(LoginAction.OnLoginClick)
            val resultState = viewModel.state.first()
            val errorResult = resultState.error
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isNotEqualTo(Res.string.error_email_not_verified)
            assertThat(
                errorResult.id
            ).isNotEqualTo(Res.string.error_invalid_credentials)
            assertThat(fakeSessionStorage.resultAuthInfoFlow.value).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }
}