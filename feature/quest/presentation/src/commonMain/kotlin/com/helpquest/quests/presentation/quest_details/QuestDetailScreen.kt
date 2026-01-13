@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalUuidApi::class, ExperimentalTime::class)

package com.helpquest.quests.presentation.quest_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.helpquest.core.designsystem.components.containers_layouts.DynamicRoundedCornerColumn
import com.helpquest.core.designsystem.components.containers_layouts.SnackbarScaffold
import com.helpquest.core.designsystem.components.generic.GenericPageHeaderSection
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.domain.models.Category
import com.helpquest.core.presentation.modelsUi.Location
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.ObserveAsEvents
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.clearFocusOnTap
import com.helpquest.core.presentation.util.currentDeviceConfiguration
import com.helpquest.quests.domain.models.QuestActivity
import com.helpquest.quests.domain.models.QuestActivityStatus
import com.helpquest.quests.domain.models.QuestStatus
import com.helpquest.quests.presentation.model.ActivityListUiElement
import com.helpquest.quests.presentation.model.QuestUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun QuestDetailRoot(
    questId: String?,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onQuestMembersClick: () -> Unit,
    viewModel: QuestDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            QuestDetailEvent.OnQuestLeftOrDeleted -> onBack()
            is QuestDetailEvent.OnError -> {
                snackbarState.showSnackbar(
                    event.error.asStringAsync()
                )
            }
            QuestDetailEvent.OnNewActivity -> {
                // TODO: Auto scroll to bottom
            }
        }
    }
    LaunchedEffect(questId) {
        viewModel.onAction(QuestDetailAction.OnSelectQuest(questId))
    }

    BackHandler(
        enabled = showBackButton
    ) {
        viewModel.onAction(QuestDetailAction.OnSelectQuest(null))
        onBack()
    }

    QuestDetailScreen(
        state = state,
        showBackButton = showBackButton,
        snackbarState = snackbarState,
        onAction = { action ->
            when (action) {
                is QuestDetailAction.OnQuestMembersClick -> onQuestMembersClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun QuestDetailScreen(
    state: QuestDetailState,
    showBackButton: Boolean,
    snackbarState: SnackbarHostState,
    onAction: (QuestDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val activityListState = rememberLazyListState()

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (state.questUi == null) {
                        //TODO EmptyListSection
                    } else {
                        GenericPageHeaderSection {
                            //TODO HEADER
//                            QuestDetailHeader(
//                                questUi = state.questUi,
//                                isBackVisible = showBackButton,
//                                isQuestOptionsDropDownOpen = state.isQuestOptionsOpen,
//                                onQuestOptionsClick = {
//                                    onAction(QuestDetailAction.OnQuestDetailsOptionsClick)
//                                },
//                                onDismissQuestOptions = {
//                                    onAction(QuestDetailAction.OnDismissQuestOptions)
//                                },
//                                onManageQuestClick = {
//                                    onAction(QuestDetailAction.OnQuestMembersClick)
//                                },
//                                onLeaveQuestClick = {
//                                    onAction(QuestDetailAction.OnLeaveQuestClick)
//                                },
//                                onDeleteQuestClick = {
//                                    onAction(QuestDetailAction.OnDeleteQuestClick)
//                                },
//                                onBackClick = {
//                                    onAction(QuestDetailAction.OnBackClick)
//                                },
//                                modifier = Modifier.fillMaxWidth()
//                            )
                        }

                        //TODO BODY
//                        ActivityList(
//                            activities = state.activities,
//                            activityWithOpenMenu = state.activityWithOpenMenu,
//                            listState = activityListState,
//                            onActivityLongClick = { activity ->
//                                onAction(QuestDetailAction.OnActivityLongClick(activity))
//                            },
//                            onDismissActivityMenu = {
//                                onAction(QuestDetailAction.OnDismissActivityMenu)
//                            },
//                            onDeleteActivityClick = { activity ->
//                                onAction(QuestDetailAction.OnDeleteActivityClick(activity))
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .weight(1f)
//                        )
                        //TODO IF the user is in the list of the participants -> SHOW THE ADD ACTIVITY BUTTON that brings to the AddActivity DialogSheet
                    }
                }

            }
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun QuestDetailScreenEmptyLightPreview() {
    HelpQuestTheme {
        QuestDetailScreen(
            state = QuestDetailState(),
            showBackButton = true,
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
private fun QuestDetailScreenEmptyDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        QuestDetailScreen(
            state = QuestDetailState(),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
private fun QuestDetailScreenActivitysLightPreview() {
    HelpQuestTheme {
        QuestDetailScreen(
            state = QuestDetailState(
                questUi = QuestUi(
                    questId = "1",
                    participants = listOf(
                        ParticipantUi(
                            id = "1",
                            username = "Philipp",
                            initials = "PH",
                        ),
                        ParticipantUi(
                            id = "2",
                            username = "Cinderella",
                            initials = "CI",
                        ),
                        ParticipantUi(
                            id = "3",
                            username = "Josh",
                            initials = "JO",
                        )
                    ),
                    questTitle = "First Quest",
                    questDescription = "the description of this quest",
                    questCategory = Category.TECHNOLOGY,
                    questStatus = QuestStatus.IN_REFINEMENT,
                    questCreatorId = "1",
                    lastActivity = QuestActivity(
                        activityId = "1",
                        questId = "1",
                        creatorId = "1",
                        actorId = "1",
                        content = "This is a last quest activity that was created by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        activityStatus = QuestActivityStatus.IN_PROGRESS,
                        startActivityAt = Clock.System.now(),
                    ),
                    createdAt = Clock.System.now(),
                    location = Location(0.0, 0.0)
                ),
                activities = (1..20).map {
                    ActivityListUiElement.ActivityItem(
                        id = Uuid.random().toString(),
                        content = "Hello world!",
                        creator = ParticipantUi(
                            id = Uuid.random().toString(),
                            username = "John",
                            initials = "JO"
                        ),
                        actor = ParticipantUi(
                            id = Uuid.random().toString(),
                            username = "John",
                            initials = "JO"
                        ),
                        activityStatus = QuestActivityStatus.IN_PROGRESS,
                        formattedStartTime = UiText.DynamicString("Friday, Aug 20"),
                    )
                }
            ),
            showBackButton = true,
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
private fun QuestDetailScreenMessagesDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        QuestDetailScreen(
            state = QuestDetailState(
                questUi = QuestUi(
                    questId = "1",
                    participants = listOf(
                        ParticipantUi(
                            id = "1",
                            username = "Philipp",
                            initials = "PH",
                        ),
                        ParticipantUi(
                            id = "2",
                            username = "Cinderella",
                            initials = "CI",
                        ),
                        ParticipantUi(
                            id = "3",
                            username = "Josh",
                            initials = "JO",
                        )
                    ),
                    questTitle = "First Quest",
                    questDescription = "the description of this quest",
                    questCategory = Category.TECHNOLOGY,
                    questStatus = QuestStatus.IN_REFINEMENT,
                    questCreatorId = "1",
                    lastActivity = QuestActivity(
                        activityId = "1",
                        questId = "1",
                        creatorId = "1",
                        actorId = "1",
                        content = "This is a last quest activity that was created by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        activityStatus = QuestActivityStatus.IN_PROGRESS,
                        startActivityAt = Clock.System.now(),
                    ),
                    createdAt = Clock.System.now(),
                    location = Location(0.0, 0.0)

                ),
                activities = (1..20).map {
                    ActivityListUiElement.ActivityItem(
                        id = Uuid.random().toString(),
                        content = "Hello world!",
                        creator = ParticipantUi(
                            id = Uuid.random().toString(),
                            username = "John",
                            initials = "JO"
                        ),
                        actor = ParticipantUi(
                            id = Uuid.random().toString(),
                            username = "John",
                            initials = "JO"
                        ),
                        activityStatus = QuestActivityStatus.IN_PROGRESS,
                        formattedStartTime = UiText.DynamicString("Friday, Aug 20"),
                    )
                }
            ),
            showBackButton = true,
            snackbarState = remember { SnackbarHostState() },
            onAction = {}
        )
    }
}