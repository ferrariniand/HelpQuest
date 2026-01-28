package com.helpquest.chat.data.di


import com.helpquest.chat.data.service.KtorChatMessageService
import com.helpquest.chat.data.service.KtorChatService
import com.helpquest.chat.data.service.WebSocketChatConnectionClient
import com.helpquest.chat.domain.service.ChatConnectionClient
import com.helpquest.chat.domain.service.ChatMessageService
import com.helpquest.chat.domain.service.ChatService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val variantChatDataModule = module {

    singleOf(::KtorChatService) bind ChatService::class
    singleOf(::KtorChatMessageService) bind ChatMessageService::class
    singleOf(::WebSocketChatConnectionClient) bind ChatConnectionClient::class
}