@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.chat.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.helpquest.chat.data.service.FakeChatConnectionClient
import com.helpquest.chat.data.service.FakeChatMessageRepository
import com.helpquest.chat.data.service.FakeChatRepository
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.chat.presentation.chat_details.ChatDetailAction
import com.helpquest.chat.presentation.chat_details.ChatDetailEvent
import com.helpquest.chat.presentation.chat_details.ChatDetailState
import com.helpquest.chat.presentation.chat_details.ChatDetailViewModel
import com.helpquest.chat.presentation.di.chatPresentationModule
import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.test.auth.FakeSessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

class ChatDetailViewModelTest : KoinTest {

    private val fakeSessionStorage by inject<FakeSessionStorage>()

    private val fakeChatRepository by inject<FakeChatRepository>()
    private val fakeMessageRepository by inject<FakeChatMessageRepository>()
    private val fakeChatConnectionClient by inject<FakeChatConnectionClient>()


    val overrideChatDataModule = module {
        singleOf(::FakeSessionStorage) bind SessionStorage::class
        singleOf(::FakeChatRepository) bind ChatRepository::class
        singleOf(::FakeChatMessageRepository) bind MessageRepository::class
        singleOf(::FakeChatConnectionClient) bind ChatConnectionClient::class
    }

    private lateinit var viewModel: ChatDetailViewModel

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
    fun `getState selecting null or empty chat`() = runBlocking {
        // Test that when `OnSelectChat` is called with a null `chatId`, the state resets to its initial empty state.
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState.chatUi).isNull()
            assertThat(resultState.messages).isEmpty()
            assertThat(resultState.bannerState).isEqualTo(BannerState())
            assertThat(resultState.isChatOptionsOpen).isFalse()
            assertThat(resultState.connectionState).isEqualTo(ConnectionState.DISCONNECTED)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState state when auth info is null`() = runBlocking {
        // Check that `getState()` returns a default `ChatDetailState` if the `sessionStorage.observeAuthInfo()` emits a null value, effectively resetting the view state.
        fakeSessionStorage.resultAuthInfoFlow = MutableStateFlow(null)
        val expectedState = ChatDetailState()
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            val resultState = viewModel.state.first()

            assertThat(resultState.chatUi).isEqualTo(expectedState.chatUi)
            assertThat(resultState.messages).isEqualTo(expectedState.messages)
            assertThat(resultState.bannerState).isEqualTo(expectedState.bannerState)
            assertThat(resultState.isChatOptionsOpen).isEqualTo(expectedState.isChatOptionsOpen)
            assertThat(resultState.connectionState).isEqualTo(expectedState.connectionState)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState chat selection updates state`() = runBlocking {
        // When `OnSelectChat` action is called with a valid `chatId`, confirm that the state is updated with the corresponding chat information from the repository and `hasLoadedInitialData` becomes true.
        val expectedState = ChatDetailState(
            chatUi = ChatUi(
                id = fakeChatRepository.chat.id,
                localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                otherParticipants = listOf(fakeChatRepository.participant2.toParticipantUi()),
                lastMessage = fakeChatRepository.message,
                lastMessageSenderUsername = fakeChatRepository.participant2.username
            ),
        )
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            val resultStateBeforeUpdate = viewModel.state.first()

            assertThat(resultStateBeforeUpdate.chatUi).isNull()
            assertThat(resultStateBeforeUpdate.messages).isEmpty()
            assertThat(resultStateBeforeUpdate.bannerState).isEqualTo(BannerState())
            assertThat(resultStateBeforeUpdate.isChatOptionsOpen).isFalse()
            assertThat(resultStateBeforeUpdate.connectionState).isEqualTo(ConnectionState.DISCONNECTED)

            viewModel.onAction(ChatDetailAction.OnSelectChat(fakeChatRepository.chat.id))
            val resultState = viewModel.state.first()

            assertThat(resultState.chatUi).isEqualTo(expectedState.chatUi)
            assertThat(resultState.messages).isNotEmpty()
            assertThat(resultState.bannerState).isEqualTo(expectedState.bannerState)
            assertThat(resultState.isChatOptionsOpen).isEqualTo(expectedState.isChatOptionsOpen)
            assertThat(resultState.connectionState).isEqualTo(expectedState.connectionState)


            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState repository error handling on chat info fetch`() = runBlocking {
        // If `repository.getChatInfoById(chatId)` emits an error, verify that the state remains unchanged or gracefully handles the error without crashing.
        fakeChatRepository.fetchChatByIdResult = Result.Failure(DataError.Remote.NOT_FOUND)
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            viewModel.events.test {
                val resultStateBeforeUpdate = viewModel.state.first()

                assertThat(resultStateBeforeUpdate.chatUi).isNull()
                assertThat(resultStateBeforeUpdate.messages).isEmpty()
                assertThat(resultStateBeforeUpdate.bannerState).isEqualTo(BannerState())
                assertThat(resultStateBeforeUpdate.isChatOptionsOpen).isFalse()
                assertThat(resultStateBeforeUpdate.connectionState).isEqualTo(ConnectionState.DISCONNECTED)


                viewModel.onAction(ChatDetailAction.OnSelectChat(fakeChatRepository.chat.id))
                val resultState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(resultState.chatUi).isNull()
                assertThat(resultState.messages).isEmpty()
                assertThat(resultState.bannerState).isEqualTo(BannerState())
                assertThat(resultState.isChatOptionsOpen).isFalse()
                assertThat(resultState.connectionState).isEqualTo(ConnectionState.DISCONNECTED)
                assertThat(collectedEvent).isInstanceOf(
                    ChatDetailEvent.OnError::class
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState chat options open state update`() = runBlocking {
        // Confirm that calling `OnChatOptionsClick` updates the `isChatOptionsOpen` flag in the state to true.
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {

            viewModel.onAction(ChatDetailAction.OnChatOptionsClick)
            val resultState = viewModel.state.first()
            assertThat(resultState.isChatOptionsOpen).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getState chat options dismiss state update`() = runBlocking {
        // Confirm that calling `OnDismissChatOptions` updates the `isChatOptionsOpen` flag in the state to false.
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            viewModel.onAction(ChatDetailAction.OnChatOptionsClick)
            val resultStateBeforeUpdate = viewModel.state.first()
            assertThat(resultStateBeforeUpdate.isChatOptionsOpen).isTrue()
            viewModel.onAction(ChatDetailAction.OnDismissChatOptions)
            val resultState = viewModel.state.first()
            assertThat(resultState.isChatOptionsOpen).isFalse()

            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `onAction OnLeaveChatClick success case`() = runBlocking {
        // When `OnLeaveChatClick` is called and `repository.leaveChat` succeeds, verify that `_chatId` becomes null, state is cleared, text fields are cleared, and `OnChatLeft` event is sent.
        val expectedStateBeforeUpdate = ChatDetailState(
            chatUi = ChatUi(
                id = fakeChatRepository.chat2.id,
                localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                otherParticipants = listOf(fakeChatRepository.participant2.toParticipantUi()),
                lastMessage = null,
                lastMessageSenderUsername = null
            )
        )
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            viewModel.events.test {
                viewModel.onAction(ChatDetailAction.OnSelectChat(fakeChatRepository.chat2.id))
                val resultStateBeforeUpdate = viewModel.state.first()

                assertThat(resultStateBeforeUpdate.chatUi).isEqualTo(expectedStateBeforeUpdate.chatUi)
                assertThat(resultStateBeforeUpdate.messages).isNotEmpty()
                assertThat(resultStateBeforeUpdate.bannerState).isEqualTo(expectedStateBeforeUpdate.bannerState)
                assertThat(resultStateBeforeUpdate.isChatOptionsOpen).isEqualTo(
                    expectedStateBeforeUpdate.isChatOptionsOpen
                )
                assertThat(resultStateBeforeUpdate.connectionState).isEqualTo(
                    expectedStateBeforeUpdate.connectionState
                )

                viewModel.onAction(ChatDetailAction.OnLeaveChatClick)
                val successState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(successState.isChatOptionsOpen).isFalse()
                assertThat(successState.messageTextFieldState.text).isEqualTo("")
                assertThat(successState.chatUi).isNull()
                assertThat(successState.messages).isEmpty()
                assertThat(successState.bannerState).isEqualTo(BannerState())
                assertThat(collectedEvent).isEqualTo(
                    ChatDetailEvent.OnChatLeft
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnLeaveChatClick failure case`() = runBlocking {
        // If `repository.leaveChat` fails during an `OnLeaveChatClick` action, confirm that an `OnError` event is sent and the UI state (e.g., `_chatId`) remains unchanged.
        val expectedStateBeforeUpdate = ChatDetailState(
            chatUi = ChatUi(
                id = fakeChatRepository.chat2.id,
                localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                otherParticipants = listOf(fakeChatRepository.participant2.toParticipantUi()),
                lastMessage = null,
                lastMessageSenderUsername = null
            )
        )
        fakeChatRepository.leaveChatResult = Result.Failure(DataError.Remote.NOT_FOUND)
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            viewModel.events.test {
                viewModel.onAction(ChatDetailAction.OnSelectChat(fakeChatRepository.chat2.id))
                val resultStateBeforeUpdate = viewModel.state.first()

                assertThat(resultStateBeforeUpdate.chatUi).isEqualTo(expectedStateBeforeUpdate.chatUi)
                assertThat(resultStateBeforeUpdate.bannerState).isEqualTo(expectedStateBeforeUpdate.bannerState)
                assertThat(resultStateBeforeUpdate.isChatOptionsOpen).isEqualTo(
                    expectedStateBeforeUpdate.isChatOptionsOpen
                )
                assertThat(resultStateBeforeUpdate.connectionState).isEqualTo(
                    expectedStateBeforeUpdate.connectionState
                )

                viewModel.onAction(ChatDetailAction.OnLeaveChatClick)
                val failureState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(failureState.chatUi).isEqualTo(expectedStateBeforeUpdate.chatUi)
                assertThat(failureState.messages).isNotEmpty()
                assertThat(failureState.bannerState).isEqualTo(expectedStateBeforeUpdate.bannerState)
                assertThat(failureState.isChatOptionsOpen).isEqualTo(
                    expectedStateBeforeUpdate.isChatOptionsOpen
                )
                assertThat(failureState.connectionState).isEqualTo(
                    expectedStateBeforeUpdate.connectionState
                )
                assertThat(collectedEvent).isInstanceOf(
                    ChatDetailEvent.OnError::class
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnLeaveChatClick with no selected chat`() = runBlocking {
        // Call `OnLeaveChatClick` when `_chatId.value` is null. Verify that the function returns early and no repository calls or state changes occur.
        viewModel = ChatDetailViewModel(
            fakeChatRepository,
            fakeSessionStorage,
            fakeMessageRepository,
            fakeChatConnectionClient
        )
        viewModel.state.test {
            val startState = awaitItem()
            viewModel.onAction(ChatDetailAction.OnLeaveChatClick)
            val noUpdateState = viewModel.state.first()

            assertThat(noUpdateState).isEqualTo(startState)

            cancelAndConsumeRemainingEvents()
        }
    }
}