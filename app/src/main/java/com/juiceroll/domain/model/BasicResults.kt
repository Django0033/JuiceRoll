package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ═══════════════════════════════════════════
// Challenge Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("FullChallengeResult")
data class FullChallengeResult(
    val physicalRoll: Int,
    val physicalSkill: String,
    val physicalDc: Int,
    val mentalRoll: Int,
    val mentalSkill: String,
    val mentalDc: Int,
    val dcMethod: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.CHALLENGE
    override val description: String get() = "Challenge"
    override val diceResults: List<Int> get() = listOf(physicalRoll, mentalRoll)
    override val total: Int get() = physicalDc + mentalDc
    override val interpretation: String?
        get() = "$physicalSkill (DC $physicalDc) OR $mentalSkill (DC $mentalDc)"
}

@Serializable
@SerialName("DcResult")
data class DcResult(
    val roll: Int,
    val dc: Int,
    val method: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.CHALLENGE
    override val description: String get() = "DC"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = dc
    override val interpretation: String? get() = "DC $dc ($method)"
}

@Serializable
@SerialName("QuickDcResult")
data class QuickDcResult(
    val dice: List<Int>,
    val rawSum: Int,
    val dc: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.CHALLENGE
    override val description: String get() = "Quick DC"
    override val diceResults: List<Int> get() = dice
    override val total: Int get() = dc
    override val interpretation: String? get() = "DC $dc"
}

@Serializable
@SerialName("ChallengeSkillResult")
data class ChallengeSkillResult(
    val challengeType: String,
    val roll: Int,
    val skill: String,
    val suggestedDc: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.CHALLENGE
    override val description: String get() = "$challengeType Challenge"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = "$skill (DC $suggestedDc)"
}

@Serializable
@SerialName("PercentageChanceResult")
data class PercentageChanceResult(
    val roll: Int,
    val minPercent: Int,
    val maxPercent: Int,
    val percent: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.CHALLENGE
    override val description: String get() = "% Chance"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = percent
    override val interpretation: String? get() = "$minPercent-$maxPercent%"
}

// ═══════════════════════════════════════════
// Immersion Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("SensoryDetailResult")
data class SensoryDetailResult(
    val senseRoll: Int,
    val sense: String,
    val detailRoll: Int,
    val detail: String,
    val whereRoll: Int,
    val where: String,
    val skew: String = "none",
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IMMERSION
    override val description: String get() = "Sensory Detail"
    override val diceResults: List<Int> get() = listOf(senseRoll, detailRoll, whereRoll)
    override val total: Int get() = senseRoll + detailRoll + whereRoll
    override val interpretation: String? get() = "You $sense something $detail $where"
}

@Serializable
@SerialName("EmotionalAtmosphereResult")
data class EmotionalAtmosphereResult(
    val emotionRoll: Int,
    val negativeEmotion: String,
    val positiveEmotion: String,
    val selectedEmotion: String,
    val isPositive: Boolean,
    val causeRoll: Int,
    val cause: String,
    val skew: String = "none",
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IMMERSION
    override val description: String get() = "Emotional Atmosphere"
    override val diceResults: List<Int> get() = listOf(emotionRoll, causeRoll)
    override val total: Int get() = emotionRoll + causeRoll
    override val interpretation: String?
        get() = "It causes $selectedEmotion because $cause"
}

@Serializable
@SerialName("FullImmersionResult")
data class FullImmersionResult(
    val sensory: SensoryDetailResult? = null,
    val emotional: EmotionalAtmosphereResult? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IMMERSION
    override val description: String get() = "Full Immersion"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = "Full Immersion"
}

// ═══════════════════════════════════════════
// Wilderness Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("WildernessAreaResult")
data class WildernessAreaResult(
    val envRoll: Int,
    val environment: String,
    val typeRoll: Int,
    val typeName: String,
    val typeModifier: Int,
    val isTransition: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.WEATHER
    override val description: String
        get() = if (isTransition) "Wilderness Transition" else "Wilderness Area"
    override val diceResults: List<Int> get() = listOf(envRoll, typeRoll)
    override val total: Int get() = envRoll + typeRoll
    override val interpretation: String? get() = "$typeName $environment"
}

@Serializable
@SerialName("WildernessEncounterResult")
data class WildernessEncounterResult(
    val roll: Int,
    val secondRoll: Int? = null,
    val encounter: String,
    val requiresFollowUp: Boolean = false,
    val dieSize: Int = 10,
    val skewUsed: String = "straight",
    val wasLost: Boolean = false,
    val becameLost: Boolean = false,
    val becameFound: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ENCOUNTER
    override val description: String get() = "Wilderness Encounter"
    override val diceResults: List<Int>
        get() = if (secondRoll != null) listOf(roll, secondRoll) else listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = encounter
}

@Serializable
@SerialName("WildernessWeatherResult")
data class WildernessWeatherResult(
    val baseRoll: Int,
    val secondRoll: Int? = null,
    val weatherRow: Int,
    val weather: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.WEATHER
    override val description: String get() = "Weather"
    override val diceResults: List<Int>
        get() = if (secondRoll != null) listOf(baseRoll, secondRoll) else listOf(baseRoll)
    override val total: Int get() = weatherRow
    override val interpretation: String? get() = weather
}

@Serializable
@SerialName("WildernessDetailResult")
data class WildernessDetailResult(
    val detailType: String,
    val roll: Int,
    val result: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.WEATHER
    override val description: String get() = detailType
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = result
}

// ═══════════════════════════════════════════
// Monster Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("MonsterLevelResult")
data class MonsterLevelResult(
    val baseRoll: Int,
    val secondRoll: Int? = null,
    val modifier: Int = 0,
    val advantageType: String = "@",
    val monsterLevel: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ENCOUNTER
    override val description: String get() = "Monster Level"
    override val diceResults: List<Int>
        get() = if (secondRoll != null) listOf(baseRoll, secondRoll) else listOf(baseRoll)
    override val total: Int get() = monsterLevel
    override val interpretation: String? get() = "Level $monsterLevel"
}

@Serializable
@SerialName("MonsterEncounterResult")
data class MonsterEncounterResult(
    override val diceResults: List<Int>,
    val row: Int,
    val difficulty: String,
    val monster: String,
    val isDeadly: Boolean = false,
    val difficultyRoll: Int? = null,
    val wasDoubles: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ENCOUNTER
    override val description: String get() = "Monster Encounter"
    override val total: Int get() = row + 1
    override val interpretation: String? get() = monster
}

@Serializable
@SerialName("FullMonsterEncounterResult")
data class FullMonsterEncounterResult(
    override val diceResults: List<Int>,
    val row: Int,
    val difficulty: String,
    val hasBoss: Boolean = false,
    val bossMonster: String? = null,
    val monsters: List<MonsterCountData> = emptyList(),
    val environmentRow: Int = 0,
    val environmentFormula: String = "",
    val wasDoubles: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ENCOUNTER
    override val description: String get() = "Monster Encounter"
    override val total: Int get() = row + 1
    override val interpretation: String? get() = "Encounter $difficulty"
}

@Serializable
data class MonsterCountData(
    val code: String,
    val name: String,
    val count: Int,
    val skewSymbol: String = "",
)

@Serializable
@SerialName("MonsterTracksResult")
data class MonsterTracksResult(
    override val diceResults: List<Int>,
    val row: Int,
    val tracks: String,
    val modifier: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ENCOUNTER
    override val description: String get() = "Monster Tracks"
    override val total: Int get() = modifier
    override val interpretation: String? get() = tracks
}
