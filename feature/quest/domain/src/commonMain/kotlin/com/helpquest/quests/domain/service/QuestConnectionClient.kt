package com.helpquest.quests.domain.service

import com.helpquest.core.domain.service.ConnectionClient
import com.helpquest.quests.domain.models.QuestActivity
import kotlinx.coroutines.flow.Flow

interface QuestConnectionClient : ConnectionClient {
    val questActivities: Flow<QuestActivity>
}