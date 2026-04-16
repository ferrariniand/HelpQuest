package com.helpquest.chat.data.di


import com.helpquest.chat.data.service.OfflineFirstChatRepository
import com.helpquest.chat.data.service.OfflineFirstMessageRepository
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.MessageRepository
import com.helpquest.feature.chat.data.BuildKonfig
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformChatDataModule: Module

val chatDataModule = module {
    //TODO: CHANGE WITH BUILD FLAVOR CONFIGURATIONS WHEN WILL BE AVAILABLE
    if (BuildKonfig.USE_MOCK_SERVER) {
        includes(variantChatDataMockModule)
    } else {
        includes(variantChatDataModule)
    }
    includes(platformChatDataModule)
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    singleOf(::OfflineFirstMessageRepository) bind MessageRepository::class
}