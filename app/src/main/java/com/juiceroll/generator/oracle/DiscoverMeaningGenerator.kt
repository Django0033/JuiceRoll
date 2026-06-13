package com.juiceroll.generator.oracle

import com.juiceroll.data.oracle.MeaningData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.DiscoverMeaningResult

/**
 * Discover Meaning preset for the Juice Oracle.
 * Generates two-word prompts for open interpretation.
 * Uses the Meaning Tables from meaning-name-generator.md.
 */
class DiscoverMeaningGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun generate(): DiscoverMeaningResult {
        val adjRoll = rollEngine.rollDie(20)
        val nounRoll = rollEngine.rollDie(20)

        val adjective = MeaningData.adjectives[adjRoll - 1]
        val noun = MeaningData.nouns[nounRoll - 1]

        return DiscoverMeaningResult(
            adjectiveRoll = adjRoll,
            adjective = adjective,
            nounRoll = nounRoll,
            noun = noun,
        )
    }
}
