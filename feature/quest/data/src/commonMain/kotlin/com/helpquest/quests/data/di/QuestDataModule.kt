package com.helpquest.quests.data.di

import com.helpquest.quests.data.service.KtorQuestService
import com.helpquest.quests.data.service.OfflineFirstActivityRepository
import com.helpquest.quests.data.service.OfflineFirstQuestRepository
import com.helpquest.quests.data.service.WebSocketQuestConnectionClient
import com.helpquest.quests.domain.service.ActivityRepository
import com.helpquest.quests.domain.service.QuestConnectionClient
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.domain.service.QuestService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val questDataModule = module {
    singleOf(::KtorQuestService) bind QuestService::class
    singleOf(::OfflineFirstQuestRepository) bind QuestRepository::class
    singleOf(::OfflineFirstActivityRepository) bind ActivityRepository::class
    singleOf(::WebSocketQuestConnectionClient) bind QuestConnectionClient::class
}