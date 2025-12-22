package com.helpquest.core.presentation.util

import helpquest.core.presentation.generated.resources.Res
import helpquest.core.presentation.generated.resources.today
import helpquest.core.presentation.generated.resources.yesterday
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

object DateUtils {

    fun formatDate(
        instant: Instant,
        isUsingEuropeDateFormat: Boolean = true,
        clock: Clock = Clock.System
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)
        val todayDate = clock.now().toLocalDateTime(timeZone).date
        val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

        val dateFormat = if (isUsingEuropeDateFormat) europeDateFormat else usDateFormat

        val formattedDate = messageDateTime.format(dateFormat)

        return when (messageDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today)
            yesterdayDate -> UiText.Resource(Res.string.yesterday)
            else -> UiText.DynamicString(formattedDate)
        }
    }

    fun formatTime(
        instant: Instant,
        isUsing24HourFormat: Boolean = true,
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)

        val timeFormat = if (isUsing24HourFormat) timeFormat24H else timeFormat12H

        val formattedTime = messageDateTime.format(timeFormat)

        return UiText.DynamicString(formattedTime)
    }


    fun formatDateTime(
        instant: Instant,
        isUsingEuropeDateFormat: Boolean = true,
        isUsing24HourFormat: Boolean = true,
        clock: Clock = Clock.System
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)
        val todayDate = clock.now().toLocalDateTime(timeZone).date
        val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

        // 1. Choose the date format part
        val dateFormat = if (isUsingEuropeDateFormat) europeDateFormat else usDateFormat

        // 2. Choose the time format part
        val timeFormat = if (isUsing24HourFormat) timeFormat24H else timeFormat12H

        val formattedDate = messageDateTime.format(dateFormat)
        val formattedTime = messageDateTime.format(timeFormat)
        val formattedDateTime = "$formattedDate $formattedTime"


        return when (messageDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today)
            yesterdayDate -> UiText.Resource(Res.string.yesterday)
            else -> UiText.DynamicString(formattedDateTime)
        }
    }

    // --- Reusable Date Formatters ---
    private val europeDateFormat = LocalDateTime.Format {
        day()
        char('/')
        monthNumber()
        char('/')
        year()
    }

    private val usDateFormat = LocalDateTime.Format {
        monthNumber()
        char('/')
        day()
        char('/')
        year()
    }

    // --- Reusable Time Formatters ---
    private val timeFormat24H = LocalDateTime.Format {
        hour()
        char(':')
        minute()
    }

    private val timeFormat12H = LocalDateTime.Format {
        amPmHour()
        char(':')
        minute()
        char(' ') // Optional space before AM/PM
        amPmMarker("am", "pm")
    }
}