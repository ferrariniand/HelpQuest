package com.helpquest.quests.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateQuestRequest(
    val questTitle: String,
    val questDescription: String,
    val questCategory: String,
    val questCreatorId: String,
)
