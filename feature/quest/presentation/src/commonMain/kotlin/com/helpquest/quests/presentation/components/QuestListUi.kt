package com.helpquest.quests.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.for_scrollables.EmptyListSection
import com.helpquest.quests.presentation.model.QuestListUiElement
import helpquest.core.designsystem.generated.resources.empty_list
import helpquest.feature.quest.presentation.generated.resources.Res
import helpquest.feature.quest.presentation.generated.resources.no_quests
import helpquest.feature.quest.presentation.generated.resources.no_quests_subtitle
import helpquest.feature.quest.presentation.generated.resources.retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun QuestListUi(
    questListUiElements: List<QuestListUiElement>,
    selectedQuestId: String?,
    listState: LazyListState,
    isLoading: Boolean,
    isPaginationLoading: Boolean,
    paginationError: String?,
    onSelectQuest: (questId: String?) -> Unit,
    onRetryPaginationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }

        questListUiElements.isEmpty() -> {
            Box(
                modifier = modifier
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyListSection(
                    title = stringResource(Res.string.no_quests),
                    description = stringResource(Res.string.no_quests_subtitle),
                    icon = painterResource(DesignSystemRes.drawable.empty_list),
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = questListUiElements,
                    key = { it.id }
                ) { questListUiElement ->
                    QuestListItemUi(
                        questListUiElement = questListUiElement,
                        isSelected = questListUiElement.id == selectedQuestId,
                        onSelect = {
                            onSelectQuest(questListUiElement.id)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()

                    )

                }

                when {
                    isPaginationLoading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    paginationError != null -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                HelpQuestButton(
                                    text = stringResource(Res.string.retry),
                                    onClick = onRetryPaginationClick,
                                    style = HelpQuestButtonStyle.SECONDARY
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = paginationError,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}