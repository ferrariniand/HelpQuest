package com.helpquest.quests.presentation.di

import com.helpquest.quests.presentation.quest_log.QuestLogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val questPresentationModule = module {
    viewModelOf(::QuestLogViewModel)
}