@file:OptIn(ExperimentalCoroutinesApi::class)

package quest.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.domain.service.ConnectionClient
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.presentation.di.questPresentationModule
import com.helpquest.quests.presentation.quest_board.QuestBoardAction
import com.helpquest.quests.presentation.quest_board.QuestBoardViewModel
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
import quest.data.service.FakeQuestConnectionClient
import quest.data.service.FakeQuestRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class QuestBoardViewModelTest : KoinTest {

    private val fakeQuestRepository by inject<FakeQuestRepository>()
    private val fakeQuestConnectionClient by inject<FakeQuestConnectionClient>()

    val overrideQuestDataModule = module {
        singleOf(::FakeQuestRepository) bind QuestRepository::class
        singleOf(::FakeQuestConnectionClient) bind ConnectionClient::class
    }

    private lateinit var viewModel: QuestBoardViewModel

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
    fun `onAction OnSelectQuest updates state`() = runBlocking {
        // Call `onAction` with `OnSelectQuest` and verify that the `state` Flow emits a new state with the `selectedQuestId` updated to the provided ID.
        viewModel = QuestBoardViewModel(
            fakeQuestRepository,
            fakeQuestConnectionClient
        )
        viewModel.state.test {
            viewModel.onAction(
                QuestBoardAction.OnSelectQuest(
                    questId = fakeQuestRepository.quest2.questId,
                )
            )
            val resultState = viewModel.state.first()

            assertThat(resultState.selectedQuestId).isEqualTo(fakeQuestRepository.quest2.questId)
            cancelAndConsumeRemainingEvents()

        }
    }

    @Test
    fun `onAction OnCreateQuestClick`() = runBlocking {

    }

    @Test
    fun `onAction OnHideBanner`() = runBlocking {

    }

    @Test
    fun `onAction OnTopVisibleIndexChanged`() = runBlocking {

    }


}