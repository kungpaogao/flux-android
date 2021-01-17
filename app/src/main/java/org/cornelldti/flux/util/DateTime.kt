package org.cornelldti.flux.util

import java.text.SimpleDateFormat
import java.util.*

private val ISO_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private val ISO_8601_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)

object DateTime {

    private val DAYS = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    /**
     * Today's date in ISO date string format
     */
    val TODAY: String
        get() = Calendar.getInstance().time.toISODateString()

    /**
     * Tomorrow's date in ISO date string format
     */
    val TOMORROW: String
        get() = getOffsetFromToday(1).toISODateString()

    /**
     * End of week starting from today, i.e. six (6) days from today, in ISO date string format
     *
     * For example: if today is 2021-01-17, then `WEEK_END` is 2021-01-23
     */
    val WEEK_END: String
        get() = getOffsetFromToday(6).toISODateString()

    /**
     * Returns Date object associated with specified `offset` days from today
     *
     * @param offset number of days from today
     */
    fun getOffsetFromToday(offset: Int): Date {
        val day = Calendar.getInstance().apply { add(Calendar.DATE, offset) }
        return day.time
    }
}

/**
 * Extension function to convert Date objects to ISO date strings
 */
fun Date.toISODateString(): String {
    return ISO_DATE_FORMAT.format(this)
}

/**
 * Extension function to convert Date objects to ISO 8601 strings
 *
 * See more information here: [https://en.wikipedia.org/wiki/ISO_8601]
 */
fun Date.toISOString(): String {
    return ISO_8601_FORMAT.format(this)
}