package org.cornelldti.flux.data

data class Facility(val id: String, val name: String) {

    /* TODO: think about placing this in the constructor to take advantage of data classes
        - would allow for the generated functions (useful for diffs)
        - would have to couple network requests together (list + info) before init
     */

    // facilityInfo
    var dailyHours: List<Pair<Int, Int>> = listOf()
    var isOpen: Boolean = false
    var nextOpen: Int = -1
    var closingAt: Int = -1
    var campusLocation: CampusLocation = CampusLocation.CENTRAL

    // howDense
    var density: Int = 0
}

enum class CampusLocation {
    NORTH, WEST, SOUTH, CENTRAL
}