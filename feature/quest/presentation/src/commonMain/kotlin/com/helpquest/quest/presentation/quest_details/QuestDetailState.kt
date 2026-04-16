package com.helpquest.quest.presentation.quest_details

import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quest.presentation.model.ActivityListUiElement
import com.helpquest.quest.presentation.model.QuestUi

data class QuestDetailState(
    val questUi: QuestUi? = null,
    val localParticipant: ParticipantUi? = null,
    val isLoading: Boolean = false,
    val activities: List<ActivityListUiElement> = emptyList(),
    val activityWithOpenMenu: ActivityListUiElement.ActivityItem? = null,
    val error: UiText? = null,
    val bannerState: BannerState = BannerState(),
    val isQuestOptionsOpen: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)