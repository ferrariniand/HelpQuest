package com.helpquest.quests.presentation.quest_board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.buttons.HelpQuestFloatingActionButton
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.pagination.PaginationScrollListener
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.quests.presentation.components.QuestListUi
import helpquest.feature.quest.presentation.generated.resources.Res
import helpquest.feature.quest.presentation.generated.resources.create_quest
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuestBoardRoot(
    selectedQuestId: String?,
    onSelectQuest: (String?) -> Unit,
    onCreateQuestClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    viewModel: QuestBoardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is QuestBoardEvent.OnError -> {
                snackbarHostState.showSnackbar(
                    event.error.asStringAsync()
                )
            }
            else -> Unit
        }
    }


    LaunchedEffect(selectedQuestId) {
        viewModel.onAction(QuestBoardAction.OnSelectQuest(selectedQuestId))
    }

    QuestBoardScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is QuestBoardAction.OnSelectQuest -> onSelectQuest(action.questId)
                QuestBoardAction.OnCreateQuestClick -> onCreateQuestClick()
                QuestBoardAction.OnProfileSettingsClick -> onProfileSettingsClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun QuestBoardScreen(
    state: QuestBoardState,
    onAction: (QuestBoardAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val questListState = rememberLazyListState()

    val questItemCount = remember { state.quests.size }

    PaginationScrollListener(
        lazyListState = questListState,
        itemCount = questItemCount,
        isPaginationLoading = state.isPaginationLoading,
        isEndReached = state.endReached,
        onNearEnd = {
            onAction(QuestBoardAction.OnScrollToBottom)
        }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            HelpQuestFloatingActionButton(
                onClick = {
                    onAction(QuestBoardAction.OnCreateQuestClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.create_quest)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuestListUi(
                quests = state.quests,
                selectedQuestId = state.selectedQuestId,
                listState = questListState,
                isLoading = state.isLoading,
                isPaginationLoading = state.isPaginationLoading,
                paginationError = state.paginationError?.asString(),
                onSelectQuest = { questId ->
                    onAction(QuestBoardAction.OnSelectQuest(questId))
                },
                onRetryPaginationClick = {
                    onAction(QuestBoardAction.OnRetryPaginationClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun QuestBoardScreenLightPreview() {
    HelpQuestTheme {
        QuestBoardScreen(
            state = QuestBoardState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun QuestBoardScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        QuestBoardScreen(
            state = QuestBoardState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}