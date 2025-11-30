package com.helpquest.quests.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.helpquest.quest.database.QuestLogDatabaseFactory
import com.helpquest.quests.data.service.KtorQuestBoardService
import com.helpquest.quests.data.service.KtorQuestLogService
import com.helpquest.quests.data.service.OfflineFirstQuestLogRepository
import com.helpquest.quests.domain.service.QuestBoardService
import com.helpquest.quests.domain.service.QuestLogRepository
import com.helpquest.quests.domain.service.QuestLogService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformQuestDataModule: Module

val questDataModule = module {
    includes(platformQuestDataModule)
    singleOf(::KtorQuestBoardService) bind QuestBoardService::class
    singleOf(::KtorQuestLogService) bind QuestLogService::class
    singleOf(::OfflineFirstQuestLogRepository) bind QuestLogRepository::class
    single {
        get<QuestLogDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}