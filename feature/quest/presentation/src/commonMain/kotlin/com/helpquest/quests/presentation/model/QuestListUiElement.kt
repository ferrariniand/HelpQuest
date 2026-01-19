package com.helpquest.quests.presentation.model

import com.helpquest.core.presentation.util.UiText


sealed class QuestListUiElement(open val id: String) {
    data class QuestItem(
        override val id: String,
        val quest: QuestUi
//TODO take parameters from QuestUi, mabe replace it
    ) : QuestListUiElement(id = id)


    data class PlaceSeparator(
        override val id: String,
        val place: UiText,
    ) : QuestListUiElement(id = id)
}