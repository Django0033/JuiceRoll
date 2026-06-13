package com.juiceroll.domain.engine

import com.juiceroll.domain.model.RollResult
import kotlin.random.Random

object RollEngine {

    data class RollRequest(
        val expression: String,
        val advantage: Boolean = false,
        val disadvantage: Boolean = false
    )

    // ── Convenience methods for preset generators ──

    /** Roll a single die with [sides] sides (1 to sides inclusive). */
    fun rollDie(sides: Int): Int = Random.nextInt(sides) + 1

    /** Roll a single Fate die: returns -1, 0, or +1. */
    fun rollFateDie(): Int = Random.nextInt(3) - 1

    /** Roll [count] Fate dice and return the individual results. */
    fun rollFateDice(count: Int): List<Int> = List(count) { rollFateDie() }

    /** Roll [count]d[sides] and return the sum. */
    fun rollNdX(count: Int, sides: Int): Int =
        (1..count).sumOf { Random.nextInt(sides) + 1 }

    /** Result of a roll with advantage or disadvantage. */
    data class AdvantageRollResult(
        val sum1: Int,
        val sum2: Int,
        val chosenSum: Int,
        val usedFirst: Boolean,
    )

    /** Roll with advantage: roll [count]d[sides] twice, take the higher sum. */
    fun rollWithAdvantage(count: Int, sides: Int): AdvantageRollResult {
        val sum1 = rollNdX(count, sides)
        val sum2 = rollNdX(count, sides)
        return if (sum1 >= sum2) AdvantageRollResult(sum1, sum2, sum1, true)
        else AdvantageRollResult(sum1, sum2, sum2, false)
    }

    /** Roll with disadvantage: roll [count]d[sides] twice, take the lower sum. */
    fun rollWithDisadvantage(count: Int, sides: Int): AdvantageRollResult {
        val sum1 = rollNdX(count, sides)
        val sum2 = rollNdX(count, sides)
        return if (sum1 <= sum2) AdvantageRollResult(sum1, sum2, sum1, true)
        else AdvantageRollResult(sum1, sum2, sum2, false)
    }

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
