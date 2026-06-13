package com.juiceroll.generator.challenge

import com.juiceroll.data.oracle.PayThePriceData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.PayThePriceResult

/**
 * Pay the Price preset for the Juice Oracle.
 * Determines consequences on failure using pay-the-price.md.
 */
class PayThePriceGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun rollConsequence(): PayThePriceResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val consequence = PayThePriceData.consequences[index]
        return PayThePriceResult(
            isMajorTwist = false,
            roll = roll,
            result = consequence,
        )
    }

    fun rollMajorTwist(): PayThePriceResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val twist = PayThePriceData.majorTwists[index]
        return PayThePriceResult(
            isMajorTwist = true,
            roll = roll,
            result = twist,
        )
    }

    fun rollWithCriticalCheck(isCriticalFail: Boolean = false): PayThePriceResult {
        return if (isCriticalFail) rollMajorTwist() else rollConsequence()
    }
}
