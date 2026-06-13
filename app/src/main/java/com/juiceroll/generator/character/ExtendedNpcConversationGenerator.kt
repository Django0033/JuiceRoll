package com.juiceroll.generator.character

import com.juiceroll.data.oracle.ExtendedNpcConversationData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.CompanionResponseResult
import com.juiceroll.domain.model.DialogTopicResult
import com.juiceroll.domain.model.InformationResult

/**
 * Extended NPC Conversation Tables preset for the Juice Oracle.
 *
 * Alternative to the Dialog Grid mini-game for NPC conversations.
 * Provides tables for:
 * - Information (2d100): Type of Information + Topic of Information
 * - Companion Response (1d100): Ordered responses to "the plan"
 * - Extended NPC Dialog Topic (1d100): What NPCs are talking about
 *
 * From the Juice instructions:
 * "NPCs make the world feel alive. Talking with them can help you world-build,
 * give you side quests, or give information that your character would otherwise
 * not have access to."
 */
class ExtendedNpcConversationGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    fun rollInformation(): InformationResult {
        val typeRoll = rollEngine.rollDie(100)
        val topicRoll = rollEngine.rollDie(100)

        val informationType = ExtendedNpcConversationData.informationTypes[typeRoll - 1]
        val topic = ExtendedNpcConversationData.informationTopics[topicRoll - 1]

        return InformationResult(
            typeRoll = typeRoll,
            topicRoll = topicRoll,
            informationType = informationType,
            topic = topic,
        )
    }

    fun rollCompanionResponse(skew: String = "none"): CompanionResponseResult {
        val (roll, allRolls) = when (skew) {
            "advantage" -> {
                val r = rollEngine.rollWithAdvantage(1, 100)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            "disadvantage" -> {
                val r = rollEngine.rollWithDisadvantage(1, 100)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            else -> {
                val r = rollEngine.rollDie(100)
                r to listOf(r)
            }
        }

        val response = ExtendedNpcConversationData.companionResponses[roll - 1]

        return CompanionResponseResult(
            roll = roll,
            response = response,
            skew = skew,
            allRolls = allRolls,
        )
    }

    fun rollDialogTopic(): DialogTopicResult {
        val roll = rollEngine.rollDie(100)
        val topic = ExtendedNpcConversationData.dialogTopics[roll - 1]

        return DialogTopicResult(
            roll = roll,
            topic = topic,
        )
    }
}
