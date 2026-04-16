package com.helpquest.quest.domain.service

import com.helpquest.core.domain.service.ConnectionClient
import com.helpquest.quest.domain.models.QuestActivity
import kotlinx.coroutines.flow.Flow

interface QuestConnectionClient : ConnectionClient {
    val questActivities: Flow<QuestActivity>
}