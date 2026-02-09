package com.helpquest.quests.presentation.quest_log_detail

import androidx.compose.runtime.Composable

@Composable
fun QuestLogDetailAdaptiveLayout(
    initialQuestId: String?,
//    onProfileSettingsClick: () -> Unit,
//    questLogDetailViewModel: QuestLogDetailViewModel = koinViewModel()
) {
//    val sharedState by questLogDetailViewModel.state.collectAsStateWithLifecycle()
//    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
//    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
//        scaffoldDirective = scaffoldDirective
//    )
//    val scope = rememberCoroutineScope()
//
//    fun navigateBackInternally() {
//        scope.launch {
//            scaffoldNavigator.navigateBack()
//        }
//    }
//
//    LaunchedEffect(initialQuestId) {
//        if (initialQuestId != null) {
//            questLogDetailViewModel.onAction(QuestLogDetailAction.OnSelectQuest(initialQuestId))
//            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
//        }
//    }
//    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
//        navigateBackInternally()
//        questLogDetailViewModel.onAction(QuestLogDetailAction.OnSelectQuest(null))
//    }
}