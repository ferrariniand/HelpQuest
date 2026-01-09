package com.helpquest.quests.domain.models

import com.helpquest.core.domain.models.Participant

data class ActivityWithCreator(
    val activity: QuestActivity,
    val creator: Participant,
    val activityStatus: QuestActivityStatus?
)
