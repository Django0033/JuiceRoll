package com.juiceroll.generator.oracle

import com.juiceroll.data.oracle.InterruptPlotPointData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.InterruptPlotPointResult

/**
 * Interrupt / Plot Point preset for the Juice Oracle.
 * Uses interrupt-plot-point.md for story interruptions.
 */
class InterruptPlotPointGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun generate(): InterruptPlotPointResult {
        val categoryRoll = rollEngine.rollDie(10)
        val eventRoll = rollEngine.rollDie(10)

        val categoryKey = if (categoryRoll == 10) 0 else categoryRoll
        val category = InterruptPlotPointData.categories[categoryKey] ?: "Action"

        val eventIndex = if (eventRoll == 10) 9 else eventRoll - 1
        val event = when (category) {
            "Action" -> InterruptPlotPointData.actionEvents[eventIndex]
            "Tension" -> InterruptPlotPointData.tensionEvents[eventIndex]
            "Mystery" -> InterruptPlotPointData.mysteryEvents[eventIndex]
            "Social" -> InterruptPlotPointData.socialEvents[eventIndex]
            "Personal" -> InterruptPlotPointData.personalEvents[eventIndex]
            else -> InterruptPlotPointData.actionEvents[eventIndex]
        }

        return InterruptPlotPointResult(
            categoryRoll = categoryRoll,
            category = category,
            eventRoll = eventRoll,
            event = event,
        )
    }
}
