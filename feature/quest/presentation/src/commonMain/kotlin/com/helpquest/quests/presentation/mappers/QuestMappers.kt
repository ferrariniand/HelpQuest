package com.helpquest.quests.presentation.mappers

import com.helpquest.core.presentation.mappers.toLocation
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.UiText
import com.helpquest.core.presentation.util.toPlace
import com.helpquest.quests.domain.models.Quest
import com.helpquest.quests.presentation.model.QuestListUiElement
import com.helpquest.quests.presentation.model.QuestUi

fun List<Quest>.toQuestUiList(): List<QuestUi> {
    return this
        .sortedByDescending { it.createdAt }
        .map { it.toQuestUi() }
}

fun List<Quest>.toQuestUiListWithSeparators(): List<QuestListUiElement> {
    return this
        .sortedByDescending { it.createdAt }
        //TODO could be grouped by place (separator created from the location and referring to the town/city/area)
        .groupBy {
            it.location.toPlace()
        }
        .flatMap { (place, quests) ->
            listOf(
                QuestListUiElement.PlaceSeparator(
                    id = place, //TODO Maybe should be a different element
                    place = UiText.DynamicString(place)
                )
            ) + quests.map { it.toQuestItem() }
        }
}

fun Quest.toQuestUi(): QuestUi {
    return QuestUi(
        questId = questId,
        questTitle = questTitle,
        questDescription = questDescription,
        questCreatorId = questCreatorId,
        createdAt = createdAt,
        location = location.toLocation(),
        questCategory = questCategory,
        participants = participants.map { it.toParticipantUi() },
        questStatus = questStatus,
        lastActivity = lastActivity
    )
}

fun Quest.toQuestItem(): QuestListUiElement.QuestItem {
    return QuestListUiElement.QuestItem(
        id = questId,
        quest = this.toQuestUi()
    )
}