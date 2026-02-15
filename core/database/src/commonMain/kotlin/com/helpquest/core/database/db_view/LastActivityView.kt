package com.helpquest.core.database.db_view

import androidx.room.DatabaseView

//allow to update the info of a DB using the info of another DB
// (like a projection of a specific element in another DB by defining a query)
@DatabaseView(
    viewName = "last_activity_view_per_quest",
    value = """
        SELECT m1.*, p.username AS actorUsername
        FROM questactivityentity m1
        JOIN (
            SELECT questId, MAX(startTimestamp) AS max_start_timestamp
            FROM questactivityentity
            GROUP BY questId
            LIMIT 1
        ) m2 ON m1.questId = m2.questId AND m1.startTimestamp = m2.max_start_timestamp
        LEFT JOIN participantentity p ON m1.actorId = p.userId
    """
)
data class LastActivityView(
    val activityId: String,
    val questId: String,
    val creatorId: String,
    val actorId: String?,
    val actorUsername: String?,
    val content: String,
    val activityStatus: String,
    val startTimestamp: Long,
    val lastActivityUpdateTimestamp: Long,
    val endTimestamp: Long?
)