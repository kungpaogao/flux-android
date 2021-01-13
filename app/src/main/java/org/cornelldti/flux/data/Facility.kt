package org.cornelldti.flux.data

import kotlinx.serialization.Serializable

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
)

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