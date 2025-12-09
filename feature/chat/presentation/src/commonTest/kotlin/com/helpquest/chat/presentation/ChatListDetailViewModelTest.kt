@file:OptIn(ExperimentalCoroutinesApi::class)


package com.helpquest.chat.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.helpquest.chat.presentation.chat_list_detail.ChatListDetailAction
import com.helpquest.chat.presentation.chat_list_detail.ChatListDetailEvent
import com.helpquest.chat.presentation.chat_list_detail.ChatListDetailViewModel
import com.helpquest.chat.presentation.chat_list_detail.DialogState
import com.helpquest.chat.presentation.di.chatPresentationModule
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ChatListDetailViewModelTest : KoinTest {

    private lateinit var viewModel: ChatListDetailViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
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
    fun `onAction OnChatClick updates state`() = runBlocking {
        // Verify that when `OnChatClick` action is triggered with a specific chatId, the `selectedChatId` in the state flow is correctly updated to that chatId.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnChatClick(chatId = "id"))
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedChatId).isEqualTo("id")
            assertThat(resultState.dialogState).isEqualTo(DialogState.Hidden)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnCreateChatClick updates dialog state`() = runBlocking {
        // Verify that `OnManageChatClick` action updates the `dialogState` in the state flow to `DialogState.CreateChat`.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnCreateChatClick)
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedChatId).isNull()
            assertThat(resultState.dialogState).isEqualTo(DialogState.CreateChat)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnDismissCurrentDialog updates dialog state and sends event`() = runBlocking {
        // Verify that `OnDismissCurrentDialog` action updates the `dialogState` in the state flow to `DialogState.Hidden`.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.events.test {
                viewModel.onAction(ChatListDetailAction.OnCreateChatClick)
                val resultStateAfterCreate = viewModel.state.first()

                assertThat(resultStateAfterCreate.selectedChatId).isNull()
                assertThat(resultStateAfterCreate.dialogState).isEqualTo(DialogState.CreateChat)
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(true))
                val resultStateAfterDismiss = viewModel.state.first()
                val collectedEvent = awaitItem()


                assertThat(resultStateAfterDismiss.selectedChatId).isNull()
                assertThat(resultStateAfterDismiss.dialogState).isEqualTo(DialogState.Hidden)
                assertThat(collectedEvent).isEqualTo(ChatListDetailEvent.CreateChatDialogDismissed)
            }
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnDismissCurrentDialog updates dialog state`() = runBlocking {
        // Verify that `OnDismissCurrentDialog` action updates the `dialogState` in the state flow to `DialogState.Hidden`.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnCreateChatClick)
            val resultStateAfterCreate = viewModel.state.first()

            assertThat(resultStateAfterCreate.selectedChatId).isNull()
            assertThat(resultStateAfterCreate.dialogState).isEqualTo(DialogState.CreateChat)
            viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog(false))
            val resultStateAfterDismiss = viewModel.state.first()


            assertThat(resultStateAfterDismiss.selectedChatId).isNull()
            assertThat(resultStateAfterDismiss.dialogState).isEqualTo(DialogState.Hidden)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnManageChatClick with selected chat`() = runBlocking {
        // Given the state has a non-null `selectedChatId`, verify that `OnManageChatClick` action updates the `dialogState` to `DialogState.ManageChat` with the correct chatId.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnChatClick(chatId = "id"))
            val resultStateChatClick = viewModel.state.first()

            assertThat(resultStateChatClick.selectedChatId).isEqualTo("id")
            assertThat(resultStateChatClick.dialogState).isEqualTo(DialogState.Hidden)
            viewModel.onAction(ChatListDetailAction.OnManageChatClick)
            val resultStateManageChat = viewModel.state.first()
            assertThat(resultStateManageChat.selectedChatId).isEqualTo("id")
            assertThat(resultStateManageChat.dialogState).isEqualTo(DialogState.ManageChat("id"))
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnManageChatClick with no selected chat`() = runBlocking {
        // Given the state has a null `selectedChatId`, verify that `OnManageChatClick` action does not change the `dialogState` and no update is emitted.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnManageChatClick)
            val resultStateManageChat = viewModel.state.first()
            assertThat(resultStateManageChat.selectedChatId).isNull()
            assertThat(resultStateManageChat.dialogState).isEqualTo(DialogState.Hidden)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnProfileSettingsClick updates dialog state`() = runBlocking {
        // Verify that `OnProfileSettingsClick` action updates the `dialogState` in the state flow to `DialogState.Profile`.
        viewModel = ChatListDetailViewModel()
        viewModel.state.test {
            val initState = awaitItem()
            assertThat(initState.selectedChatId).isNull()
            assertThat(initState.dialogState).isEqualTo(DialogState.Hidden)

            viewModel.onAction(ChatListDetailAction.OnProfileSettingsClick)
            val resultStateManageChat = viewModel.state.first()
            assertThat(resultStateManageChat.selectedChatId).isNull()
            assertThat(resultStateManageChat.dialogState).isEqualTo(DialogState.Profile)
            cancelAndConsumeRemainingEvents()

        }
    }
}