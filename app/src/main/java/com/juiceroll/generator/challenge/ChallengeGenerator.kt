package com.juiceroll.generator.challenge

import com.juiceroll.data.oracle.ChallengeData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.ChallengeSkillResult
import com.juiceroll.domain.model.DcResult
import com.juiceroll.domain.model.FullChallengeResult
import com.juiceroll.domain.model.PercentageChanceResult
import com.juiceroll.domain.model.QuickDcResult

/**
 * Challenge generator preset for the Juice Oracle.
 * Uses random-event-challenge.md for skill challenges and DCs.
 *
 * Core concept: Roll a physical challenge and a mental challenge,
 * then create a situation where these challenges make sense.
 * PC must pass only one; otherwise, Pay The Price.
 */
class ChallengeGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    /**
     * Generate a FULL CHALLENGE: both physical and mental skills with independent DCs.
     * PC must pass only ONE of these; otherwise, Pay The Price.
     */
    fun rollFullChallenge(dcSkew: String = "none"): FullChallengeResult {
        val physicalRoll = rollEngine.rollDie(10)
        val physicalIndex = if (physicalRoll == 10) 9 else physicalRoll - 1
        val physicalSkill = ChallengeData.physicalChallenges[physicalIndex]
        val physicalDcResult = rollDc(skew = dcSkew)

        val mentalRoll = rollEngine.rollDie(10)
        val mentalIndex = if (mentalRoll == 10) 9 else mentalRoll - 1
        val mentalSkill = ChallengeData.mentalChallenges[mentalIndex]
        val mentalDcResult = rollDc(skew = dcSkew)

        return FullChallengeResult(
            physicalRoll = physicalRoll,
            physicalSkill = physicalSkill,
            physicalDc = physicalDcResult.dc,
            mentalRoll = mentalRoll,
            mentalSkill = mentalSkill,
            mentalDc = mentalDcResult.dc,
            dcMethod = physicalDcResult.method,
        )
    }

    /**
     * Generate a quick DC (2d6+6).
     */
    fun rollQuickDc(): QuickDcResult {
        val dice = listOf(rollEngine.rollDie(6), rollEngine.rollDie(6))
        val rawSum = dice.sum()
        val dc = rawSum + 6
        return QuickDcResult(dice = dice, rawSum = rawSum, dc = dc)
    }

    /**
     * Roll for a DC using 1d10 with optional advantage/disadvantage.
     */
    fun rollDc(skew: String = "none"): DcResult {
        val (roll, method) = when (skew) {
            "advantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                (if (r1 > r2) r1 else r2) to "Easy (1d10@+)"
            }
            "disadvantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                (if (r1 < r2) r1 else r2) to "Hard (1d10@-)"
            }
            else -> rollEngine.rollDie(10) to "Random (1d10)"
        }

        val index = if (roll == 10) 9 else roll - 1
        val dc = ChallengeData.dcValues[index]

        return DcResult(roll = roll, dc = dc, method = method)
    }

    /**
     * Roll for a Balanced DC using d100 bell curve.
     */
    fun rollBalancedDc(): DcResult {
        val d100 = rollEngine.rollDie(100)
        var index = 0
        for (i in ChallengeData.percentageRanges.indices) {
            val range = ChallengeData.percentageRanges[i]
            if (d100 >= range[0] && d100 <= range[1]) {
                index = i
                break
            }
        }
        val dc = ChallengeData.dcValues[index]
        return DcResult(roll = d100, dc = dc, method = "Balanced (1d100)")
    }

    /**
     * Roll for a physical challenge skill.
     */
    fun rollPhysicalChallenge(): ChallengeSkillResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val skill = ChallengeData.physicalChallenges[index]
        val dc = ChallengeData.dcValues[index]
        return ChallengeSkillResult(
            challengeType = "physical",
            roll = roll,
            skill = skill,
            suggestedDc = dc,
        )
    }

    /**
     * Roll for a mental challenge skill.
     */
    fun rollMentalChallenge(): ChallengeSkillResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val skill = ChallengeData.mentalChallenges[index]
        val dc = ChallengeData.dcValues[index]
        return ChallengeSkillResult(
            challengeType = "mental",
            roll = roll,
            skill = skill,
            suggestedDc = dc,
        )
    }

    /**
     * Roll for any challenge (50/50 physical vs mental).
     */
    fun rollAnyChallenge(): ChallengeSkillResult {
        return if (rollEngine.rollDie(2) == 1) {
            rollPhysicalChallenge()
        } else {
            rollMentalChallenge()
        }
    }

    /**
     * Roll for a percentage chance (d10 -> % range).
     */
    fun rollPercentageChance(): PercentageChanceResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val range = ChallengeData.percentageRanges[index]
        val minPercent = range[0]
        val maxPercent = range[1]
        val percent = (minPercent + maxPercent) / 2
        return PercentageChanceResult(
            roll = roll,
            minPercent = minPercent,
            maxPercent = maxPercent,
            percent = percent,
        )
    }
}
