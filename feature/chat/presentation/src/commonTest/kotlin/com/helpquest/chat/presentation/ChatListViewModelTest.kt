@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.chat.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.chat.data.service.FakeChatRepository
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.presentation.chat_list.ChatListAction
import com.helpquest.chat.presentation.chat_list.ChatListState
import com.helpquest.chat.presentation.chat_list.ChatListViewModel
import com.helpquest.chat.presentation.di.chatPresentationModule
import com.helpquest.chat.presentation.mappers.toChatUi
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.test.auth.FakeSessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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

class ChatListViewModelTest : KoinTest {
    private val fakeSessionStorage by inject<FakeSessionStorage>()

    private val fakeChatRepository by inject<FakeChatRepository>()

    val overrideChatDataModule = module {
        singleOf(::FakeSessionStorage) bind SessionStorage::class
        singleOf(::FakeChatRepository) bind ChatRepository::class
    }

    private lateinit var viewModel: ChatListViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                overrideChatDataModule,
                chatPresentationModule,
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
    fun `getState authenticated user state emission`() = runBlocking {
        // Given a valid `AuthInfo` from `sessionStorage`, verify that `getState()` emits a state with correctly mapped chats and local participant information.
        val expectedState = ChatListState(
            chats = fakeChatRepository.chatList.map { chat ->
                chat.toChatUi(fakeSessionStorage.fakeAuthInfo.user.id)
            },
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )

        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState).isEqualTo(expectedState)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState unauthenticated user state emission`() = runBlocking {
        // When `sessionStorage` emits a null `AuthInfo`, verify that `getState()` emits a default, empty `ChatListState()`.
        fakeSessionStorage.resultAuthInfoFlow = MutableStateFlow(null)
        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState).isEqualTo(ChatListState())
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `getState updates on new chats`() = runBlocking {
        // When `repository.getChats()` emits a new list of chats, verify that the `state` Flow updates and emits a new state reflecting these changes.
        val expectedStateBeforeUpdate = ChatListState(
            chats = fakeChatRepository.chatList.map { chat ->
                chat.toChatUi(fakeSessionStorage.fakeAuthInfo.user.id)
            },
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )

        val expectedStateAfterUpdate = ChatListState(
            chats = mutableListOf(fakeChatRepository.chat2).map { chat ->
                chat.toChatUi(fakeSessionStorage.fakeAuthInfo.user.id)
            },
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )

        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            val resultStateBeforeUpdate = viewModel.state.first()

            assertThat(resultStateBeforeUpdate).isEqualTo(expectedStateBeforeUpdate)
            fakeChatRepository.chatListFlow.update {
                mutableListOf(fakeChatRepository.chat2)
            }
            val resultState = viewModel.state.first()

            assertThat(resultState).isEqualTo(expectedStateAfterUpdate)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState updates on auth info change`() = runBlocking {
        // Verify that if the `AuthInfo` changes (e.g., from null to a valid user), the `state` Flow updates accordingly with the chats and user info.
        val expectedState = ChatListState(
            chats = fakeChatRepository.chatList.map { chat ->
                chat.toChatUi(fakeSessionStorage.fakeAuthInfo.user.id)
            },
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )

        fakeSessionStorage.resultAuthInfoFlow = MutableStateFlow(null)
        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            val resultStateBeforeUpdate = viewModel.state.first()

            assertThat(resultStateBeforeUpdate).isEqualTo(ChatListState())

            fakeSessionStorage.resultAuthInfoFlow.update {
                fakeSessionStorage.fakeAuthInfo
            }
            val resultState = viewModel.state.first()
            assertThat(resultState).isEqualTo(expectedState)

            cancelAndConsumeRemainingEvents()

        }
    }


    @Test
    fun `onAction OnSelectChat updates state`() = runBlocking {
        // Call `onAction` with `OnSelectChat` and verify that the `state` Flow emits a new state with the `selectedChatId` updated to the provided ID.
        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            viewModel.onAction(ChatListAction.OnSelectChat(fakeChatRepository.chat2.id))
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedChatId).isEqualTo(fakeChatRepository.chat2.id)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnChatClick updates state`() = runBlocking {
        // Call `onAction` with `OnSelectChat` and verify that the `state` Flow emits a new state with the `selectedChatId` updated to the ID from the `ChatUi` object.
        viewModel = ChatListViewModel(
            fakeChatRepository,
            fakeSessionStorage,
        )
        viewModel.state.test {
            viewModel.onAction(
                ChatListAction.OnSelectChat(
                    fakeChatRepository.chat2.id
                )
            )
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedChatId).isEqualTo(fakeChatRepository.chat2.id)
            cancelAndConsumeRemainingEvents()

        }
    }

}