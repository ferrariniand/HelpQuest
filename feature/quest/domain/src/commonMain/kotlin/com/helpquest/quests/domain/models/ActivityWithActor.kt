package com.helpquest.quests.domain.models

import com.helpquest.core.domain.models.Participant

data class ActivityWithActor(
    val activity: QuestActivity,
    val actor: Participant,
    val activityStatus: QuestActivityStatus?
)
