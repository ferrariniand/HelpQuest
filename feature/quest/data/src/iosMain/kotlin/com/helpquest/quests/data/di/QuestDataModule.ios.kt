package com.helpquest.quests.data.di

import com.helpquest.quest.database.QuestDatabaseFactory
import org.koin.dsl.module

actual val platformQuestDataModule = module {
    single { QuestDatabaseFactory() }
}