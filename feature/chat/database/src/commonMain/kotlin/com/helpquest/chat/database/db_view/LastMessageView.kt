package com.helpquest.chat.database.db_view

import androidx.room.DatabaseView

//allow to update the info of a DB using the info of another DB
// (like a projection of a specific element in another DB by defining a query)
@DatabaseView(
    viewName = "last_message_view_per_chat",
    value = """
        SELECT m1.*
        FROM chatmessageentity m1
        JOIN (
            SELECT chatId, MAX(timestamp) AS max_timestamp
            FROM chatmessageentity
            GROUP BY chatId
            LIMIT 1
        ) m2 ON m1.chatId = m2.chatId AND m1.timestamp = m2.max_timestamp
    """
)
data class LastMessageView(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val deliveryStatus: String,
    val deliveryStatusTimestamp: Long
)