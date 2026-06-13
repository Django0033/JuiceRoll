package com.juiceroll.generator.oracle

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.ScaleResult
import com.juiceroll.domain.model.ScaledValueResult

/**
 * Scale preset for the Juice Oracle.
 * Converts 2dF + 1d6 to percentage modifiers for quantity/value adjustments.
 *
 * Based on expectation-behavior-intensity-scale.md Scale column.
 * Used when you need to modify a value (price, distance, duration, etc.).
 */
class ScaleGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun roll(): ScaleResult {
        val fateDice = rollEngine.rollFateDice(2)
        val fateSum = fateDice[0] + fateDice[1]
        val intensity = rollEngine.rollDie(6)
        val total = fateSum + intensity
        val clampedTotal = total.coerceIn(-1, 8)
        val modifier = scaleModifiers[clampedTotal] ?: "-"
        val multiplier = scaleMultipliers[clampedTotal] ?: 1.0

        return ScaleResult(
            fateDice = fateDice,
            fateSum = fateSum,
            intensity = intensity,
            total = total,
            modifier = modifier,
            multiplier = multiplier,
        )
    }

    fun applyToValue(baseValue: Double): ScaledValueResult {
        val scaleRoll = roll()
        val scaledValue = baseValue * scaleRoll.multiplier

        return ScaledValueResult(
            fateDice = scaleRoll.fateDice,
            fateSum = scaleRoll.fateSum,
            intensity = scaleRoll.intensity,
            total = scaleRoll.total,
            modifier = scaleRoll.modifier,
            multiplier = scaleRoll.multiplier,
            baseValue = baseValue,
            scaledValue = scaledValue,
        )
    }

    companion object {
        val scaleModifiers: Map<Int, String> = mapOf(
            -1 to "-100%",
            0 to "-50%",
            1 to "-25%",
            2 to "-10%",
            3 to "-",
            4 to "-",
            5 to "+10%",
            6 to "+25%",
            7 to "+50%",
            8 to "+100%",
        )

        val scaleMultipliers: Map<Int, Double> = mapOf(
            -1 to 0.0,
            0 to 0.5,
            1 to 0.75,
            2 to 0.9,
            3 to 1.0,
            4 to 1.0,
            5 to 1.1,
            6 to 1.25,
            7 to 1.5,
            8 to 2.0,
        )
    }
}
