package com.helpquest.quests.domain.service

import com.helpquest.core.domain.service.ConnectionClient
import com.helpquest.core.domain.util.ConnectionError
import com.helpquest.core.domain.util.EmptyResult
import com.helpquest.quests.domain.models.QuestActivity
import kotlinx.coroutines.flow.Flow

interface QuestConnectionClient : ConnectionClient {
    val questActivities: Flow<QuestActivity>
    suspend fun addQuestActivity(activity: QuestActivity): EmptyResult<ConnectionError>
}