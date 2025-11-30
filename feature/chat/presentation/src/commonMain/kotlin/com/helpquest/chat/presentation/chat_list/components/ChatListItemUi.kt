@file:OptIn(ExperimentalTime::class)

package com.helpquest.chat.presentation.chat_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.helpquest.chat.domain.models.ChatMessage
import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.chat.presentation.components.ChatItemHeaderRow
import com.helpquest.chat.presentation.model.ChatUi
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.designsystem.theme.extended
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun ChatListItemUi(
    chat: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val isGroupChat = chat.otherParticipants.size > 1
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.extended.surfaceLower
                }
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatItemHeaderRow(
                chat = chat,
                isGroupChat = isGroupChat,
                modifier = Modifier.fillMaxWidth()
            )

            if (chat.lastMessage != null) {
                val previewMessage = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                        )
                    ) {
                        append(chat.lastMessageSenderUsername + ": ")
                    }
                    append(chat.lastMessage.content)
                }
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(if (isSelected) 1f else 0f)
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}


@Composable
@Preview
fun ChatListItemUiLightPreview() {
    HelpQuestTheme {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth(),
            chat = ChatUi(
                id = "1",
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                otherParticipants = listOf(
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
                lastMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "This is a last chat message that was sent by Philipp " +
                            "and goes over multiple lines to showcase the ellipsis",
                    createdAt = Clock.System.now(),
                    senderId = "1",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    deliveredAt = Clock.System.now()
                ),
                lastMessageSenderUsername = "Philipp"
            )
        )
    }
}

@Composable
@Preview
fun ChatListItemUiDarkPreview() {
    HelpQuestTheme(darkTheme = true) {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth(),
            chat = ChatUi(
                id = "1",
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                otherParticipants = listOf(
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
                lastMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "This is a last chat message that was sent by Philipp " +
                            "and goes over multiple lines to showcase the ellipsis",
                    createdAt = Clock.System.now(),
                    senderId = "1",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    deliveredAt = Clock.System.now()
                ),
                lastMessageSenderUsername = "Philipp"
            )
        )
    }
}

@Composable
@Preview
fun ChatListItemUiLightSingleParticipantPreview() {
    HelpQuestTheme {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth(),
            chat = ChatUi(
                id = "1",
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                otherParticipants = listOf(
                    ParticipantUi(
                        id = "2",
                        username = "Cinderella",
                        initials = "CI",
                    ),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "This is a last chat message that was sent by Philipp " +
                            "and goes over multiple lines to showcase the ellipsis",
                    createdAt = Clock.System.now(),
                    senderId = "1",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    deliveredAt = Clock.System.now()
                ),
                lastMessageSenderUsername = "Philipp"
            )
        )
    }
}

@Composable
@Preview
fun ChatListItemUiDarkSingleParticipantPreview() {
    HelpQuestTheme(darkTheme = true) {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth(),
            chat = ChatUi(
                id = "1",
                localParticipant = ParticipantUi(
                    id = "1",
                    username = "Philipp",
                    initials = "PH",
                ),
                otherParticipants = listOf(
                    ParticipantUi(
                        id = "2",
                        username = "Cinderella",
                        initials = "CI",
                    ),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "This is a last chat message that was sent by Philipp " +
                            "and goes over multiple lines to showcase the ellipsis",
                    createdAt = Clock.System.now(),
                    senderId = "1",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    deliveredAt = Clock.System.now()
                ),
                lastMessageSenderUsername = "Philipp"
            )
        )
    }
}