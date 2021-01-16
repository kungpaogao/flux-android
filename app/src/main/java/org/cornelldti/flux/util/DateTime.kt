package org.cornelldti.flux.util

import java.text.SimpleDateFormat
import java.util.*

private val ISO_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private val ISO_8601_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)

object DateTime {

    private val DAYS = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    val TODAY: String
        get() = Calendar.getInstance().time.toISODateString()

    val TOMORROW: String
        get() {
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DATE, 1) }
            return tomorrow.time.toISODateString()
        }

    val NEXT_WEEK: String
        get() {
            val nextWeek = Calendar.getInstance().apply { add(Calendar.DATE, 6) }
            return nextWeek.time.toISODateString()
        }
}

fun Date.toISODateString(): String {
    return ISO_DATE_FORMAT.format(this)
}