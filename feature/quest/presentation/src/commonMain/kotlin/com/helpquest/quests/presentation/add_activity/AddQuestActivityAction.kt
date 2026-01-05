package com.helpquest.quests.presentation.add_activity

import com.helpquest.quests.presentation.model.ActivityListUiElement

sealed interface AddQuestActivityAction {
    data object OnCreateActivityClick : AddQuestActivityAction
    data class OnRetryClick(val activity: ActivityListUiElement.ActivityItem) :
        AddQuestActivityAction
    data object OnCloseClick : AddQuestActivityAction
}