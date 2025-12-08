@file:OptIn(ExperimentalCoroutinesApi::class)

package quest.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.test.auth.FakeSessionStorage
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.presentation.di.questPresentationModule
import com.helpquest.quests.presentation.model.QuestLogItemUi
import com.helpquest.quests.presentation.quest_log.QuestLogAction
import com.helpquest.quests.presentation.quest_log.QuestLogEvent
import com.helpquest.quests.presentation.quest_log.QuestLogState
import com.helpquest.quests.presentation.quest_log.QuestLogViewModel
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
import quest.data.service.FakeQuestRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class QuestLogViewModelTest : KoinTest {

    private val fakeQuestRepository by inject<FakeQuestRepository>()
    private val fakeSessionStorage by inject<FakeSessionStorage>()

    val overrideQuestDataModule = module {
        singleOf(::FakeQuestRepository) bind QuestRepository::class
        singleOf(::FakeSessionStorage) bind SessionStorage::class
    }

    private lateinit var viewModel: QuestLogViewModel

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                overrideQuestDataModule,
                questPresentationModule,
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
    fun `onAction OnQuestClick updates state`() = runBlocking {
        // Call `onAction` with `OnQuestClick` and verify that the `state` Flow emits a new state with the `selectedQuestId` updated to the provided ID.
        viewModel = QuestLogViewModel(
            fakeQuestRepository,
        )
        viewModel.state.test {
            viewModel.onAction(
                QuestLogAction.OnQuestClick(
                    QuestLogItemUi(
                        questId = fakeQuestRepository.quest2.questId,
                        localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                        otherParticipants = listOf(fakeQuestRepository.participant2.toParticipantUi()),
                        lastActivity = null,
                        lastActivityActorUsername = null,
                    )
                )
            )
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedQuestId).isEqualTo(fakeQuestRepository.quest2.questId)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnLeaveQuestClick success case`() = runBlocking {
        // When `OnLeaveQuestClick` is called and `repository.leaveQuest` succeeds, verify that `_chatId` becomes null, state is cleared, text fields are cleared, and `OnQuestLeft` event is sent.
        val expectedStateBeforeUpdate = QuestLogState(
            quests = listOf(
                QuestLogItemUi(
                    questId = fakeQuestRepository.quest2.questId,
                    localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                    otherParticipants = listOf(fakeQuestRepository.participant2.toParticipantUi()),
                    lastActivity = null,
                    lastActivityActorUsername = null,
                )
            ),
            selectedQuestId = fakeQuestRepository.quest2.questId,
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )
        viewModel = QuestLogViewModel(
            fakeQuestRepository,
        )
        viewModel.state.test {
            viewModel.events.test {
                viewModel.onAction(
                    QuestLogAction.OnQuestClick(
                        QuestLogItemUi(
                            questId = fakeQuestRepository.quest2.questId,
                            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                            otherParticipants = listOf(fakeQuestRepository.participant2.toParticipantUi()),
                            lastActivity = null,
                            lastActivityActorUsername = null,
                        )
                    )
                )
                val resultStateBeforeUpdate = viewModel.state.first()

                assertThat(resultStateBeforeUpdate.selectedQuestId).isEqualTo(fakeQuestRepository.quest2.questId)


                viewModel.onAction(QuestLogAction.OnLeaveQuestClick(fakeQuestRepository.quest2.questId))
                val successState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(successState.quests).isEmpty()
                assertThat(collectedEvent).isEqualTo(
                    QuestLogEvent.OnQuestLeft
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction OnLeaveQuestClick failure case`() = runBlocking {
        // If `repository.leaveQuest` fails during an `OnLeaveQuestClick` action, confirm that an `OnError` event is sent and the UI state remains unchanged.
        val expectedStateBeforeUpdate = QuestLogState(
            quests = listOf(
                QuestLogItemUi(
                    questId = fakeQuestRepository.quest2.questId,
                    localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                    otherParticipants = listOf(fakeQuestRepository.participant2.toParticipantUi()),
                    lastActivity = null,
                    lastActivityActorUsername = null,
                )
            ),
            selectedQuestId = fakeQuestRepository.quest2.questId,
            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
        )
        fakeQuestRepository.leaveQuestResult = Result.Failure(DataError.Remote.NOT_FOUND)
        viewModel = QuestLogViewModel(
            fakeQuestRepository,
        )
        viewModel.state.test {
            viewModel.events.test {
                viewModel.onAction(
                    QuestLogAction.OnQuestClick(
                        QuestLogItemUi(
                            questId = fakeQuestRepository.quest2.questId,
                            localParticipant = fakeSessionStorage.fakeAuthInfo.user.toParticipantUi(),
                            otherParticipants = listOf(fakeQuestRepository.participant2.toParticipantUi()),
                            lastActivity = null,
                            lastActivityActorUsername = null,
                        )
                    )
                )
                val resultStateBeforeUpdate = viewModel.state.first()

                assertThat(resultStateBeforeUpdate.selectedQuestId).isEqualTo(fakeQuestRepository.quest2.questId)


                viewModel.onAction(QuestLogAction.OnLeaveQuestClick(fakeQuestRepository.quest2.questId))
                val failureState = viewModel.state.first()
                val collectedEvent = awaitItem()

                assertThat(failureState.selectedQuestId).isEqualTo(fakeQuestRepository.quest2.questId)

                assertThat(collectedEvent).isInstanceOf(
                    QuestLogEvent.OnError::class
                )
                cancelAndConsumeRemainingEvents()
            }
            cancelAndConsumeRemainingEvents()
        }
    }

}