package com.helpquest.quests.presentation.quest_log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuestLogRoot(
    viewModel: QuestLogViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            QuestLogEvent.OnQuestLeftOrDeleted -> {
                //TODO Maybe Not needed
            }

            is QuestLogEvent.OnError -> {
                snackbarState.showSnackbar(
                    event.error.asStringAsync()
                )
            }
        }
    }

    QuestLogScreen(
        state = state,
        snackbarState = snackbarState,
        onAction = viewModel::onAction
    )
}

@Composable
fun QuestLogScreen(
    state: QuestLogState,
    snackbarState: SnackbarHostState,
    onAction: (QuestLogAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()

    SnackbarScaffold(
        snackbarHostState = snackbarState,
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (!configuration.isWideScreen) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.extended.surfaceLower
        }
    ) {
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {

        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun QuestLogScreenLightPreview() {
    HelpQuestTheme {
        QuestLogScreen(
            state = QuestLogState(),
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
private fun QuestLogScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        QuestLogScreen(
            state = QuestLogState(),
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}