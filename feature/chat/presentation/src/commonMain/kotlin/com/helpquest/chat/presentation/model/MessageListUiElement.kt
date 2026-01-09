package com.helpquest.chat.presentation.model

import com.helpquest.chat.domain.models.ChatMessageDeliveryStatus
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText


sealed class MessageListUiElement(open val id: String) {
    data class LocalUserMessage(
        override val id: String,
        val content: String,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val formattedSentTime: UiText
    ) : MessageListUiElement(id = id)

    data class OtherUserMessage(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ParticipantUi
    ) : MessageListUiElement(id = id)

    data class DateSeparator(
        override val id: String,
        val date: UiText,
    ) : MessageListUiElement(id = id)
}