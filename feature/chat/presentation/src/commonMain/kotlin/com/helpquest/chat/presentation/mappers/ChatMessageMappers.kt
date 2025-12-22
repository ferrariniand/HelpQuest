package com.helpquest.chat.presentation.mappers

import com.helpquest.chat.domain.models.MessageWithSender
import com.helpquest.chat.presentation.model.MessageListUiElement
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.DateUtils


fun MessageWithSender.toMessageListUiElement(
    localUserId: String,
): MessageListUiElement {
    val isFromLocalUser = this.sender.userId == localUserId
    return if (isFromLocalUser) {
        MessageListUiElement.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            isMenuOpen = false,
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