package com.juiceroll.generator.oracle

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.ExpectationCheckResult
import com.juiceroll.domain.model.ExpectationOutcome

/**
 * Expectation Check preset for the Juice Oracle.
 * An alternative to Fate Check for players with existing expectations.
 *
 * Instead of asking "Is X true?", you assume X is true and test
 * whether your expectation holds.
 *
 * Also functions as NPC Behavior generation: assume what an NPC
 * will likely do, then test it.
 *
 * Uses 2dF only (no intensity die, unlike Fate Check).
 *
 * When the result is "O O" (Modified Idea), automatically rolls on the
 * Modifier + Idea (Discover Meaning) table to provide the modification.
 */
class ExpectationCheckGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val discoverMeaningGenerator: DiscoverMeaningGenerator =
        DiscoverMeaningGenerator(rollEngine),
) {
    fun check(): ExpectationCheckResult {
        val fateDice = rollEngine.rollFateDice(2)
        val primary = fateDice[0]
        val secondary = fateDice[1]
        val outcome = interpretDice(primary, secondary)

        val meaningResult = if (outcome == ExpectationOutcome.MODIFIED_IDEA) {
            discoverMeaningGenerator.generate()
        } else {
            null
        }

        return ExpectationCheckResult(
            fateDice = fateDice,
            fateSum = primary + secondary,
            outcome = outcome,
            meaningResult = meaningResult,
        )
    }

    /**
     * Interpret dice for expectation check.
     * Based on the Expectation/Behavior table from Juice instructions (page 55).
     */
    fun interpretDice(primary: Int, secondary: Int): ExpectationOutcome {
        // ++ = Expected (Intensified)
        if (primary == 1 && secondary == 1) return ExpectationOutcome.EXPECTED_INTENSIFIED
        // +0 = Expected
        if (primary == 1 && secondary == 0) return ExpectationOutcome.EXPECTED
        // +- = Next Most Expected
        if (primary == 1 && secondary == -1) return ExpectationOutcome.NEXT_MOST_EXPECTED
        // 0+ = Favorable
        if (primary == 0 && secondary == 1) return ExpectationOutcome.FAVORABLE
        // 00 = Modified Idea (roll on Modifier + Idea table)
        if (primary == 0 && secondary == 0) return ExpectationOutcome.MODIFIED_IDEA
        // 0- = Unfavorable
        if (primary == 0 && secondary == -1) return ExpectationOutcome.UNFAVORABLE
        // -+ = Next Most Expected
        if (primary == -1 && secondary == 1) return ExpectationOutcome.NEXT_MOST_EXPECTED
        // -0 = Opposite
        if (primary == -1 && secondary == 0) return ExpectationOutcome.OPPOSITE
        // -- = Opposite (Intensified)
        if (primary == -1 && secondary == -1) return ExpectationOutcome.OPPOSITE_INTENSIFIED
        // Fallback (should not reach here)
        return ExpectationOutcome.EXPECTED
    }
}
