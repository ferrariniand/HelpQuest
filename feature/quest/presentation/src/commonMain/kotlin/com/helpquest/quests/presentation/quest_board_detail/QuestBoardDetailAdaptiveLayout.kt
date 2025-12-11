@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)

package com.helpquest.quests.presentation.quest_board_detail

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.util.DialogSheetScopedViewModelContainer
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.quests.presentation.quest_board.QuestBoardRoot
import com.helpquest.quests.presentation.quest_details.QuestDetailRoot
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuestBoardDetailAdaptiveLayout(
    questBoardDetailViewModel: QuestBoardDetailViewModel = koinViewModel()
) {
    val sharedState by questBoardDetailViewModel.state.collectAsStateWithLifecycle()
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective
    )
    val scope = rememberCoroutineScope()

    fun navigateBackInternally() {
        scope.launch {
            scaffoldNavigator.navigateBack()
        }
    }

    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
        navigateBackInternally()
    }

    ObserveAsEvents(questBoardDetailViewModel.events) { event ->
        when (event) {
            QuestBoardDetailEvent.CreateQuestDialogDismissed -> if (scaffoldNavigator.canNavigateBack()) {
                navigateBackInternally()
            }
        }
    }

    val listPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List]
    val detailPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail]

    LaunchedEffect(detailPane, sharedState.selectedQuestId) {
        if (detailPane == PaneAdaptedValue.Hidden && sharedState.selectedQuestId != null) {
            questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnQuestClick(null))
        }
    }

    ListDetailPaneScaffold(
        directive = scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        listPane = {
            AnimatedPane {
                QuestBoardRoot(
                    questId = sharedState.selectedQuestId,
                    onQuestClick = {
                        questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnQuestClick(it.questId))
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail
                            )
                        }
                    },
                    onCreateQuestClick = {
                        questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnCreateQuestClick)
                    },
                    onProfileSettingsClick = {
                        questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnProfileSettingsClick)
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                QuestDetailRoot(
                    questId = sharedState.selectedQuestId,
                    showBackButton = detailPane == PaneAdaptedValue.Hidden || listPane == PaneAdaptedValue.Hidden,
                    onBack = {
                        scope.launch {
                            if (scaffoldNavigator.canNavigateBack()) {
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    },
                    onQuestMembersClick = {
                        questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnQuestMembersClick)
                    }
                )
            }
        }
    )

    DialogSheetScopedViewModelContainer(
        visible = sharedState.dialogState is DialogState.CreateQuest
    ) {
        //TODO
//        CreateQuestRoot(
//            onDismiss = {
//                questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnDismissCurrentDialog)
//            },
//            onQuestCreated = { quest ->
//                questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnDismissCurrentDialog)
//                questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnQuestClick(quest.questId))
//                scope.launch {
//                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
//                }
//            },
//        )
    }

    DialogSheetScopedViewModelContainer(
        visible = sharedState.dialogState is DialogState.ManageQuest
    ) {
        //TODO
//        ManageQuestBoardRoot(
//            onDismiss = {
//                questBoardDetailViewModel.onAction(QuestBoardDetailAction.OnDismissCurrentDialog)
//            }
//        )
    }
}