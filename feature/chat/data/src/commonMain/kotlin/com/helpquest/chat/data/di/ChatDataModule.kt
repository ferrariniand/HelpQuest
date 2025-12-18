package com.helpquest.chat.data.di


import com.helpquest.chat.data.service.ChatWebSocketConnectionClient
import com.helpquest.chat.data.service.OfflineFirstChatRepository
import com.helpquest.chat.data.service.OfflineFirstMessageRepository
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatRepository
import com.helpquest.chat.domain.service.MessageRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chatDataModule = module {
    includes(variantChatDataModule)
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    singleOf(::OfflineFirstMessageRepository) bind MessageRepository::class
    singleOf(::ChatWebSocketConnectionClient) bind ChatConnectionClient::class
}