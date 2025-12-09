@file:OptIn(ExperimentalCoroutinesApi::class)

package com.helpquest.chat.presentation

import androidx.compose.foundation.text.input.TextFieldState
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.helpquest.chat.data.service.FakeChatParticipantService
import com.helpquest.chat.data.service.FakeChatRepository
import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.presentation.create_chat.CreateChatEvent
import com.helpquest.chat.presentation.create_manage_chat.CreateChatViewModel
import com.helpquest.chat.presentation.create_manage_chat.ManageChatAction
import com.helpquest.chat.presentation.create_manage_chat.ManageChatState
import com.helpquest.chat.presentation.di.chatPresentationModule
import com.helpquest.core.designsystem.components.selection_sections.SearchResult
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.modelsUi.ParticipantUi
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


class CreateChatViewModelTest : KoinTest {

    private val fakeChatParticipantService by inject<FakeChatParticipantService>()
    private val fakeChatRepository by inject<FakeChatRepository>()

    val overrideChatDataModule = module {
        singleOf(::FakeChatParticipantService) bind ChatParticipantService::class
        singleOf(::FakeChatRepository) bind ChatRepository::class
    }

    private lateinit var viewModel: CreateChatViewModel

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
    fun `OnDebounceSearchTextField with blank query`() = runBlocking {
        // When OnDebounceSearchTextField action is called with a blank or empty search query, verify the state is updated to clear the search result and disable adding participants.
        val stateEmptyQuery = ManageChatState(
            queryTextState = TextFieldState(
                initialText = ""
            ),
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateEmptyQuery
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnDebounceSearchTextField)
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnDebounceSearchTextField triggers successful search`() = runBlocking {
        // When performSearch is called with a valid query and the service returns a success result, check that the state is updated with the found participant, isSearching is false, and canAddParticipant is true.
        val stateValidQuery = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuery
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnDebounceSearchTextField)
            val resultState = viewModel.state.first()

            assertThat(
                resultState.canAddParticipant.getValue(
                    fakeChatParticipantService.participant.toParticipantUi()
                )
            ).isTrue()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isEqualTo(
                SearchResult.Success(
                    listOf(fakeChatParticipantService.participant.toParticipantUi())
                )
            )
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnDebounceSearchTextField triggers search with  Not Found  error`() = runBlocking {
        // If the search service returns a DataError.Remote.NOT_FOUND error, ensure the state reflects that the participant was not found and searching has stopped.
        val stateNotFoundQuery = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "not-found"
            ),
        )
        fakeChatParticipantService.searchParticipantResult =
            Result.Failure(DataError.Remote.NOT_FOUND)
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateNotFoundQuery
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnDebounceSearchTextField)
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isEqualTo(
                SearchResult.NotFound
            )
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnDebounceSearchTextField triggers search with a generic error`() = runBlocking {
        // If the search service returns any other error, verify the state is updated with the correct UI error message and isSearching is set to false.
        val stateValidQuery = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
        )
        fakeChatParticipantService.searchParticipantResult =
            Result.Failure(DataError.Remote.UNKNOWN)
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuery
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnDebounceSearchTextField)
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isNull()
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnAddClick with a valid search result`() = runBlocking {
        // When a participant is found and OnAddClick is triggered, verify the participant is added to selectedChatParticipants, the search query is cleared, and canAddParticipant is set to false.
        val participant = fakeChatParticipantService.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = emptyList(),
            canAddParticipant = mapOf(participant to true)
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnAddClick(participant))
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isNull()
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(1)
            assertThat(resultState.queryTextState.text).isEqualTo("")
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnAddClick with a duplicate participant`() = runBlocking {
        // If OnAddClick is called for a participant that is already in the selectedChatParticipants list, verify that the participant is not added again.
        val participant = fakeChatParticipantService.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = listOf(participant),
            canAddParticipant = mapOf(participant to true)
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnAddClick(participant))
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEqualTo(mapOf(participant to true))
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isEqualTo(
                SearchResult.Success(
                    listOf(participant)
                )
            )
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(1)
            assertThat(resultState.queryTextState.text).isEqualTo("primo")
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnAddClick with canAddParticipant empty`() = runBlocking {
        // Trigger OnAddClick when currentSearchResult is null and verify that the selectedChatParticipants list remains unchanged.
        // If OnAddClick is called for a participant that is already in the selectedChatParticipants list, verify that the participant is not added again.
        val participant = fakeChatParticipantService.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = emptyList(),
            canAddParticipant = emptyMap()
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnAddClick(participant))
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isEqualTo(
                SearchResult.Success(
                    listOf(participant)
                )
            )
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(0)
            assertThat(resultState.queryTextState.text).isEqualTo("primo")
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnDismissDialog action resets search state`() = runBlocking {
        // When OnDismissDialog action is called, verify that isSearching is set to false and any existing searchError is cleared from the state.
        val participant = fakeChatParticipantService.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = listOf(
                ParticipantUi(
                    id = "id1",
                    username = "primo",
                    imageUrl = "test",
                    initials = "PR",
                    showParticipantIdentity = false,
                    classImageUrl = "class",
                )
            ),
            canAddParticipant = mapOf(participant to true)
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnDismissDialog)
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isNull()
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(1)
            assertThat(resultState.queryTextState.text).isEqualTo("")
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnCreateChatClick action with empty selectedChatParticipants`() = runBlocking {
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = null,
            selectedChatParticipants = emptyList(),
            canAddParticipant = emptyMap()
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.onAction(ManageChatAction.OnPrimaryActionClick)
            val resultState = viewModel.state.first()

            assertThat(resultState.canAddParticipant).isEmpty()
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.isCreatingChat).isFalse()
            assertThat(resultState.currentSearchResult).isNull()
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(0)
            assertThat(resultState.queryTextState.text).isEqualTo("primo")
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `OnCreateChatClick action with success response`() = runBlocking {
        val participant = fakeChatRepository.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = listOf(participant),
            canAddParticipant = mapOf(participant to true)
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            viewModel.events.test {
                viewModel.onAction(ManageChatAction.OnPrimaryActionClick)
                val successState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(successState.canAddParticipant).isEmpty()
                assertThat(successState.isSearching).isFalse()
                assertThat(successState.isCreatingChat).isFalse()
                assertThat(successState.currentSearchResult).isNull()
                assertThat(successState.searchError).isNull()
                assertThat(successState.createChatError).isNull()
                assertThat(successState.selectedChatParticipants).isEmpty()
                assertThat(successState.queryTextState.text).isEqualTo("")
                assertThat(collectedEvent).isEqualTo(
                    CreateChatEvent.OnChatCreated(
                        fakeChatRepository.chat
                    )
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnCreateChatClick action with error response`() = runBlocking {
        val participant = fakeChatRepository.participant.toParticipantUi()
        val stateValidQuerySearched = ManageChatState(
            queryTextState = TextFieldState(
                initialText = "primo"
            ),
            currentSearchResult = SearchResult.Success(
                listOf(participant)
            ),
            selectedChatParticipants = listOf(participant),
            canAddParticipant = mapOf(participant to true)
        )
        viewModel = CreateChatViewModel(
            fakeChatParticipantService,
            fakeChatRepository,
            initialState = stateValidQuerySearched
        )
        viewModel.state.test {
            fakeChatRepository.createChatResult = Result.Failure(DataError.Remote.UNKNOWN)
            viewModel.onAction(ManageChatAction.OnPrimaryActionClick)
            val resultState = viewModel.state.first()

            assertThat(resultState.isCreatingChat).isFalse()
            assertThat(resultState.createChatError).isNotNull()
            assertThat(resultState.canAddParticipant).isEqualTo(mapOf(participant to true))
            assertThat(resultState.isSearching).isFalse()
            assertThat(resultState.currentSearchResult).isEqualTo(
                SearchResult.Success(
                    listOf(participant)
                )
            )
            assertThat(resultState.selectedChatParticipants.size).isEqualTo(1)
            assertThat(resultState.queryTextState.text).isEqualTo("primo")
            cancelAndConsumeRemainingEvents()

        }
    }


}