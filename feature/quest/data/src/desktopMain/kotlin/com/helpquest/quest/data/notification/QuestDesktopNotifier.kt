package com.helpquest.quest.data.notification


import com.helpquest.core.domain.auth.SessionStorage
import com.helpquest.core.domain.notification.DesktopNotifier
import com.helpquest.core.domain.notification.NotificationPayload
import com.helpquest.quest.domain.service.QuestConnectionClient
import com.helpquest.quest.domain.service.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class QuestDesktopNotifier(
    private val questConnectionClient: QuestConnectionClient,
    private val sessionStorage: SessionStorage,
    private val questRepository: QuestRepository
) : DesktopNotifier {

    override fun observeNewNotifications(): Flow<NotificationPayload> {
        return combine(
            questConnectionClient.questActivities,
            sessionStorage.observeAuthInfo()
        ) { questActivity, authInfo ->
            val currentUserId = authInfo?.user?.id
            if (questActivity.actorId != currentUserId) {
                questActivity
            } else null
        }
            .filterNotNull()
            .distinctUntilChangedBy { it.activityId }
            .map { activity ->
                val questInfo = questRepository.getQuestInfoById(activity.questId).firstOrNull()

                val actorName = questInfo?.quest?.participants?.find {
                    it.userId == activity.actorId
                }?.username

                NotificationPayload(
                    title = "Quest: ${questInfo?.quest?.questTitle ?: "Unknown"}",
                    message = "${actorName.let { "$it: " }}${activity.content}"
                )
            }
    }
}