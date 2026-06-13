package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Enums ──

@Serializable
enum class FateCheckOutcome {
    @SerialName("noAnd") NO_AND,
    @SerialName("noBecause") NO_BECAUSE,
    @SerialName("no") NO,
    @SerialName("noBut") NO_BUT,
    @SerialName("unfavorable") UNFAVORABLE,
    @SerialName("favorable") FAVORABLE,
    @SerialName("yesBut") YES_BUT,
    @SerialName("yes") YES,
    @SerialName("yesBecause") YES_BECAUSE,
    @SerialName("yesAnd") YES_AND,
}

@Serializable
enum class SpecialTrigger {
    @SerialName("randomEvent") RANDOM_EVENT,
    @SerialName("invalidAssumption") INVALID_ASSUMPTION,
}

@Serializable
enum class ExpectationOutcome {
    @SerialName("expectedIntensified") EXPECTED_INTENSIFIED,
    @SerialName("expected") EXPECTED,
    @SerialName("nextMostExpected") NEXT_MOST_EXPECTED,
    @SerialName("favorable") FAVORABLE,
    @SerialName("modifiedIdea") MODIFIED_IDEA,
    @SerialName("unfavorable") UNFAVORABLE,
    @SerialName("opposite") OPPOSITE,
    @SerialName("oppositeIntensified") OPPOSITE_INTENSIFIED,
}

// ── Fate Check Result ──

@Serializable
@SerialName("FateCheckResult")
data class FateCheckResult(
    val likelihood: String,
    val fateDice: List<Int>,
    val fateSum: Int,
    val intensity: Int,
    val outcome: FateCheckOutcome,
    val specialTrigger: SpecialTrigger? = null,
    val primaryOnLeft: Boolean = true,
    val randomEventData: RandomEventData? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.FATE_CHECK
    override val description: String get() = "Fate Check ($likelihood)"
    override val diceResults: List<Int>
        get() = buildList {
            addAll(fateDice)
            add(intensity)
        }
    override val total: Int get() = fateSum
    override val interpretation: String? get() = outcome.name
}

// ── Expectation Check Result ──

@Serializable
@SerialName("ExpectationCheckResult")
data class ExpectationCheckResult(
    val fateDice: List<Int>,
    val fateSum: Int,
    val outcome: ExpectationOutcome,
    val meaningResult: DiscoverMeaningResult? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.EXPECTATION_CHECK
    override val description: String get() = "Expectation Check"
    override val diceResults: List<Int> get() = fateDice
    override val total: Int get() = fateSum
    override val interpretation: String? get() = outcome.name
}

// ── Scale Result ──

@Serializable
@SerialName("ScaleResult")
data class ScaleResult(
    val fateDice: List<Int>,
    val fateSum: Int,
    val intensity: Int,
    override val total: Int,
    val modifier: String,
    val multiplier: Double,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SCALE
    override val description: String get() = "Scale"
    override val diceResults: List<Int> get() = fateDice + intensity
    override val interpretation: String? get() = modifier
}

// ── Scaled Value Result ──

@Serializable
@SerialName("ScaledValueResult")
data class ScaledValueResult(
    val fateDice: List<Int>,
    val fateSum: Int,
    val intensity: Int,
    override val total: Int,
    val modifier: String,
    val multiplier: Double,
    val baseValue: Double,
    val scaledValue: Double,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SCALE
    override val description: String get() = "Scaled Value"
    override val diceResults: List<Int> get() = fateDice + intensity
    override val interpretation: String? get() = "$baseValue → $scaledValue ($modifier)"
}

// ── Next Scene Results ──

@Serializable
@SerialName("NextSceneResult")
data class NextSceneResult(
    val fateDice: List<Int>,
    val fateSum: Int,
    val sceneType: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NEXT_SCENE
    override val description: String get() = "Next Scene"
    override val diceResults: List<Int> get() = fateDice
    override val total: Int get() = fateSum
    override val interpretation: String? get() = sceneType
}

@Serializable
@SerialName("NextSceneWithFollowUpResult")
data class NextSceneWithFollowUpResult(
    val sceneType: String,
    val fateDice: List<Int>,
    val fateSum: Int,
    val focusResult: FocusResult? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NEXT_SCENE
    override val description: String get() = "Next Scene"
    override val diceResults: List<Int> get() = fateDice
    override val total: Int get() = fateSum
    override val interpretation: String? get() = sceneType
}

@Serializable
@SerialName("FocusResult")
data class FocusResult(
    val roll: Int,
    val focus: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NEXT_SCENE
    override val description: String get() = "Focus"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = focus
}

// ── Discover Meaning Result ──

@Serializable
@SerialName("DiscoverMeaningResult")
data class DiscoverMeaningResult(
    val adjectiveRoll: Int,
    val adjective: String,
    val nounRoll: Int,
    val noun: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DISCOVER_MEANING
    override val description: String get() = "Discover Meaning"
    override val diceResults: List<Int> get() = listOf(adjectiveRoll, nounRoll)
    override val total: Int get() = adjectiveRoll + nounRoll
    override val interpretation: String? get() = "$adjective $noun"
    val phrase: String get() = "$adjective $noun"
}
