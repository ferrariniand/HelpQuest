package com.helpquest.quests.presentation.di

import com.helpquest.quests.presentation.quest_board.QuestBoardViewModel
import com.helpquest.quests.presentation.quest_board_detail.QuestBoardDetailViewModel
import com.helpquest.quests.presentation.quest_log.QuestLogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val questPresentationModule = module {
    viewModelOf(::QuestLogViewModel)
    viewModelOf(::QuestBoardViewModel)
    viewModelOf(::QuestBoardDetailViewModel)
}