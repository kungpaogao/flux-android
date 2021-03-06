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
    var nextOpen: Long = -1L,
    var closingAt: Long = -1L,
    var campusLocation: CampusLocation = CampusLocation.UNKNOWN,
    // howDense
    var density: Int = -1,
    // menuData
    var weekMenu: Map<String, List<Menu>> = mapOf()
    // TODO: facilityHours
) : Comparable<Facility> {
    /**
     * Returns string resource ID corresponding with current density
     */
    val densityStringResource: Int
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

    /**
     * Returns color resource ID corresponding with current density
     */
    val densityColorResource: Int
        get() =
            if (!this.isOpen) {
                R.color.flux_grey_light
            } else when (this.density) {
                0 -> R.color.flux_green
                1 -> R.color.flux_yellow
                2 -> R.color.flux_orange
                3 -> R.color.flux_red
                else -> R.color.flux_grey_light
            }

    /**
     * TODO: delete this if not used
     * @param date date given in ISO date string format
     */
    fun mealsByDate(date: String): List<Meal> {
        val dayMenus = weekMenu[date]
        return dayMenus?.map { it.description } ?: listOf()
    }

    /**
     * Defines default sort behavior for facilities. Sorts based on lowest density, then by name.
     */
    override fun compareTo(other: Facility): Int {
        return if (this.isOpen xor other.isOpen) {
            // one is open and the other is not
            if (this.isOpen) -1 else 1
        } else {
            // both are closed or both are open
            var density = 0
            if (this.isOpen) {
                // if open -> choose less empty
                density = this.density.compareTo(other.density)
            }
            // sort by name if tied or both closed
            if (density == 0) this.displayName.compareTo(other.displayName) else density
        }
    }
}

@Serializable
enum class Meal {
    @SerialName("Breakfast")
    BREAKFAST,

    @SerialName("Brunch")
    BRUNCH,

    @SerialName("Lunch")
    LUNCH,

    @SerialName("Lite Lunch")
    LITE_LUNCH,

    @SerialName("Dinner")
    DINNER,

    @SerialName("Open")
    OPEN
}

@Serializable
data class MenuCategory(
    val items: List<String>,
    val category: String
)

@Serializable
data class Menu(
    val menu: List<MenuCategory>,
    val description: Meal,
    val endTime: Long,
    val startTime: Long,
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