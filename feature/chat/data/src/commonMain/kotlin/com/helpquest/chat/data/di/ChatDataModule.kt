package com.helpquest.chat.data.di


import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.helpquest.chat.data.service.OfflineFirstChatRepository
import com.helpquest.chat.database.ChatDatabaseFactory
import com.helpquest.chat.domain.service.ChatRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)
    includes(variantChatDataModule)
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    single {
        get<ChatDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}