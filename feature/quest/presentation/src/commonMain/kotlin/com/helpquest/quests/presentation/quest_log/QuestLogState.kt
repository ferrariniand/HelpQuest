package com.helpquest.quests.presentation.quest_log

import com.helpquest.core.presentation.modelsUi.ParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.quests.presentation.model.QuestLogItemUi

data class QuestLogState(
    val quests: List<QuestLogItemUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ParticipantUi? = null,
    val selectedQuestId: String? = null,
    val isLoading: Boolean = false
)