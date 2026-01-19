package com.helpquest.chat.presentation.mappers

import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.DateUtils
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun List<MessageWithSender>.toMessageListUi(localUserId: String): List<MessageListUiElement> {
    return this
        .sortedByDescending { it.message.createdAt }
        .groupBy {
            it.message.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
        .flatMap { (date, messages) ->
            messages.map { it.toMessageListUiElement(localUserId) } + MessageListUiElement.DateSeparator(
                id = date.toString(),
                date = DateUtils.formatDateSeparator(date)
            )
        }
}

fun MessageWithSender.toMessageListUiElement(
    localUserId: String,
): MessageListUiElement {
    val isFromLocalUser = this.sender.userId == localUserId
    return if (isFromLocalUser) {
        MessageListUiElement.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            formattedSentTime = DateUtils.formatDateTime(instant = message.createdAt)
        )
    } else {
        MessageListUiElement.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = DateUtils.formatDateTime(instant = message.createdAt),
            sender = sender.toParticipantUi()
        )
    }
}