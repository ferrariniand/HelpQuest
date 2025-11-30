package com.helpquest.quests.domain.service

import com.helpquest.core.domain.util.DataError
import com.helpquest.core.domain.util.Result
import com.helpquest.quests.domain.models.Quest

interface QuestLogService {
    suspend fun getQuestLog(): Result<List<Quest>, DataError.Remote>
}
