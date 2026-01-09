package com.helpquest.quests.presentation.mappers

import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.util.DateUtils
import com.helpquest.quests.domain.models.ActivityWithCreator
import com.helpquest.quests.presentation.model.ActivityListUiElement


fun ActivityWithCreator.toActivityListUiElement(): ActivityListUiElement {
    return ActivityListUiElement.ActivityItem(
        id = activity.activityId,
        content = activity.content,
        creator = creator.toParticipantUi(),
        //TODO HOW TO GET ACTOR???
        actor = null,
        activityStatus = activity.activityStatus,
        isMenuOpen = false,
        formattedStartTime = DateUtils.formatDateTime(instant = activity.startActivityAt),
        formattedEndTime = activity.endActivityAt?.let {
            DateUtils.formatDateTime(instant = it)
        },
    )
}