package com.juiceroll.generator.flavor

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.AbstractIconResult

/**
 * Abstract Icons generator based on the Juice Oracle Right Extension.
 * Roll 1d10 + 1d6 to pick an icon. These selections were inspired by Rory's Story Cubes.
 */
class AbstractIconsGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun generate(): AbstractIconResult {
        val d10Result = rollEngine.rollDie(10)
        val rowLabel = if (d10Result == 10) 0 else d10Result
        val d6Result = rollEngine.rollDie(6)
        val colLabel = d6Result

        return AbstractIconResult(
            d10Roll = d10Result,
            d6Roll = d6Result,
            rowLabel = rowLabel,
            colLabel = colLabel,
        )
    }

    companion object {
        val rowLabels: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        val colLabels: List<Int> = listOf(1, 2, 3, 4, 5, 6)

        fun getImagePath(row: Int, col: Int): String =
            "assets/images/abstract_icons/${row}_$col.png"

        fun getAllImagePaths(): List<String> {
            return rowLabels.flatMap { row ->
                colLabels.map { col -> getImagePath(row, col) }
            }
        }
    }
}
