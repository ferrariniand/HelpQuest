package com.helpquest.quest.domain.models

data class OutgoingNewActivity(
    val questId: String,
    val activityId: String,
    val content: String,
)
