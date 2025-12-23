package com.helpquest.quests.presentation.add_activity

sealed interface AddQuestActivityAction {
    data object OnCreateActivityClick : AddQuestActivityAction
    data object OnCloseClick : AddQuestActivityAction
}