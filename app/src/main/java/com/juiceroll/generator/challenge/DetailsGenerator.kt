package com.juiceroll.generator.challenge

import com.juiceroll.data.oracle.DetailsData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.DetailResult
import com.juiceroll.domain.model.DetailWithFollowUpResult
import com.juiceroll.domain.model.DualPropertyResult
import com.juiceroll.domain.model.PropertyResult

/**
 * Details generator preset for the Juice Oracle.
 * Uses details.md for colors, properties, and history.
 */
class DetailsGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun rollColor(): DetailResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val color = DetailsData.colors[index]
        val emoji = DetailsData.colorEmoji[index]
        return DetailResult(
            detailType = "color",
            roll = roll,
            result = color,
            emoji = emoji,
        )
    }

    fun rollProperty(): PropertyResult {
        val propRoll = rollEngine.rollDie(10)
        val intensityRoll = rollEngine.rollDie(6)
        val index = if (propRoll == 10) 9 else propRoll - 1
        val property = DetailsData.properties[index]

        return PropertyResult(
            propertyRoll = propRoll,
            property = property,
            intensityRoll = intensityRoll,
        )
    }

    fun rollTwoProperties(): DualPropertyResult {
        val prop1 = rollProperty()
        val prop2 = rollProperty()
        return DualPropertyResult(
            property1 = prop1,
            property2 = prop2,
            property1Roll = prop1.propertyRoll,
            property1Name = prop1.property,
            intensity1 = prop1.intensityRoll,
            property2Roll = prop2.propertyRoll,
            property2Name = prop2.property,
            intensity2 = prop2.intensityRoll,
        )
    }

    fun rollDetail(skew: String = "none"): DetailResult {
        val (roll, secondRoll) = when (skew) {
            "advantage" -> {
                val r = rollEngine.rollWithAdvantage(1, 10)
                r.chosenSum to (r.sum2 as Int?)
            }
            "disadvantage" -> {
                val r = rollEngine.rollWithDisadvantage(1, 10)
                r.chosenSum to (r.sum2 as Int?)
            }
            else -> rollEngine.rollDie(10) to null
        }

        val index = if (roll == 10) 9 else roll - 1
        val detail = DetailsData.detailModifiers[index]

        return DetailResult(
            detailType = "detail",
            roll = roll,
            secondRoll = secondRoll,
            result = detail,
            skew = skew,
            requiresFollowUp = detail == "History" || detail == "Property",
        )
    }

    fun rollDetailWithFollowUp(skew: String = "none"): DetailWithFollowUpResult {
        val detailResult = rollDetail(skew = skew)

        val followUpResult = when (detailResult.result) {
            "History" -> rollHistory().result
            "Property" -> rollProperty().property
            else -> null
        }

        return DetailWithFollowUpResult(
            detailType = detailResult.detailType,
            roll = detailResult.roll,
            result = detailResult.result,
            skew = detailResult.skew,
            followUpResult = followUpResult,
        )
    }

    fun rollHistory(skew: String = "none"): DetailResult {
        val (roll, secondRoll) = when (skew) {
            "advantage" -> {
                val r = rollEngine.rollWithAdvantage(1, 10)
                r.chosenSum to (r.sum2 as Int?)
            }
            "disadvantage" -> {
                val r = rollEngine.rollWithDisadvantage(1, 10)
                r.chosenSum to (r.sum2 as Int?)
            }
            else -> rollEngine.rollDie(10) to null
        }

        val index = if (roll == 10) 9 else roll - 1
        val history = DetailsData.histories[index]

        return DetailResult(
            detailType = "history",
            roll = roll,
            secondRoll = secondRoll,
            result = history,
            skew = skew,
        )
    }
}
