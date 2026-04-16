package com.helpquest.chat.presentation.chat_details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.designsystem.components.avatar.HelpQuestAvatar
import com.helpquest.core.designsystem.components.chat.HelpQuestChatBubble
import com.helpquest.core.designsystem.components.chat.TrianglePosition
import com.helpquest.core.designsystem.components.chat.getChatBubbleColorForUser
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText


@Composable
fun OtherUserMessageItem(
    message: MessageListUiElement.OtherUserMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 26.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        HelpQuestAvatar(
            displayText = message.sender.initials,
            userImageUrl = message.sender.imageUrl,
            showUserIdentity = message.sender.showParticipantIdentity,
            classImageUrl = message.sender.classImageUrl,
            showClass = true
        )
        HelpQuestChatBubble(
            messageContent = message.content,
            sender = message.sender.username,
            trianglePosition = TrianglePosition.LEFT,
            color = getChatBubbleColorForUser(message.sender.id),
            formattedDateTime = message.formattedSentTime.asString()
        )
    }
}

@Composable
@Preview
fun OtherUserMessageItemPreview() {
    HelpQuestTheme {
        OtherUserMessageItem(
            message = MessageListUiElement.OtherUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                formattedSentTime = UiText.DynamicString("Friday 2:20pm"),
                sender = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH"
                )
            )

        )
    }
}