@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.profile.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.helpquest.core.domain.util.ClassUtils
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.core.test.di.coreTestModule
import com.helpquest.core.test.service.auth.FakeAuthService
import com.helpquest.core.test.service.participant.FakeParticipantRepository
import com.helpquest.profile.presentation.di.profilePresentationModule
import helpquest.feature.profile.presentation.generated.resources.Res
import helpquest.feature.profile.presentation.generated.resources.error_current_password_equal_to_new_one
import helpquest.feature.profile.presentation.generated.resources.error_current_password_incorrect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

class ProfileViewModelTest : KoinTest {

    private val fakeAuthService by inject<FakeAuthService>()
    private val fakeParticipantRepository by inject<FakeParticipantRepository>()
    private val fakeSessionStorage by inject<FakeSessionStorage>()

    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                coreTestModule,
                profilePresentationModule,
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
    fun `getState state when auth info is null`() = runBlocking {
        // Check that `getState()` returns a default `ProfileState` if the `sessionStorage.observeAuthInfo()` emits a null value, effectively resetting the view state.
        fakeSessionStorage.resultAuthInfoFlow = MutableStateFlow(null)
        val expectedState = ProfileState()
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState.username).isEqualTo(expectedState.username)
            assertThat(resultState.userInitials).isEqualTo(expectedState.userInitials)
            assertThat(resultState.profilePictureUrl).isEqualTo(expectedState.profilePictureUrl)
            assertThat(resultState.classImageUrl).isEqualTo(expectedState.classImageUrl)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState state when auth info is not null and participant fetched fails`() = runBlocking {
        // Check that `getState()` returns a modified `ProfileState` if the `sessionStorage.observeAuthInfo()` emits a not null value, effectively resetting the view state.
        fakeParticipantRepository.fetchLocalParticipantResult =
            Result.Failure(DataError.Remote.NO_INTERNET)
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState.username).isEqualTo(fakeSessionStorage.fakeAuthInfo.user.username)
            assertThat(resultState.userInitials).isEqualTo(fakeSessionStorage.fakeAuthInfo.user.initials)
            assertThat(resultState.profilePictureUrl).isEqualTo(fakeSessionStorage.fakeAuthInfo.user.profilePictureUrl)
            assertThat(resultState.classImageUrl).isEqualTo(
                ClassUtils.findClassById(
                    fakeSessionStorage.fakeAuthInfo.user.classId
                ).classImageUrl
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState state when auth info and participant fetched are not null`() = runBlocking {
        // Check that `getState()` returns a modified `ProfileState` if the `sessionStorage.observeAuthInfo()` emits a not null value, effectively resetting the view state.
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState.username).isEqualTo(fakeParticipantRepository.participant.username)
            assertThat(resultState.userInitials).isEqualTo(fakeParticipantRepository.participant.initials)
            assertThat(resultState.profilePictureUrl).isEqualTo(fakeParticipantRepository.participant.profilePictureUrl)
            assertThat(resultState.classImageUrl).isEqualTo(fakeParticipantRepository.participant.participantClass.classImageUrl)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `change password success`() = runBlocking {
        fakeAuthService.changePasswordResult = Result.Success(Unit)
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isPasswordChangeSuccessful).isFalse()
            viewModel.onAction(ProfileAction.OnChangePasswordClick)
            val successState = viewModel.state.first()
            assertThat(successState.isPasswordChangeSuccessful).isTrue()
            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `change password failure unauthorized`() = runBlocking {
        fakeAuthService.changePasswordResult = Result.Failure(DataError.Remote.UNAUTHORIZED)
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isPasswordChangeSuccessful).isFalse()
            viewModel.onAction(ProfileAction.OnChangePasswordClick)
            val failureState = viewModel.state.first()
            assertThat(failureState.isPasswordChangeSuccessful).isFalse()
            val errorResult = failureState.newPasswordError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_current_password_incorrect)

            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `change password failure conflict`() = runBlocking {
        fakeAuthService.changePasswordResult = Result.Failure(DataError.Remote.CONFLICT)
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isPasswordChangeSuccessful).isFalse()
            viewModel.onAction(ProfileAction.OnChangePasswordClick)
            val failureState = viewModel.state.first()
            assertThat(failureState.isPasswordChangeSuccessful).isFalse()
            val errorResult = failureState.newPasswordError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isEqualTo(Res.string.error_current_password_equal_to_new_one)

            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `change password failure generic error`() = runBlocking {
        fakeAuthService.changePasswordResult = Result.Failure(DataError.Remote.SERVER_ERROR)
        viewModel = ProfileViewModel(fakeAuthService, fakeParticipantRepository, fakeSessionStorage)

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.isPasswordChangeSuccessful).isFalse()
            viewModel.onAction(ProfileAction.OnChangePasswordClick)
            val failureState = viewModel.state.first()
            assertThat(failureState.isPasswordChangeSuccessful).isFalse()
            val errorResult = failureState.newPasswordError
            assertThat(
                errorResult
            ).isNotNull()
            assertThat(
                errorResult!!
            ).isInstanceOf(UiText.Resource::class)

            assertThat(
                (errorResult as UiText.Resource).id
            ).isNotEqualTo(Res.string.error_current_password_equal_to_new_one)
            assertThat(
                errorResult.id
            ).isNotEqualTo(Res.string.error_current_password_incorrect)

            cancelAndConsumeRemainingEvents()
        }

    }

    @Test
    fun `onAction OnPictureSelected case success`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnPictureSelected case error`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnConfirmDeleteClick case success`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnConfirmDeleteClick case error`() = runBlocking {
        //TODO: IMPLEMENT
    }


    @Test
    fun `onAction OnDeletePictureClick`() = runBlocking {
        //TODO: IMPLEMENT
    }

    @Test
    fun `onAction OnDismissDeleteConfirmationDialogClick`() = runBlocking {
        //TODO: IMPLEMENT
    }
}