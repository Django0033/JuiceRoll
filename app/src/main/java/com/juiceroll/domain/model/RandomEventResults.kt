package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RandomEventResult")
data class RandomEventResult(
    val focusRoll: Int,
    val focus: String,
    val modifierRoll: Int,
    val modifier: String,
    val ideaRoll: Int,
    val idea: String,
    val ideaCategory: String = "Idea",
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.RANDOM_EVENT
    override val description: String get() = "Random Event"
    override val diceResults: List<Int> get() = listOf(focusRoll, modifierRoll, ideaRoll)
    override val total: Int get() = focusRoll + modifierRoll + ideaRoll
    override val interpretation: String? get() = "$focus: $modifier $idea"
    val eventPhrase: String get() = "$modifier $idea"
}

@Serializable
@SerialName("RandomEventFocusResult")
data class RandomEventFocusResult(
    val focusRoll: Int,
    val focus: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.RANDOM_EVENT
    override val description: String get() = "Random Event Focus"
    override val diceResults: List<Int> get() = listOf(focusRoll)
    override val total: Int get() = focusRoll
    override val interpretation: String? get() = focus
}

@Serializable
@SerialName("IdeaResult")
data class IdeaResult(
    val modifierRoll: Int,
    val modifier: String,
    val ideaRoll: Int,
    val idea: String,
    val ideaCategory: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.RANDOM_EVENT
    override val description: String get() = ideaCategory
    override val diceResults: List<Int> get() = listOf(modifierRoll, ideaRoll)
    override val total: Int get() = modifierRoll + ideaRoll
    override val interpretation: String? get() = "$modifier $idea"
    val phrase: String get() = "$modifier $idea"
}

@Serializable
@SerialName("SingleTableResult")
data class SingleTableResult(
    val roll: Int,
    val result: String,
    val tableName: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.RANDOM_EVENT
    override val description: String get() = tableName
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = result
}
