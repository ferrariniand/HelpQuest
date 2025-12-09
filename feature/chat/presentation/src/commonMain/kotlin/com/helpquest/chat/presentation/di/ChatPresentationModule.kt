package com.helpquest.chat.presentation.di

import com.helpquest.chat.presentation.chat_details.ChatDetailViewModel
import com.helpquest.chat.presentation.chat_list.ChatListViewModel
import com.helpquest.chat.presentation.chat_list_detail.ChatListDetailViewModel
import com.helpquest.chat.presentation.create_manage_chat.CreateChatViewModel
import com.helpquest.chat.presentation.create_manage_chat.ManageChatState
import com.helpquest.chat.presentation.create_manage_chat.ManageChatViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    single<ManageChatState> { ManageChatState() }

    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
}