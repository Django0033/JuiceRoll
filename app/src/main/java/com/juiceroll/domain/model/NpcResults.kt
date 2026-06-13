package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("NpcActionResult")
data class NpcActionResult(
    val column: String,
    val roll: Int,
    val result: String,
    val dieSize: Int? = null,
    val allRolls: List<Int>? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_ACTION
    override val description: String get() = "NPC Action"
    override val diceResults: List<Int> get() = allRolls ?: listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = result
}

@Serializable
@SerialName("MotiveWithFollowUpResult")
data class MotiveWithFollowUpResult(
    val roll: Int,
    val motive: String,
    val followUp: IdeaData? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_ACTION
    override val description: String get() = "NPC Motive"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = motive
}

@Serializable
@SerialName("SimpleNpcProfileResult")
data class SimpleNpcProfileResult(
    val personalityRoll: Int,
    val personality: String,
    val needRoll: Int,
    val need: String,
    val motiveRoll: Int,
    val motive: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_PROFILE
    override val description: String get() = "NPC Simple Profile"
    override val diceResults: List<Int> get() = listOf(personalityRoll, needRoll, motiveRoll)
    override val total: Int get() = personalityRoll + needRoll + motiveRoll
    override val interpretation: String? get() = "$personality / $need / $motive"
}

@Serializable
@SerialName("NpcProfileResult")
data class NpcProfileResult(
    val primaryPersonalityRoll: Int,
    val primaryPersonality: String,
    val secondaryPersonalityRoll: Int,
    val secondaryPersonality: String,
    val needRoll: Int,
    val need: String,
    val motiveRoll: Int,
    val motive: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_PROFILE
    override val description: String get() = "NPC Full Profile"
    override val diceResults: List<Int>
        get() = listOf(primaryPersonalityRoll, secondaryPersonalityRoll, needRoll, motiveRoll)
    override val total: Int get() = primaryPersonalityRoll + secondaryPersonalityRoll + needRoll + motiveRoll
    override val interpretation: String?
        get() = "$primaryPersonality, yet $secondaryPersonality / $need / $motive"
}

@Serializable
@SerialName("DualPersonalityResult")
data class DualPersonalityResult(
    val primaryRoll: Int,
    val primary: String,
    val secondaryRoll: Int,
    val secondary: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_PROFILE
    override val description: String get() = "Dual Personality"
    override val diceResults: List<Int> get() = listOf(primaryRoll, secondaryRoll)
    override val total: Int get() = primaryRoll + secondaryRoll
    override val interpretation: String? get() = "$primary, yet $secondary"
}

@Serializable
@SerialName("ComplexNpcResult")
data class ComplexNpcResult(
    val name: NameResult? = null,
    val primaryPersonalityRoll: Int,
    val primaryPersonality: String,
    val secondaryPersonalityRoll: Int? = null,
    val secondaryPersonality: String? = null,
    val needRoll: Int,
    val need: String,
    val needSkew: String = "none",
    val needAllRolls: List<Int> = emptyList(),
    val motiveRoll: Int,
    val motive: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NPC_PROFILE
    override val description: String get() = "Complex NPC"
    override val diceResults: List<Int>
        get() = buildList {
            add(primaryPersonalityRoll)
            if (secondaryPersonalityRoll != null) add(secondaryPersonalityRoll)
            addAll(needAllRolls)
            add(motiveRoll)
        }
    override val total: Int get() = primaryPersonalityRoll + needRoll + motiveRoll
    override val interpretation: String?
        get() = "$primaryPersonality / $need / $motive"
}

@Serializable
@SerialName("InformationResult")
data class InformationResult(
    val typeRoll: Int,
    val topicRoll: Int,
    val informationType: String,
    val topic: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.EXTENDED_NPC_CONVERSATION
    override val description: String get() = "NPC Information"
    override val diceResults: List<Int> get() = listOf(typeRoll, topicRoll)
    override val total: Int get() = typeRoll + topicRoll
    override val interpretation: String? get() = "$informationType $topic"
}

@Serializable
@SerialName("CompanionResponseResult")
data class CompanionResponseResult(
    val roll: Int,
    val response: String,
    val skew: String = "none",
    val allRolls: List<Int> = emptyList(),
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.EXTENDED_NPC_CONVERSATION
    override val description: String get() = "Companion Response"
    override val diceResults: List<Int> get() = allRolls.ifEmpty { listOf(roll) }
    override val total: Int get() = roll
    override val interpretation: String? get() = response
}

@Serializable
@SerialName("DialogTopicResult")
data class DialogTopicResult(
    val roll: Int,
    val topic: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.EXTENDED_NPC_CONVERSATION
    override val description: String get() = "Dialog Topic"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = topic
}
