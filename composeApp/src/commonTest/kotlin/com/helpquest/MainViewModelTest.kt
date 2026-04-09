@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.di.coreTestModule
import com.helpquest.core.test.service.notification.FakeDeviceTokenService
import com.helpquest.di.appModule
import com.helpquest.notification.domain.service.PushNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

class MainViewModelTest : KoinTest {

    private val fakeSessionStorage by inject<FakeSessionStorage>()
    private val fakePushNotificationService by inject<FakePushNotificationService>()
    private val fakeDeviceTokenService by inject<FakeDeviceTokenService>()

    val overrideModule = module {
        singleOf(::FakePushNotificationService) bind PushNotificationService::class
    }
    private lateinit var viewModel: MainViewModel
    private lateinit var coroutineScope: CoroutineScope

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                coreTestModule,
                overrideModule,
                appModule,
            )
        }
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coroutineScope = CoroutineScope(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `Authenticated user state update`() = runBlocking {
        // Test that when sessionStorage.observeAuthInfo() emits a non-null AuthInfo, the state is updated to isCheckingAuth = false and isLoggedIn = true.
        fakeSessionStorage.resultAuthInfoFlow.value = fakeSessionStorage.fakeAuthInfo
        viewModel =
            MainViewModel(fakeSessionStorage, fakePushNotificationService, fakeDeviceTokenService)

        viewModel.state.test {
            val authenticatedState = awaitItem()
            assertThat(authenticatedState.isCheckingAuth).isFalse()
            assertThat(authenticatedState.isLoggedIn).isTrue()
        }
    }

    @Test
    fun `Unauthenticated user state update`() = runBlocking {
        // Test that when sessionStorage.observeAuthInfo() emits null, the state is updated to isCheckingAuth = false and isLoggedIn = false.
        fakeSessionStorage.resultAuthInfoFlow.value = null
        viewModel =
            MainViewModel(fakeSessionStorage, fakePushNotificationService, fakeDeviceTokenService)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isCheckingAuth).isFalse()
            assertThat(initialState.isLoggedIn).isFalse()
        }
    }


    @Test
    fun `After user valid authentication, authentication expired and user state update to unauthenticated`() =
        runBlocking {
            // Test that sessionStorage.observeAuthInfo() emits a non-null AuthInfo, the state is updated to isCheckingAuth = false and isLoggedIn = true.
            // Then sessionStorage.observeAuthInfo() emits a null AuthInfo, the state is updated to isCheckingAuth = false and isLoggedIn = false.
            fakeSessionStorage.resultAuthInfoFlow.value = fakeSessionStorage.fakeAuthInfo
            viewModel = MainViewModel(
                fakeSessionStorage,
                fakePushNotificationService,
                fakeDeviceTokenService
            )

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.isCheckingAuth).isFalse()
                assertThat(initialState.isLoggedIn).isTrue()
                fakeSessionStorage.resultAuthInfoFlow.value = null
                val resultState = viewModel.state.first { state ->
                    state != initialState
                }
                assertThat(resultState.isCheckingAuth).isFalse()
                assertThat(resultState.isLoggedIn).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }
}