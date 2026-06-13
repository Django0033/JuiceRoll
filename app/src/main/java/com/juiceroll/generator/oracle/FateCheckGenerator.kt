package com.juiceroll.generator.oracle

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.FateCheckOutcome
import com.juiceroll.domain.model.FateCheckResult
import com.juiceroll.domain.model.RandomEventData
import com.juiceroll.domain.model.SpecialTrigger

/**
 * Fate Check preset for the Juice Oracle.
 * Uses 2dF (Fate dice) + 1d6 (Intensity) to answer yes/no questions.
 *
 * Primary interpretation:
 * - + = Yes-like result
 * - - = No-like result
 * - 0 = Look to secondary (Favorable/Unfavorable)
 *
 * When a Random Event is triggered (O O with primary on left), automatically
 * rolls on the Random Event tables to provide the event details.
 */
class FateCheckGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val randomEventGenerator: RandomEventGenerator = RandomEventGenerator(rollEngine),
) {
    companion object {
        val likelihoods: List<String> = listOf("Unlikely", "Even Odds", "Likely")
    }

    fun check(
        likelihood: String = "Even Odds",
        primaryOnLeft: Boolean? = null,
    ): FateCheckResult {
        val fateDice = rollEngine.rollFateDice(2)
        val primary = fateDice[0]
        val secondary = fateDice[1]
        val intensity = rollEngine.rollDie(6)
        val isPrimaryLeft = primaryOnLeft ?: (rollEngine.rollDie(2) == 1)

        val isDoubleBlanks = primary == 0 && secondary == 0
        var specialTrigger: SpecialTrigger? = null
        var randomEventData: RandomEventData? = null

        if (isDoubleBlanks) {
            specialTrigger = if (isPrimaryLeft) {
                SpecialTrigger.RANDOM_EVENT
            } else {
                SpecialTrigger.INVALID_ASSUMPTION
            }
            if (specialTrigger == SpecialTrigger.RANDOM_EVENT) {
                val re = randomEventGenerator.generate()
                randomEventData = RandomEventData(
                    focusRoll = re.focusRoll,
                    focus = re.focus,
                    modifierRoll = re.modifierRoll,
                    modifier = re.modifier,
                    ideaRoll = re.ideaRoll,
                    idea = re.idea,
                    ideaCategory = re.ideaCategory,
                )
            }
        }

        val outcome = interpretDice(primary, secondary, likelihood, isDoubleBlanks)

        return FateCheckResult(
            likelihood = likelihood,
            fateDice = fateDice,
            fateSum = primary + secondary,
            intensity = intensity,
            outcome = outcome,
            specialTrigger = specialTrigger,
            primaryOnLeft = isPrimaryLeft,
            randomEventData = randomEventData,
        )
    }

    /**
     * Interpret dice according to Juice Oracle Fate Check rules.
     */
    fun interpretDice(
        primary: Int,
        secondary: Int,
        likelihood: String,
        isDoubleBlanks: Boolean,
    ): FateCheckOutcome {
        // Likely mode: if either die is +, result is Yes-like
        if (likelihood == "Likely") {
            if (primary == 1 && secondary == 1) return FateCheckOutcome.YES_AND
            if (primary == -1 && secondary == -1) return FateCheckOutcome.NO_AND
            if ((primary == 1 && secondary == -1) || (primary == -1 && secondary == 1)) {
                return FateCheckOutcome.YES_BUT
            }
            if (primary == 1 || secondary == 1) return FateCheckOutcome.YES
            if (isDoubleBlanks) return FateCheckOutcome.YES
            if ((primary == 0 && secondary == -1) || (primary == -1 && secondary == 0)) {
                return FateCheckOutcome.NO
            }
        }

        // Unlikely mode: if either die is -, result is No-like
        if (likelihood == "Unlikely") {
            if (primary == 1 && secondary == 1) return FateCheckOutcome.YES_AND
            if (primary == -1 && secondary == -1) return FateCheckOutcome.NO_AND
            if ((primary == 1 && secondary == -1) || (primary == -1 && secondary == 1)) {
                return FateCheckOutcome.NO_BUT
            }
            if ((primary == 1 && secondary == 0) || (primary == 0 && secondary == 1)) {
                return FateCheckOutcome.YES
            }
            if ((primary == 0 && secondary == -1) || (primary == -1 && secondary == 0)) {
                return FateCheckOutcome.NO
            }
            if (isDoubleBlanks) return FateCheckOutcome.NO
        }

        // Standard (Even Odds)
        if (isDoubleBlanks) return FateCheckOutcome.YES_BUT

        if (primary == 1) {
            when (secondary) {
                1 -> return FateCheckOutcome.YES_AND
                -1 -> return FateCheckOutcome.YES_BUT
                else -> return FateCheckOutcome.YES_BECAUSE
            }
        }

        if (primary == -1) {
            when (secondary) {
                -1 -> return FateCheckOutcome.NO_AND
                1 -> return FateCheckOutcome.NO_BUT
                else -> return FateCheckOutcome.NO_BECAUSE
            }
        }

        // Primary is 0
        if (secondary == 1) return FateCheckOutcome.FAVORABLE
        if (secondary == -1) return FateCheckOutcome.UNFAVORABLE

        return FateCheckOutcome.FAVORABLE
    }
}
