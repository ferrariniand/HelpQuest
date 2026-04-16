package com.helpquest.quest.presentation.add_activity

import com.helpquest.quest.presentation.model.ActivityListUiElement

sealed interface AddQuestActivityAction {
    data object OnCreateActivityClick : AddQuestActivityAction
    data class OnRetryClick(val activity: ActivityListUiElement.ActivityItem) :
        AddQuestActivityAction

    data object OnCloseClick : AddQuestActivityAction
}