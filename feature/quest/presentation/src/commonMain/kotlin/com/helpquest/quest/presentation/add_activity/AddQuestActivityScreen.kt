@file:OptIn(ExperimentalComposeUiApi::class)

package com.helpquest.quest.presentation.add_activity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddQuestActivityRoot(
    questId: String?,
    onClose: () -> Unit,
    viewModel: AddQuestActivityViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AddQuestActivityEvent.OnError -> {
                snackbarState.showSnackbar(
                    event.error.asStringAsync()
                )
            }

        }
    }

    BackHandler {
        onClose()
    }

    AddQuestActivityScreen(
        state = state,
        snackbarState = snackbarState,
        onAction = { action ->
            when (action) {
                is AddQuestActivityAction.OnCloseClick -> onClose()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun AddQuestActivityScreen(
    state: AddQuestActivityState,
    snackbarState: SnackbarHostState,
    onAction: (AddQuestActivityAction) -> Unit,
) {
    SnackbarScaffold(
        snackbarHostState = snackbarState,
        modifier = Modifier
            .fillMaxSize(),
    ) {

    }
}

@Composable
@Preview(
    showBackground = true
)
private fun AddQuestActivityScreenLightPreview() {
    HelpQuestTheme {
        AddQuestActivityScreen(
            state = AddQuestActivityState(),
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
private fun AddQuestActivityScreenDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        AddQuestActivityScreen(
            state = AddQuestActivityState(),
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}