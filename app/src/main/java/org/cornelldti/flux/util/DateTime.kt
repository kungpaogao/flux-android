package org.cornelldti.flux.util

import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val ISO_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private val ISO_8601_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
private val DAY_ABBREV_LIST = listOf("Su", "M", "Tu", "W", "Th", "F", "Sa")
private val TIME_FORMAT_AM_PM = SimpleDateFormat("hh:mm a", Locale.US)
private val TIME_FORMAT_24 = SimpleDateFormat("HH:mm", Locale.US)

object DateTime {

    /**
     * Gets abbreviation of day for given day of week integer
     *
     * @param dayOfWeek number from 1..7 representing day of week
     */
    fun getDayAbbrev(dayOfWeek: Int) = DAY_ABBREV_LIST[dayOfWeek - 1]

    /**
     * Current time as Date object
     */
    val NOW: Date
        get() = Calendar.getInstance().time

    /**
     * Today's date in ISO date string format
     */
    val TODAY: String
        get() = NOW.toISODateString()

    /**
     * Tomorrow's date in ISO date string format
     */
    val TOMORROW: String
        get() = getOffsetDateFromToday(1).toISODateString()

    /**
     * End of week starting from today, i.e. six (6) days from today, in ISO date string format
     *
     * For example: if today is 2021-01-17, then `WEEK_END` is 2021-01-23
     */
    val WEEK_END: String
        get() = getOffsetDateFromToday(6).toISODateString()

    /**
     * Returns Date object associated with specified `offset` days from today
     *
     * @param offset number of days from today
     */
    private fun getOffsetDateFromToday(offset: Int): Date = getOffsetFromToday(offset).time

    /**
     * Returns Calendar instance associated with specified `offset` days from today
     *
     * @param offset number of days from today
     */
    fun getOffsetFromToday(offset: Int): Calendar =
        Calendar.getInstance().apply { add(Calendar.DATE, offset) }

    /**
     * Wrapper for DateFormat.is24HourFormat to prevent import of `DateFormat`
     */
    fun is24HourFormat(context: Context?): Boolean = DateFormat.is24HourFormat(context)

    /**
     * Converts time in milliseconds to time formatted according to user preferences
     */
    fun fromMillisToTimeString(millis: Long, context: Context?): String =
        if (is24HourFormat(context)) TIME_FORMAT_24.format(Date(millis))
        else TIME_FORMAT_AM_PM.format(Date(millis))

}

/**
 * Extension function to convert Date objects to ISO date strings
 */
fun Date.toISODateString(): String = ISO_DATE_FORMAT.format(this)

/**
 * Extension function to convert Date objects to time strings according to user time preferences
 */
fun Date.toTimeString(context: Context?): String =
    if (DateTime.is24HourFormat(context)) TIME_FORMAT_24.format(this)
    else TIME_FORMAT_AM_PM.format(this)

/**
 * Extension function to convert Date objects to ISO 8601 strings
 *
 * See more information here: [https://en.wikipedia.org/wiki/ISO_8601]
 */
fun Date.toISOString(): String = ISO_8601_FORMAT.format(this)