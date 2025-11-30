package com.helpquest.chat.data.di

import com.helpquest.chat.database.ChatDatabaseFactory
import org.koin.dsl.module

actual val platformChatDataModule = module {
    single { ChatDatabaseFactory() }
}