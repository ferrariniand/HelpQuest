@file:OptIn(ExperimentalTime::class)

package com.helpquest.core.presentation.util

import helpquest.core.presentation.generated.resources.Res
import helpquest.core.presentation.generated.resources.today
import helpquest.core.presentation.generated.resources.today_with_time
import helpquest.core.presentation.generated.resources.yesterday
import helpquest.core.presentation.generated.resources.yesterday_with_time
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DateUtils {

    private fun formatDateFromLDT(
        localDateTime: LocalDateTime,
        isUsingEuropeDateFormat: Boolean,
        timeZone: TimeZone,
        clock: Clock = Clock.System
    ): UiText {
        val todayDate = clock.now().toLocalDateTime(timeZone).date
        val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

        val dateFormat = if (isUsingEuropeDateFormat) europeDateFormat else usDateFormat

        val formattedDate = localDateTime.format(dateFormat)

        return when (localDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today)
            yesterdayDate -> UiText.Resource(Res.string.yesterday)
            else -> UiText.DynamicString(formattedDate)
        }
    }

    fun formatDateSeparator(
        date: LocalDate,
        isUsingEuropeDateFormat: Boolean = true,
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = date.atStartOfDayIn(timeZone).toLocalDateTime(timeZone)
        return formatDateFromLDT(localDateTime, isUsingEuropeDateFormat, timeZone)
    }

    fun formatDate(
        instant: Instant,
        isUsingEuropeDateFormat: Boolean = true,
    ): UiText {
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = instant.toLocalDateTime(timeZone)
        return formatDateFromLDT(localDateTime, isUsingEuropeDateFormat, timeZone)
    }

    private fun formatTimeString(
        instant: Instant,
        isUsing24HourFormat: Boolean = true,
    ): String {
        val timeZone = TimeZone.currentSystemDefault()
        val messageDateTime = instant.toLocalDateTime(timeZone)

        val timeFormat = if (isUsing24HourFormat) timeFormat24H else timeFormat12H

        return messageDateTime.format(timeFormat)
    }

    fun formatTime(
        instant: Instant,
        isUsing24HourFormat: Boolean = true,
    ): UiText {
        return UiText.DynamicString(
            formatTimeString(
                instant = instant,
                isUsing24HourFormat = isUsing24HourFormat
            )
        )
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

        // Choose the date format part
        val dateFormat = if (isUsingEuropeDateFormat) europeDateFormat else usDateFormat

        val formattedDate = messageDateTime.format(dateFormat)
        val formattedTime = formatTimeString(
            instant = instant,
            isUsing24HourFormat = isUsing24HourFormat
        )
        val formattedDateTime = "$formattedDate, $formattedTime"


        return when (messageDateTime.date) {
            todayDate -> UiText.Resource(Res.string.today_with_time, arrayOf(formattedTime))
            yesterdayDate -> UiText.Resource(Res.string.yesterday_with_time, arrayOf(formattedTime))
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