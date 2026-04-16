package com.helpquest.quest.presentation.quest_details

import com.helpquest.quest.presentation.model.ActivityListUiElement


sealed interface QuestDetailAction {
    data object OnAddActivityClick : QuestDetailAction
    data class OnSelectQuest(val questId: String?) : QuestDetailAction
    data class OnDeleteActivityClick(val activity: ActivityListUiElement.ActivityItem) :
        QuestDetailAction

    data class OnActivityLongClick(val activity: ActivityListUiElement.ActivityItem) :
        QuestDetailAction

    data object OnDismissActivityMenu : QuestDetailAction
    data object OnBackClick : QuestDetailAction
    data object OnQuestDetailsOptionsClick : QuestDetailAction
    data object OnQuestMembersClick : QuestDetailAction
    data object OnDismissQuestOptions : QuestDetailAction
}