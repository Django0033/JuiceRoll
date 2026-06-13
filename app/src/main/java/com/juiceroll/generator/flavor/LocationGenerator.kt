package com.juiceroll.generator.flavor

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.LocationResult

/**
 * Location grid generator using 1d100 to determine position on a 5x5 grid.
 *
 * The grid is 5x5, colored like a bullseye:
 * - Center (row 3, col 3): The origin point
 * - Close (Ring 1): Cells adjacent to center
 * - Far (Ring 2): Edge/corner cells
 *
 * Two methods of use:
 * 1. Compass Method: Direction + Distance from center
 * 2. Zoom Method: Iterative zooming into regions
 */
class LocationGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun generate(): LocationResult {
        val rollResult = rollEngine.rollNdX(1, 100)
        val rollValue = if (rollResult == 100) 0 else rollResult - 1
        return fromValue(rollValue)
    }

    companion object {
        val gridRanges: List<List<List<Int>>> = listOf(
            listOf(listOf(0, 3), listOf(4, 7), listOf(8, 11), listOf(12, 15), listOf(16, 19)),
            listOf(listOf(20, 23), listOf(24, 27), listOf(28, 31), listOf(32, 35), listOf(36, 39)),
            listOf(listOf(40, 43), listOf(44, 47), listOf(48, 51), listOf(52, 55), listOf(56, 59)),
            listOf(listOf(60, 63), listOf(64, 67), listOf(68, 71), listOf(72, 75), listOf(76, 79)),
            listOf(listOf(80, 83), listOf(84, 87), listOf(88, 91), listOf(92, 95), listOf(96, 99)),
        )

        val distanceRings: List<List<Int>> = listOf(
            listOf(2, 2, 2, 2, 2),
            listOf(2, 1, 1, 1, 2),
            listOf(2, 1, 0, 1, 2),
            listOf(2, 1, 1, 1, 2),
            listOf(2, 2, 2, 2, 2),
        )

        fun fromValue(value: Int): LocationResult {
            val clampedValue = value.coerceIn(0, 99)
            val row = (clampedValue / 20) + 1
            val columnOffset = clampedValue % 20
            val column = (columnOffset / 4) + 1
            return LocationResult(
                roll = clampedValue,
                row = row,
                column = column,
            )
        }

        fun getRangeForCell(row: Int, column: Int): List<Int> {
            val r = row.coerceIn(1, 5)
            val c = column.coerceIn(1, 5)
            return gridRanges[r - 1][c - 1]
        }

        fun isInCell(value: Int, row: Int, column: Int): Boolean {
            val range = getRangeForCell(row, column)
            return value >= range[0] && value <= range[1]
        }

        fun getDistanceRing(row: Int, column: Int): Int {
            val r = row.coerceIn(1, 5)
            val c = column.coerceIn(1, 5)
            return distanceRings[r - 1][c - 1]
        }
    }
}
