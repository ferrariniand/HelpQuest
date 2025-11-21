package com.helpquest.chat.presentation.di

import com.helpquest.chat.presentation.chat_details.ChatDetailViewModel
import com.helpquest.chat.presentation.chat_list.ChatListViewModel
import com.helpquest.chat.presentation.chat_list_detail.ChatListDetailViewModel
import com.helpquest.chat.presentation.create_chat.CreateChatState
import com.helpquest.chat.presentation.create_chat.CreateChatViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    single<CreateChatState> { CreateChatState() }

    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatDetailViewModel)
}