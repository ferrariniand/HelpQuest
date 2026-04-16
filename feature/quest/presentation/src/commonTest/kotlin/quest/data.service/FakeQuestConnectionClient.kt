@file:OptIn(ExperimentalTime::class)

package quest.data.service

import com.helpquest.core.domain.util.ConnectionState
import com.helpquest.quest.domain.models.QuestActivity
import com.helpquest.quest.domain.service.QuestConnectionClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.ExperimentalTime

class FakeQuestConnectionClient : QuestConnectionClient {

    override val questActivities: Flow<QuestActivity> = flowOf<QuestActivity>()

    var conState = ConnectionState.DISCONNECTED
    override val connectionState = MutableStateFlow(conState)
}