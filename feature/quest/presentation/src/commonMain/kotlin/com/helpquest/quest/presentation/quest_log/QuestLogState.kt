package com.helpquest.quest.presentation.quest_log

import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quest.presentation.model.QuestUi

data class QuestLogState(
    val quests: List<QuestUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ParticipantUi? = null,
    val selectedQuestId: String? = null,
    val isQuestOptionsOpen: Boolean = false,
    val isLoading: Boolean = false
)