package com.helpquest.chat.presentation.chat_details.components

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
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.designsystem.components.buttons.HelpQuestButton
import com.helpquest.core.designsystem.components.buttons.HelpQuestButtonStyle
import com.helpquest.core.designsystem.components.for_scrollables.EmptyListSection
import helpquest.core.designsystem.generated.resources.empty_list
import helpquest.feature.chat.presentation.generated.resources.Res
import helpquest.feature.chat.presentation.generated.resources.no_messages
import helpquest.feature.chat.presentation.generated.resources.no_messages_subtitle
import helpquest.feature.chat.presentation.generated.resources.retry
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import helpquest.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun MessageList(
    messages: List<MessageListUiElement>,
    messageWithOpenMenu: MessageListUiElement.LocalUserMessage?,
    listState: LazyListState,
    isPaginationLoading: Boolean,
    paginationError: String?,
    onMessageLongClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    onMessageRetryClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    onDeleteMessageClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    onRetryPaginationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptyListSection(
                title = stringResource(Res.string.no_messages),
                description = stringResource(Res.string.no_messages_subtitle),
                icon = painterResource(DesignSystemRes.drawable.empty_list),
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(16.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageListItemUi(
                    messageListUiElement = message,
                    messageWithOpenMenu = messageWithOpenMenu,
                    onMessageLongClick = onMessageLongClick,
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = onDeleteMessageClick,
                    onRetryClick = onMessageRetryClick,
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