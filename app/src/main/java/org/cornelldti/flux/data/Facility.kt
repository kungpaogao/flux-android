package org.cornelldti.flux.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cornelldti.flux.R

@Serializable
data class Facility(
    val id: String,
    val displayName: String,
    // facilityInfo
    var dailyHours: List<Pair<Int, Int>> = listOf(),
    var isOpen: Boolean = false,
    var nextOpen: Int = -1,
    var closingAt: Int = -1,
    var campusLocation: CampusLocation = CampusLocation.UNKNOWN,
    // howDense
    var density: Int = -1,
    // menuData
    var menus: List<DayMenu> = listOf()
    // TODO: facilityHours
) {
    val densityString: Int
        get() =
            if (!this.isOpen) {
                R.string.status_closed
            } else when (this.density) {
                0 -> R.string.status_very_empty
                1 -> R.string.status_pretty_empty
                2 -> R.string.status_pretty_crowded
                3 -> R.string.status_very_crowded
                else -> R.string.status_unknown
            }
}

@Serializable
data class MenuItem(
    val items: List<String>,
    val category: String
)

@Serializable
data class Menu(
    val menu: List<MenuItem>,
    val description: String,
    val endTime: Int,
    val startTime: Int,
    // val similarity: Double
)

@Serializable
data class DayMenu(
    val date: String,
    val menus: List<Menu>
)

@Serializable
enum class CampusLocation {
    @SerialName("north")
    NORTH,

    @SerialName("west")
    WEST,

    @SerialName("south")
    SOUTH,

    @SerialName("central")
    CENTRAL,

    @SerialName("null")
    UNKNOWN
}