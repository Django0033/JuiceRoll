package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PayThePriceResult")
data class PayThePriceResult(
    val isMajorTwist: Boolean = false,
    val roll: Int,
    val result: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.PAY_THE_PRICE
    override val description: String
        get() = if (isMajorTwist) "Major Plot Twist" else "Pay the Price"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = result
}

@Serializable
@SerialName("InterruptPlotPointResult")
data class InterruptPlotPointResult(
    val categoryRoll: Int,
    val category: String,
    val eventRoll: Int,
    val event: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.INTERRUPT_PLOT_POINT
    override val description: String get() = "Interrupt / Plot Point"
    override val diceResults: List<Int> get() = listOf(categoryRoll, eventRoll)
    override val total: Int get() = categoryRoll + eventRoll
    override val interpretation: String? get() = "$category: $event"
}

@Serializable
@SerialName("QuestResult")
data class QuestResult(
    val objectiveRoll: Int,
    val objective: String,
    override val description: String,
    val descriptionRoll: Int,
    val descriptionExpanded: String? = null,
    val descriptionSubRoll: Int? = null,
    val focusRoll: Int,
    val focus: String,
    val focusExpanded: String? = null,
    val focusSubRoll: Int? = null,
    val prepositionRoll: Int,
    val preposition: String,
    val locationRoll: Int,
    val location: String,
    val locationExpanded: String? = null,
    val locationSubRoll: Int? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.QUEST
    override val diceResults: List<Int>
        get() = buildList {
            add(objectiveRoll)
            add(descriptionRoll)
            if (descriptionSubRoll != null) add(descriptionSubRoll)
            add(focusRoll)
            if (focusSubRoll != null) add(focusSubRoll)
            add(prepositionRoll)
            add(locationRoll)
            if (locationSubRoll != null) add(locationSubRoll)
        }
    override val total: Int
        get() = objectiveRoll + descriptionRoll + (descriptionSubRoll ?: 0) +
            focusRoll + (focusSubRoll ?: 0) + prepositionRoll + locationRoll + (locationSubRoll ?: 0)
    override val interpretation: String?
        get() {
            val descText = descriptionExpanded?.let { "$it ($description)" } ?: description
            val focusText = focusExpanded?.let { "$it ($focus)" } ?: focus
            val locText = locationExpanded?.let { "$it ($location)" } ?: location
            return "$objective the $descText $focusText $preposition the $locText"
        }

    val questSentence: String get() = interpretation ?: ""
}

@Serializable
@SerialName("DetailResult")
data class DetailResult(
    val detailType: String,
    val roll: Int,
    val secondRoll: Int? = null,
    val result: String,
    val emoji: String? = null,
    val skew: String = "none",
    val requiresFollowUp: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DETAILS
    override val description: String get() = detailType
    override val diceResults: List<Int>
        get() = if (secondRoll != null) listOf(roll, secondRoll) else listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String?
        get() = if (emoji != null) "$emoji $result" else result
}

@Serializable
@SerialName("PropertyResult")
data class PropertyResult(
    val propertyRoll: Int,
    val property: String,
    val intensityRoll: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DETAILS
    override val description: String get() = "Property"
    override val diceResults: List<Int> get() = listOf(propertyRoll, intensityRoll)
    override val total: Int get() = propertyRoll + intensityRoll
    override val interpretation: String? get() = property
}

@Serializable
@SerialName("DualPropertyResult")
data class DualPropertyResult(
    val property1: PropertyResult? = null,
    val property2: PropertyResult? = null,
    val property1Roll: Int,
    val property1Name: String,
    val intensity1: Int,
    val property2Roll: Int,
    val property2Name: String,
    val intensity2: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DETAILS
    override val description: String get() = "Properties"
    override val diceResults: List<Int>
        get() = listOf(property1Roll, intensity1, property2Roll, intensity2)
    override val total: Int get() = property1Roll + property2Roll
    override val interpretation: String?
        get() = "$property1Name + $property2Name"
}

@Serializable
@SerialName("DetailWithFollowUpResult")
data class DetailWithFollowUpResult(
    val detailType: String,
    val roll: Int,
    val result: String,
    val skew: String = "none",
    val followUpResult: String? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DETAILS
    override val description: String get() = "Detail"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String?
        get() = if (followUpResult != null) "$result → $followUpResult" else result
}
