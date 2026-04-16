package com.helpquest.quest.domain.models

data class QuestInfo(
    val quest: Quest,
    val activities: List<ActivityWithCreator>
)
