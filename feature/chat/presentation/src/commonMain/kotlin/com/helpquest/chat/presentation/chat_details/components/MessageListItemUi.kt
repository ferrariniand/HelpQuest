package com.helpquest.chat.presentation.chat_details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.designsystem.components.generic.HelpQuestHorizontalDividerWithTitle
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageListItemUi(
    messageListUiElement: MessageListUiElement,
    onMessageLongClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    onRetryClick: (MessageListUiElement.LocalUserMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when (messageListUiElement) {
            is MessageListUiElement.DateSeparator -> {
                DateSeparatorItem(
                    date = messageListUiElement.date.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is MessageListUiElement.LocalUserMessage -> {
                LocalUserMessageItem(
                    message = messageListUiElement,
                    onMessageLongClick = {
                        onMessageLongClick(messageListUiElement)
                    },
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = {
                        onDeleteClick(messageListUiElement)
                    },
                    onRetryClick = {
                        onRetryClick(messageListUiElement)
                    }
                )
            }

            is MessageListUiElement.OtherUserMessage -> {
                OtherUserMessageItem(
                    message = messageListUiElement,
                )
            }
        }
    }
}


@Composable
private fun DateSeparatorItem(
    date: String,
    modifier: Modifier = Modifier
) {
    HelpQuestHorizontalDividerWithTitle(
        date,
        modifier
    )
}

@Composable
@Preview
fun MessageListItemLocalMessageUiPreview() {
    HelpQuestTheme {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                isMenuOpen = true,
                formattedSentTime = UiText.DynamicString("Friday 2:20pm")
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
@Preview
fun MessageListItemLocalMessageRetryUiPreview() {
    HelpQuestTheme {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                isMenuOpen = false,
                formattedSentTime = UiText.DynamicString("Friday 2:20pm")
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview
fun MessageListItemOtherMessageUiPreview() {
    HelpQuestTheme {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.OtherUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                formattedSentTime = UiText.DynamicString("Friday 2:20pm"),
                sender = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH"
                )
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun MessageListItemLocalMessageUiDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                isMenuOpen = true,
                formattedSentTime = UiText.DynamicString("Friday 2:20pm")
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun MessageListItemLocalMessageRetryUiDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                isMenuOpen = false,
                formattedSentTime = UiText.DynamicString("Friday 2:20pm")
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1
)
fun MessageListItemOtherMessageUiDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        MessageListItemUi(
            messageListUiElement = MessageListUiElement.OtherUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                formattedSentTime = UiText.DynamicString("Friday 2:20pm"),
                sender = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH"
                )
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}