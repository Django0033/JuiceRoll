package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Enums ──

@Serializable
enum class IronswornOutcome {
    @SerialName("strongHit") STRONG_HIT,
    @SerialName("weakHit") WEAK_HIT,
    @SerialName("miss") MISS,
}

@Serializable
enum class IronswornOdds {
    @SerialName("almostCertain") ALMOST_CERTAIN,
    @SerialName("likely") LIKELY,
    @SerialName("fiftyFifty") FIFTY_FIFTY,
    @SerialName("unlikely") UNLIKELY,
    @SerialName("smallChance") SMALL_CHANCE,
}

// ── Ironsworn Action Result ──

@Serializable
@SerialName("IronswornActionResult")
data class IronswornActionResult(
    val actionDie: Int,
    val challengeDice: List<Int>,
    val statBonus: Int = 0,
    val adds: Int = 0,
    val actionScore: Int,
    val outcome: IronswornOutcome,
    val isMatch: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_ACTION
    override val description: String get() = "Action Roll"
    override val diceResults: List<Int> get() = listOf(actionDie) + challengeDice
    override val total: Int get() = actionScore
    override val interpretation: String? get() = outcome.name
}

// ── Ironsworn Progress Result ──

@Serializable
@SerialName("IronswornProgressResult")
data class IronswornProgressResult(
    val progressScore: Int,
    val challengeDice: List<Int>,
    val outcome: IronswornOutcome,
    val isMatch: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_PROGRESS
    override val description: String get() = "Progress Roll"
    override val diceResults: List<Int> get() = challengeDice
    override val total: Int get() = progressScore
    override val interpretation: String? get() = outcome.name
}

// ── Ironsworn Oracle Result ──

@Serializable
@SerialName("IronswornOracleResult")
data class IronswornOracleResult(
    val oracleRoll: Int,
    val oracleTable: String? = null,
    val dieType: Int = 100,
    val isMatch: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_ORACLE
    override val description: String
        get() = if (oracleTable != null) "Oracle: $oracleTable" else "Oracle Roll"
    override val diceResults: List<Int> get() = listOf(oracleRoll)
    override val total: Int get() = oracleRoll
    override val interpretation: String? get() = oracleRoll.toString()
}

// ── Ironsworn Yes/No Result ──

@Serializable
@SerialName("IronswornYesNoResult")
data class IronswornYesNoResult(
    val roll: Int,
    val odds: IronswornOdds,
    val isYes: Boolean,
    val isMatch: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_ORACLE
    override val description: String get() = "Ask the Oracle"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = if (isYes) "Yes" else "No"
}

// ── Ironsworn Cursed Oracle Result ──

@Serializable
@SerialName("IronswornCursedOracleResult")
data class IronswornCursedOracleResult(
    val oracleRoll: Int,
    val cursedDie: Int,
    val oracleTable: String? = null,
    val isCursed: Boolean = false,
    val isMatch: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_ORACLE
    override val description: String
        get() = if (oracleTable != null) "Cursed Oracle: $oracleTable" else "Cursed Oracle Roll"
    override val diceResults: List<Int> get() = listOf(oracleRoll, cursedDie)
    override val total: Int get() = oracleRoll
    override val interpretation: String? get() = oracleRoll.toString()
}

// ── Ironsworn Momentum Burn Result ──

@Serializable
@SerialName("IronswornMomentumBurnResult")
data class IronswornMomentumBurnResult(
    val actionDie: Int,
    val challengeDice: List<Int>,
    val momentumValue: Int,
    val statBonus: Int = 0,
    val adds: Int = 0,
    val originalActionScore: Int,
    val originalOutcome: IronswornOutcome,
    val burnedOutcome: IronswornOutcome,
    val isMatch: Boolean = false,
    val wasUpgraded: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.IRONSWORN_ACTION
    override val description: String get() = "Action Roll (Momentum Burn)"
    override val diceResults: List<Int> get() = listOf(actionDie) + challengeDice
    override val total: Int get() = momentumValue
    override val interpretation: String? get() = burnedOutcome.name
}
