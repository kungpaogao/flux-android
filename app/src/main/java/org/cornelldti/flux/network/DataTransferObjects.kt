package org.cornelldti.flux.network

import kotlinx.serialization.Serializable
import org.cornelldti.flux.data.CampusLocation
import org.cornelldti.flux.data.DayMenu

/*
Data transfer objects are used to carry data between Retrofit responses and actual data classes
used in the UI.

Read more about the pattern: https://en.wikipedia.org/wiki/Data_transfer_object
*/

@Serializable
data class DailyHours(
    val startTimestamp: Int,
    val endTimestamp: Int
)

@Serializable
data class FacilityInfo(
    val id: String,
    val dailyHours: List<DailyHours>,
    val isOpen: Boolean,
    val nextOpen: Int,
    val closingAt: Int,
    val campusLocation: CampusLocation,
)

@Serializable
data class HowDense(
    val id: String,
    val density: Int
)

@Serializable
data class FacilityHour(
    val dailyHours: DailyHours,
    val date: String,
    val dayOfWeek: Int,
    val status: String,
    val statusText: String
)

@Serializable
data class FacilityHourList(
    val id: String,
    val hours: List<FacilityHour>,
)

@Serializable
data class MenuData(
    val id: String,
    val weeksMenus: List<DayMenu>
)