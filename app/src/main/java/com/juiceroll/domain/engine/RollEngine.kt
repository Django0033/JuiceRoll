package com.juiceroll.domain.engine

import com.juiceroll.domain.model.RollResult
import kotlin.random.Random

object RollEngine {

    data class RollRequest(
        val expression: String,
        val advantage: Boolean = false,
        val disadvantage: Boolean = false
    )

    fun roll(
        count: Int = 1,
        sides: Int = 6,
        advantage: Boolean = false,
        disadvantage: Boolean = false
    ): RollResult.StandardRollResult {
        val effectiveCount = when {
            advantage || disadvantage -> count * 2
            else -> count
        }
        val values = List(effectiveCount) { Random.nextInt(1, sides + 1) }
        val (kept, dropped) = when {
            advantage -> values.sortedDescending().let {
                it.take(count) to it.drop(count)
            }
            disadvantage -> values.sorted().let {
                it.take(count) to it.drop(count)
            }
            else -> values to emptyList()
        }
        return RollResult.StandardRollResult(
            values = kept,
            sides = sides,
            total = kept.sum(),
            dropped = dropped,
            advantage = advantage,
            disadvantage = disadvantage
        )
    }

    fun rollFate(): RollResult.FateRollResult {
        val values = List(4) { Random.nextInt(-1, 2) } // -1, 0, or +1
        return RollResult.FateRollResult(
            values = values,
            total = values.sum()
        )
    }

    fun parseAndRoll(expression: String): RollResult {
        val regex = Regex("""(\d+)?d(\d+)""")
        val match = regex.find(expression) ?: return RollResult.InvalidExpression(expression)
        val count = match.groupValues[1].toIntOrNull() ?: 1
        val sides = match.groupValues[2].toInt()
        return roll(count, sides)
    }
}
