package com.helpquest.quest.presentation.di

import com.helpquest.quest.presentation.quest_board.QuestBoardViewModel
import com.helpquest.quest.presentation.quest_board_detail.QuestBoardDetailViewModel
import com.helpquest.quest.presentation.quest_details.QuestDetailViewModel
import com.helpquest.quest.presentation.quest_log.QuestLogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val questPresentationModule = module {
    viewModelOf(::QuestLogViewModel)
    viewModelOf(::QuestBoardViewModel)
    viewModelOf(::QuestBoardDetailViewModel)
    viewModelOf(::QuestDetailViewModel)
}