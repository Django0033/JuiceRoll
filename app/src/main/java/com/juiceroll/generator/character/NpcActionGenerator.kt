package com.juiceroll.generator.character

import com.juiceroll.data.oracle.DungeonData
import com.juiceroll.data.oracle.NpcActionData
import com.juiceroll.data.oracle.RandomEventData
import com.juiceroll.data.oracle.WildernessData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.ComplexNpcResult
import com.juiceroll.domain.model.DualPersonalityResult
import com.juiceroll.domain.model.FocusResult
import com.juiceroll.domain.model.IdeaData
import com.juiceroll.domain.model.MotiveWithFollowUpResult
import com.juiceroll.domain.model.NameResult
import com.juiceroll.domain.model.NpcActionResult
import com.juiceroll.domain.model.NpcProfileResult
import com.juiceroll.domain.model.SimpleNpcProfileResult
import com.juiceroll.generator.challenge.DetailsGenerator
import com.juiceroll.generator.oracle.NextSceneGenerator
import com.juiceroll.generator.oracle.RandomEventGenerator
import com.juiceroll.generator.world.DungeonGenerator
import com.juiceroll.generator.world.SettlementGenerator

/**
 * NPC Action preset for the Juice Oracle.
 * Determines NPC behavior using npc-action.md tables.
 *
 * The most complex generator with extensive cross-references to other generators.
 * Settlement is lazy-initialized to avoid circular dependency.
 */
class NpcActionGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val detailsGenerator: DetailsGenerator = DetailsGenerator(rollEngine),
    private val nextSceneGenerator: NextSceneGenerator = NextSceneGenerator(rollEngine),
    settlementGenerator: SettlementGenerator? = null,
    private val nameGenerator: NameGenerator = NameGenerator(rollEngine),
    private val randomEventGenerator: RandomEventGenerator = RandomEventGenerator(rollEngine),
    private val dungeonGenerator: DungeonGenerator = DungeonGenerator(rollEngine),
) {
    /** Lazy settlement to avoid circular dependency with Settlement -> NpcAction */
    private val _settlement: SettlementGenerator by lazy {
        settlementGenerator ?: SettlementGenerator(rollEngine)
    }
    val settlement: SettlementGenerator get() = _settlement

    // ── Helper ──

    /** Convert 1-10 roll to 0-9 index */
    private fun idx(roll: Int): Int = if (roll == 10) 9 else roll - 1

    // ── Individual Column Rolls ──

    /**
     * Roll for a single NPC action with disposition and context.
     * Passive: d6, Active: d10
     * Active Context: advantage, Passive Context: disadvantage
     */
    fun rollAction(
        disposition: String = "active",
        context: String = "active",
    ): NpcActionResult {
        val dieSize = if (disposition == "passive") 6 else 10
        val roll: Int
        val allRolls: List<Int>

        if (context == "active") {
            val r = rollEngine.rollWithAdvantage(1, dieSize)
            roll = r.chosenSum
            allRolls = listOf(r.sum1, r.sum2)
        } else if (context == "passive") {
            val r = rollEngine.rollWithDisadvantage(1, dieSize)
            roll = r.chosenSum
            allRolls = listOf(r.sum1, r.sum2)
        } else {
            roll = rollEngine.rollDie(dieSize)
            allRolls = listOf(roll)
        }

        return NpcActionResult(
            column = "action",
            roll = roll,
            result = NpcActionData.npcActions[idx(roll)],
            dieSize = dieSize,
            allRolls = allRolls,
        )
    }

    /** Roll for NPC personality (1d10). */
    fun rollPersonality(): NpcActionResult {
        val roll = rollEngine.rollDie(10)
        return NpcActionResult(
            column = "personality",
            roll = roll,
            result = NpcActionData.npcPersonalities[idx(roll)],
        )
    }

    /**
     * Roll for NPC need with optional skew.
     * Disadvantage = more primitive needs, Advantage = more complex needs.
     */
    fun rollNeed(skew: String = "none"): NpcActionResult {
        val (roll, allRolls) = when (skew) {
            "complex" -> {
                val r = rollEngine.rollWithAdvantage(1, 10)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            "primitive" -> {
                val r = rollEngine.rollWithDisadvantage(1, 10)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            else -> rollEngine.rollDie(10) to listOf(rollEngine.rollDie(10))
        }

        return NpcActionResult(
            column = "need",
            roll = roll,
            result = NpcActionData.npcNeeds[idx(roll)],
            allRolls = allRolls,
        )
    }

    /** Roll for NPC motive/topic (1d10). */
    fun rollMotive(): NpcActionResult {
        val roll = rollEngine.rollDie(10)
        return NpcActionResult(
            column = "motive",
            roll = roll,
            result = NpcActionData.npcMotives[idx(roll)],
        )
    }

    /**
     * Expand a focus entry by rolling on the appropriate sub-table.
     * Italic Focus entries: Monster, Event, Environment, Person, Location, Object
     */
    private fun expandFocus(focus: String): Pair<Int, String>? {
        if (!NpcActionData.npcItalicFocuses.contains(focus)) return null

        val subRoll = rollEngine.rollDie(10)
        val subIndex = idx(subRoll)

        val expanded = when (focus) {
            "Monster" -> DungeonData.dungeonMonsterDescriptors[subIndex]
            "Event" -> RandomEventData.eventWords[subIndex]
            "Environment" -> WildernessData.wildernessEnvironments[subIndex]
            "Person" -> RandomEventData.personWords[subIndex]
            "Location" -> {
                val name = _settlement.generateName()
                return subRoll to name.name
            }
            "Object" -> RandomEventData.objectWords[subIndex]
            else -> return null
        }

        return subRoll to expanded
    }

    /**
     * Roll for NPC motive/topic with automatic follow-up.
     * If "History" → roll on History table
     * If "Focus" → roll on Focus table → expand if italic
     */
    fun rollMotiveWithFollowUp(): MotiveWithFollowUpResult {
        val roll = rollEngine.rollDie(10)
        val motive = NpcActionData.npcMotives[idx(roll)]

        val ideaData = if (motive == "History") {
            val historyResult = detailsGenerator.rollHistory()
            IdeaData(
                modifierRoll = historyResult.roll,
                modifier = historyResult.result,
                ideaRoll = historyResult.roll,
                idea = historyResult.result,
                ideaCategory = "History",
            )
        } else if (motive == "Focus") {
            val focusResult = nextSceneGenerator.rollFocus()
            val expansion = expandFocus(focusResult.focus)
            IdeaData(
                modifierRoll = focusResult.roll,
                modifier = focusResult.focus,
                ideaRoll = expansion?.first ?: focusResult.roll,
                idea = expansion?.second ?: focusResult.focus,
                ideaCategory = "Focus",
            )
        } else {
            null
        }

        return MotiveWithFollowUpResult(
            roll = roll,
            motive = motive,
            followUp = ideaData,
        )
    }

    /**
     * Roll for combat action with focus and objective.
     * Passive Focus: d6, Active Focus: d10
     * Defensive Objective: disadvantage, Offensive Objective: advantage
     */
    fun rollCombatAction(
        focus: String = "active",
        objective: String = "offensive",
    ): NpcActionResult {
        val dieSize = if (focus == "passive") 6 else 10
        val roll: Int
        val allRolls: List<Int>

        if (objective == "offensive") {
            val r = rollEngine.rollWithAdvantage(1, dieSize)
            roll = r.chosenSum
            allRolls = listOf(r.sum1, r.sum2)
        } else {
            val r = rollEngine.rollWithDisadvantage(1, dieSize)
            roll = r.chosenSum
            allRolls = listOf(r.sum1, r.sum2)
        }

        return NpcActionResult(
            column = "combat",
            roll = roll,
            result = NpcActionData.npcCombatActions[idx(roll)],
            dieSize = dieSize,
            allRolls = allRolls,
        )
    }

    // ── Profile Generation ──

    /**
     * Generate a full NPC profile.
     * Includes: 2 Personality traits + Need + Motive (with expansion) +
     * Color + Two Properties
     */
    fun generateProfile(needSkew: String = "none"): NpcProfileResult {
        val persRoll1 = rollEngine.rollDie(10)
        val persRoll2 = rollEngine.rollDie(10)
        val primaryPersonality = NpcActionData.npcPersonalities[idx(persRoll1)]
        val secondaryPersonality = NpcActionData.npcPersonalities[idx(persRoll2)]

        val needRoll: Int
        val needAllRolls: List<Int>
        if (needSkew == "complex") {
            val r = rollEngine.rollWithAdvantage(1, 10)
            needRoll = r.chosenSum
            needAllRolls = listOf(r.sum1, r.sum2)
        } else if (needSkew == "primitive") {
            val r = rollEngine.rollWithDisadvantage(1, 10)
            needRoll = r.chosenSum
            needAllRolls = listOf(r.sum1, r.sum2)
        } else {
            needRoll = rollEngine.rollDie(10)
            needAllRolls = emptyList()
        }

        val motiveRoll = rollEngine.rollDie(10)
        val need = NpcActionData.npcNeeds[idx(needRoll)]
        val motive = NpcActionData.npcMotives[idx(motiveRoll)]

        return NpcProfileResult(
            primaryPersonalityRoll = persRoll1,
            primaryPersonality = primaryPersonality,
            secondaryPersonalityRoll = persRoll2,
            secondaryPersonality = secondaryPersonality,
            needRoll = needRoll,
            need = need,
            motiveRoll = motiveRoll,
            motive = motive,
        )
    }

    /**
     * Generate a simple NPC profile (personality + need + motive only).
     * Used for NPCs like shop owners where full detail isn't needed.
     */
    fun generateSimpleProfile(needSkew: String = "none"): SimpleNpcProfileResult {
        val persRoll = rollEngine.rollDie(10)

        val needRoll: Int
        if (needSkew == "complex") {
            val r = rollEngine.rollWithAdvantage(1, 10)
            needRoll = r.chosenSum
        } else if (needSkew == "primitive") {
            val r = rollEngine.rollWithDisadvantage(1, 10)
            needRoll = r.chosenSum
        } else {
            needRoll = rollEngine.rollDie(10)
        }

        val motiveRoll = rollEngine.rollDie(10)

        return SimpleNpcProfileResult(
            personalityRoll = persRoll,
            personality = NpcActionData.npcPersonalities[idx(persRoll)],
            needRoll = needRoll,
            need = NpcActionData.npcNeeds[idx(needRoll)],
            motiveRoll = motiveRoll,
            motive = NpcActionData.npcMotives[idx(motiveRoll)],
        )
    }

    /** Generate a dual personality (primary + secondary traits). */
    fun rollDualPersonality(): DualPersonalityResult {
        val roll1 = rollEngine.rollDie(10)
        val roll2 = rollEngine.rollDie(10)

        return DualPersonalityResult(
            primaryRoll = roll1,
            primary = NpcActionData.npcPersonalities[idx(roll1)],
            secondaryRoll = roll2,
            secondary = NpcActionData.npcPersonalities[idx(roll2)],
        )
    }

    /**
     * Generate a complex NPC profile.
     * Includes: Name + 2 Personality traits + Need + Motive +
     * Color + Two Properties
     */
    fun generateComplexNpc(
        needSkew: String = "complex",
        includeName: Boolean = true,
        dualPersonality: Boolean = true,
    ): ComplexNpcResult {
        // Name (optional)
        val nameResult: NameResult? = if (includeName) {
            nameGenerator.generatePatternNeutral()
        } else null

        // Personality (1 or 2 traits)
        val persRoll1 = rollEngine.rollDie(10)
        val primaryPersonality = NpcActionData.npcPersonalities[idx(persRoll1)]
        val persRoll2: Int? = if (dualPersonality) rollEngine.rollDie(10) else null
        val secondaryPersonality: String? = persRoll2?.let { NpcActionData.npcPersonalities[idx(it)] }

        // Need with skew
        val needRoll: Int
        val needAllRolls: List<Int>
        if (needSkew == "complex") {
            val r = rollEngine.rollWithAdvantage(1, 10)
            needRoll = r.chosenSum
            needAllRolls = listOf(r.sum1, r.sum2)
        } else if (needSkew == "primitive") {
            val r = rollEngine.rollWithDisadvantage(1, 10)
            needRoll = r.chosenSum
            needAllRolls = listOf(r.sum1, r.sum2)
        } else {
            needRoll = rollEngine.rollDie(10)
            needAllRolls = emptyList()
        }

        val motiveRoll = rollEngine.rollDie(10)

        val need = NpcActionData.npcNeeds[idx(needRoll)]
        val motive = NpcActionData.npcMotives[idx(motiveRoll)]

        return ComplexNpcResult(
            name = nameResult,
            primaryPersonalityRoll = persRoll1,
            primaryPersonality = primaryPersonality,
            secondaryPersonalityRoll = persRoll2,
            secondaryPersonality = secondaryPersonality,
            needRoll = needRoll,
            need = need,
            needSkew = needSkew,
            needAllRolls = needAllRolls,
            motiveRoll = motiveRoll,
            motive = motive,
        )
    }
}
