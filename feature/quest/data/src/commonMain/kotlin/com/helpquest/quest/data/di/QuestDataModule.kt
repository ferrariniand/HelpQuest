package com.helpquest.quest.data.di

import com.helpquest.quest.data.service.KtorQuestActivityService
import com.helpquest.quest.data.service.KtorQuestService
import com.helpquest.quest.data.service.OfflineFirstActivityRepository
import com.helpquest.quest.data.service.OfflineFirstQuestRepository
import com.helpquest.quest.data.service.WebSocketQuestConnectionClient
import com.helpquest.quest.domain.service.ActivityRepository
import com.helpquest.quest.domain.service.QuestActivityService
import com.helpquest.quest.domain.service.QuestConnectionClient
import com.helpquest.quest.domain.service.QuestRepository
import com.helpquest.quest.domain.service.QuestService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformQuestDataModule: Module

val questDataModule = module {
    includes(platformQuestDataModule)
    singleOf(::KtorQuestService) bind QuestService::class
    singleOf(::OfflineFirstQuestRepository) bind QuestRepository::class
    singleOf(::OfflineFirstActivityRepository) bind ActivityRepository::class
    singleOf(::WebSocketQuestConnectionClient) bind QuestConnectionClient::class
    singleOf(::KtorQuestActivityService) bind QuestActivityService::class
}