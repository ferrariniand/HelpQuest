package com.helpquest.notification

import com.helpquest.chat.data.notification.ChatDesktopNotifier
import com.helpquest.core.domain.notification.DesktopNotifier
import com.helpquest.core.domain.notification.NotificationPayload
import com.helpquest.quest.data.notification.QuestDesktopNotifier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.merge

class ApplicationDesktopNotifier(
    private val chatDesktopNotifier: ChatDesktopNotifier,
    private val questDesktopNotifier: QuestDesktopNotifier,
) : DesktopNotifier {

    override fun observeNewNotifications(): Flow<NotificationPayload> {
        return merge(
            chatDesktopNotifier.observeNewNotifications(),
            questDesktopNotifier.observeNewNotifications()
        ).buffer(capacity = Channel.UNLIMITED)
    }
}