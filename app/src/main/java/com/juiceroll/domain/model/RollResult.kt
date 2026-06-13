package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── RollType enum (33 values, matching Flutter with extras) ──

@Serializable
enum class RollType {
    @SerialName("standard")
    STANDARD,
    @SerialName("fate")
    FATE,
    @SerialName("advantage")
    ADVANTAGE,
    @SerialName("disadvantage")
    DISADVANTAGE,
    @SerialName("skewed")
    SKEWED,
    @SerialName("tableLookup")
    TABLE_LOOKUP,

    // Ironsworn/Starforged
    @SerialName("ironswornAction")
    IRONSWORN_ACTION,
    @SerialName("ironswornProgress")
    IRONSWORN_PROGRESS,
    @SerialName("ironswornOracle")
    IRONSWORN_ORACLE,

    // Core Oracle
    @SerialName("fateCheck")
    FATE_CHECK,
    @SerialName("nextScene")
    NEXT_SCENE,
    @SerialName("randomEvent")
    RANDOM_EVENT,
    @SerialName("discoverMeaning")
    DISCOVER_MEANING,
    @SerialName("expectationCheck")
    EXPECTATION_CHECK,

    // NPC & Dialog
    @SerialName("npcAction")
    NPC_ACTION,
    @SerialName("npcProfile")
    NPC_PROFILE,
    @SerialName("extendedNpcConversation")
    EXTENDED_NPC_CONVERSATION,
    @SerialName("dialog")
    DIALOG,

    // Plot & Story
    @SerialName("payThePrice")
    PAY_THE_PRICE,
    @SerialName("quest")
    QUEST,
    @SerialName("interruptPlotPoint")
    INTERRUPT_PLOT_POINT,

    // Exploration
    @SerialName("weather")
    WEATHER,
    @SerialName("encounter")
    ENCOUNTER,
    @SerialName("dungeon")
    DUNGEON,
    @SerialName("location")
    LOCATION,

    // Generation
    @SerialName("settlement")
    SETTLEMENT,
    @SerialName("objectTreasure")
    OBJECT_TREASURE,
    @SerialName("challenge")
    CHALLENGE,
    @SerialName("details")
    DETAILS,
    @SerialName("immersion")
    IMMERSION,
    @SerialName("nameGenerator")
    NAME_GENERATOR,
    @SerialName("scale")
    SCALE,

    // Abstract Icons
    @SerialName("abstractIcons")
    ABSTRACT_ICONS,
}

// ── Sealed base class ──

@Serializable
sealed class RollResult {
    abstract val type: RollType
    abstract val timestamp: Long
    abstract val description: String
    abstract val diceResults: List<Int>
    abstract val total: Int
    abstract val interpretation: String?

    // ── Existing subtypes ──

    @Serializable
    @SerialName("StandardRollResult")
    data class StandardRollResult(
        val values: List<Int>,
        val sides: Int,
        override val total: Int,
        val dropped: List<Int> = emptyList(),
        val advantage: Boolean = false,
        val disadvantage: Boolean = false,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : RollResult() {
        override val type: RollType get() = RollType.STANDARD
        override val description: String get() = "Standard Roll"
        override val diceResults: List<Int> get() = values
        override val interpretation: String? get() = null
    }

    @Serializable
    @SerialName("FateRollResult")
    data class FateRollResult(
        val values: List<Int>,
        override val total: Int,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : RollResult() {
        override val type: RollType get() = RollType.FATE
        override val description: String get() = "Fate Roll"
        override val diceResults: List<Int> get() = values
        override val interpretation: String? get() = null
    }

    @Serializable
    @SerialName("InvalidExpression")
    data class InvalidExpression(
        val expression: String,
        override val timestamp: Long = System.currentTimeMillis(),
    ) : RollResult() {
        override val type: RollType get() = RollType.STANDARD
        override val description: String get() = expression
        override val diceResults: List<Int> get() = emptyList()
        override val total: Int get() = 0
        override val interpretation: String? get() = null
    }
}
