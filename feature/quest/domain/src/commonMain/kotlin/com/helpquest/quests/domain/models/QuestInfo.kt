package com.helpquest.quests.domain.models

data class QuestInfo(
    val quest: Quest,
    val activities: List<ActivityWithCreator>
)
