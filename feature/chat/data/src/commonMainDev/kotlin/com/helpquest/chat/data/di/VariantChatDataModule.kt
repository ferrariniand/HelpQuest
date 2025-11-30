package com.helpquest.chat.data.di


import com.helpquest.chat.data.service.KtorChatParticipantService
import com.helpquest.chat.data.service.KtorChatService
import com.helpquest.chat.domain.service.ChatParticipantService
import com.helpquest.chat.domain.service.ChatService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val variantChatDataModule = module {

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
}