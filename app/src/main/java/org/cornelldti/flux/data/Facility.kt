package org.cornelldti.flux.data

import kotlinx.serialization.Serializable
import org.cornelldti.flux.R

@Serializable
data class Facility(
    val id: String,
    val displayName: String,
    var dailyHours: List<Pair<Int, Int>> = listOf(),
    var isOpen: Boolean = false,
    var nextOpen: Int = -1,
    var closingAt: Int = -1,
    var campusLocation: CampusLocation = CampusLocation.CENTRAL,
    var density: Int = -1
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

enum class CampusLocation {
    NORTH, WEST, SOUTH, CENTRAL, UNKNOWN
}

fun String.toCampusLocation() =
    when (this) {
        "north" -> CampusLocation.NORTH
        "west" -> CampusLocation.WEST
        "south" -> CampusLocation.SOUTH
        "central" -> CampusLocation.CENTRAL
        else -> CampusLocation.UNKNOWN
    }