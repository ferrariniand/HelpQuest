package com.helpquest.quests.presentation.quest_details

import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.core.presentation.modelsUi.BannerState
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quests.presentation.model.ActivityListUiElement
import com.helpquest.quests.presentation.model.QuestUi

data class QuestDetailState(
    val questUi: QuestUi? = null,
    val localParticipant: ParticipantUi? = null,
    val isLoading: Boolean = false,
    val activities: List<ActivityListUiElement> = emptyList(),
    val error: UiText? = null,
    val bannerState: BannerState = BannerState(),
    val isQuestOptionsOpen: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)