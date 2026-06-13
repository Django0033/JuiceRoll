package com.juiceroll.generator.flavor

import com.juiceroll.data.oracle.ImmersionData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.EmotionalAtmosphereResult
import com.juiceroll.domain.model.FullImmersionResult
import com.juiceroll.domain.model.SensoryDetailResult

/**
 * Immersion generator preset for the Juice Oracle.
 * Uses immersion.md for sensory details and atmosphere.
 *
 * Full Immersion roll is 5d10 + 1dF:
 * - Sense (d10): 1-3=See, 4-6=Hear, 7-8=Smell, 9-0=Feel
 * - Detail (d10): Based on sense category
 * - Where (d10): Location of the sensory detail
 * - Emotion (d10): The emotional reaction
 * - Fate (1dF): Negative (-/blank) or Positive (+)
 * - Cause (d10): Why it causes that emotion
 */
class ImmersionGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun generateSensoryDetail(
        senseDie: Int = 10,
        skew: String = "none",
    ): SensoryDetailResult {
        val senseRoll = rollEngine.rollDie(senseDie)
        val detailRoll = rollEngine.rollDie(10)

        // For skew: advantage = closer (lower), disadvantage = further (higher)
        val whereRoll = when (skew) {
            "advantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                minOf(r1, r2)
            }
            "disadvantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                maxOf(r1, r2)
            }
            else -> rollEngine.rollDie(10)
        }

        val senseKey = if (senseRoll == 10) 0 else senseRoll
        val sense = ImmersionData.senseCategories[senseKey] ?: "See"

        val detailIndex = if (detailRoll == 10) 9 else detailRoll - 1
        val detail = when (sense) {
            "See" -> ImmersionData.seeDetails[detailIndex]
            "Hear" -> ImmersionData.hearDetails[detailIndex]
            "Smell" -> ImmersionData.smellDetails[detailIndex]
            "Feel" -> ImmersionData.feelDetails[detailIndex]
            else -> ImmersionData.seeDetails[detailIndex]
        }

        val whereIndex = if (whereRoll == 10) 9 else whereRoll - 1
        val where = ImmersionData.whereLocations[whereIndex]

        return SensoryDetailResult(
            senseRoll = senseRoll,
            sense = sense,
            detailRoll = detailRoll,
            detail = detail,
            whereRoll = whereRoll,
            where = where,
            skew = skew,
        )
    }

    fun generateEmotionalAtmosphere(
        emotionDie: Int = 10,
        skew: String = "none",
    ): EmotionalAtmosphereResult {
        // For emotion skew: advantage = positive (higher index), disadvantage = negative
        val emotionRoll = when (skew) {
            "advantage" -> {
                val r1 = rollEngine.rollDie(emotionDie)
                val r2 = rollEngine.rollDie(emotionDie)
                maxOf(r1, r2)
            }
            "disadvantage" -> {
                val r1 = rollEngine.rollDie(emotionDie)
                val r2 = rollEngine.rollDie(emotionDie)
                minOf(r1, r2)
            }
            else -> rollEngine.rollDie(emotionDie)
        }

        val causeRoll = rollEngine.rollDie(10)

        // Roll 1dF to determine emotion polarity:
        // - or blank (1-4) = negative emotion
        // + (5-6) = positive emotion
        // But rollFateDie returns -1, 0, 1 so + result = 1
        val fateDieRoll = rollEngine.rollFateDie()
        val isPositive = fateDieRoll == 1

        val emotionIndex = if (emotionRoll == 10) 9 else emotionRoll - 1
        val causeIndex = if (causeRoll == 10) 9 else causeRoll - 1

        val negativeEmotion = ImmersionData.negativeEmotions[emotionIndex]
        val positiveEmotion = ImmersionData.positiveEmotions[emotionIndex]
        val cause = ImmersionData.causes[causeIndex]
        val selectedEmotion = if (isPositive) positiveEmotion else negativeEmotion

        return EmotionalAtmosphereResult(
            emotionRoll = emotionRoll,
            negativeEmotion = negativeEmotion,
            positiveEmotion = positiveEmotion,
            selectedEmotion = selectedEmotion,
            isPositive = isPositive,
            causeRoll = causeRoll,
            cause = cause,
            skew = skew,
        )
    }

    fun generateFullImmersion(
        senseDie: Int = 10,
        emotionDie: Int = 10,
        sensorySkew: String = "none",
        emotionSkew: String = "none",
    ): FullImmersionResult {
        val sensory = generateSensoryDetail(senseDie = senseDie, skew = sensorySkew)
        val emotional = generateEmotionalAtmosphere(emotionDie = emotionDie, skew = emotionSkew)
        return FullImmersionResult(sensory = sensory, emotional = emotional)
    }
}
