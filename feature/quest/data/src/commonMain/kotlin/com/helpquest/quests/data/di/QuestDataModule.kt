package com.helpquest.quests.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.helpquest.quest.database.QuestLogDatabaseFactory
import com.helpquest.quests.data.service.KtorQuestService
import com.helpquest.quests.data.service.OfflineFirstQuestRepository
import com.helpquest.quests.domain.service.QuestRepository
import com.helpquest.quests.domain.service.QuestService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformQuestDataModule: Module

val questDataModule = module {
    includes(platformQuestDataModule)
    singleOf(::KtorQuestService) bind QuestService::class
    singleOf(::OfflineFirstQuestRepository) bind QuestRepository::class
    single {
        get<QuestLogDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}