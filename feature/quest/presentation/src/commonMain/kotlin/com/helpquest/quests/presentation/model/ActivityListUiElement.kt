package com.helpquest.quests.presentation.model

import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quests.domain.models.QuestActivityStatus


sealed class ActivityListUiElement(open val id: String) {
    data class ActivityItem(
        override val id: String,
        val content: String,
        val creator: ParticipantUi,
        val actor: ParticipantUi?,
        val activityStatus: QuestActivityStatus,
        val isMenuOpen: Boolean = false,
        val formattedStartTime: UiText,
        val formattedEndTime: UiText? = null,
    ) : ActivityListUiElement(id = id)


    data class DateSeparator(
        override val id: String,
        val date: UiText,
    ) : ActivityListUiElement(id = id)
}