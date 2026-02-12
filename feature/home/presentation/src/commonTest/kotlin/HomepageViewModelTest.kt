@file:OptIn(ExperimentalCoroutinesApi::class)

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.di.coreTestModule
import com.helpquest.core.test.service.auth.FakeAuthRepository
import com.helpquest.core.test.service.participant.FakeParticipantRepository
import com.helpquest.home.presentation.HomepageAction
import com.helpquest.home.presentation.HomepageViewModel
import com.helpquest.home.presentation.di.homepagePresentationModule
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

class HomepageViewModelTest : KoinTest {

    private val fakeAuthRepository by inject<FakeAuthRepository>()
    private val fakeParticipantRepository by inject<FakeParticipantRepository>()
    private val fakeSessionStorage by inject<FakeSessionStorage>()

    private lateinit var viewModel: HomepageViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                coreTestModule,
                homepagePresentationModule,
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
    fun `getState `() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnUserAvatarClick`() = runBlocking {
        viewModel = HomepageViewModel(
            fakeAuthRepository,
            fakeParticipantRepository,
            fakeSessionStorage
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isUserMenuOpen).isFalse()
            viewModel.onAction(HomepageAction.OnUserAvatarClick)
            val newState = viewModel.state.first()
            assertThat(newState.isUserMenuOpen).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnProfileSettingsClick`() = runBlocking {
        viewModel = HomepageViewModel(
            fakeAuthRepository,
            fakeParticipantRepository,
            fakeSessionStorage
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isUserMenuOpen).isFalse()
            viewModel.onAction(HomepageAction.OnUserAvatarClick)
            val midState = viewModel.state.first()
            assertThat(midState.isUserMenuOpen).isTrue()
            viewModel.onAction(HomepageAction.OnProfileSettingsClick)
            val newState = viewModel.state.first()
            assertThat(newState.isUserMenuOpen).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnDismissUserMenu`() = runBlocking {
        viewModel = HomepageViewModel(
            fakeAuthRepository,
            fakeParticipantRepository,
            fakeSessionStorage
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isUserMenuOpen).isFalse()
            viewModel.onAction(HomepageAction.OnUserAvatarClick)
            val midState = viewModel.state.first()
            assertThat(midState.isUserMenuOpen).isTrue()
            viewModel.onAction(HomepageAction.OnDismissUserMenu)
            val newState = viewModel.state.first()
            assertThat(newState.isUserMenuOpen).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnLogoutClick`() = runBlocking {
        viewModel = HomepageViewModel(
            fakeAuthRepository,
            fakeParticipantRepository,
            fakeSessionStorage
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isUserMenuOpen).isFalse()
            viewModel.onAction(HomepageAction.OnUserAvatarClick)
            val midState = viewModel.state.first()
            assertThat(midState.isUserMenuOpen).isTrue()
            viewModel.onAction(HomepageAction.OnLogoutClick)
            val newState = viewModel.state.first()
            assertThat(newState.isUserMenuOpen).isFalse()
            assertThat(newState.showLogoutConfirmation).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnDismissLogoutDialog`() = runBlocking {
        viewModel = HomepageViewModel(
            fakeAuthRepository,
            fakeParticipantRepository,
            fakeSessionStorage
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isUserMenuOpen).isFalse()
            viewModel.onAction(HomepageAction.OnUserAvatarClick)
            val midState = viewModel.state.first()
            assertThat(midState.isUserMenuOpen).isTrue()
            viewModel.onAction(HomepageAction.OnLogoutClick)
            val mid2State = viewModel.state.first()
            assertThat(mid2State.isUserMenuOpen).isFalse()
            assertThat(mid2State.showLogoutConfirmation).isTrue()
            viewModel.onAction(HomepageAction.OnDismissLogoutDialog)
            val newState = viewModel.state.first()
            assertThat(newState.showLogoutConfirmation).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnConfirmLogout case success`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnConfirmLogout case logout error`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnConfirmLogout case deviceToken error`() = runBlocking {
        //TODO: IMPLEMENT
    }
}