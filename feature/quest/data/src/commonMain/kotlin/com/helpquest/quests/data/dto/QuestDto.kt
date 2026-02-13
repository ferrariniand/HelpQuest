package com.helpquest.quests.data.dto

import com.helpquest.core.data.dto.GeoLocationDto
import com.helpquest.core.data.dto.ParticipantDto

data class QuestDto(
    val questId: String,
    val questTitle: String,
    val questDescription: String,
    val questCreatorId: String,
    val createdAt: String,
    val location: GeoLocationDto,
    val participants: List<ParticipantDto>,
    val questCategory: String?,
    val questStatus: String?,
    val lastActivity: QuestActivityDto?,
    val lastUpdateAt: String
)

object QuestDtoConstants {
    const val PAGE_SIZE = 20
}