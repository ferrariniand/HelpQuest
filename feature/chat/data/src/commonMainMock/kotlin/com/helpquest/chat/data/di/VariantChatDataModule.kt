package com.helpquest.chat.data.di

import com.helpquest.chat.data.service.MockChatService
import com.helpquest.chat.data.service.MockChatMessageService
import com.helpquest.chat.data.service.MockChatResponseElements
import com.helpquest.chat.data.service.MockChatConnectionClient
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatService
import com.helpquest.chat.domain.service.ChatMessageService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val variantChatDataModule = module {

    single { MockChatResponseElements }
    singleOf(::MockChatService) bind ChatService::class
    singleOf(::MockChatMessageService) bind ChatMessageService::class

    singleOf(::MockChatConnectionClient) bind ChatConnectionClient::class

}