package com.juiceroll.generator.world

import com.juiceroll.data.oracle.DungeonData
import com.juiceroll.data.oracle.WildernessData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.DungeonAreaResult
import com.juiceroll.domain.model.DungeonDetailResult
import com.juiceroll.domain.model.DungeonEncounterResult
import com.juiceroll.domain.model.DungeonMonsterResult
import com.juiceroll.domain.model.DungeonNameResult
import com.juiceroll.domain.model.DungeonTrapResult
import com.juiceroll.domain.model.FullDungeonAreaResult
import com.juiceroll.domain.model.TrapProcedureResult
import com.juiceroll.domain.model.TwoPassAreaResult

/**
 * Dungeon Generator preset for the Juice Oracle.
 * Uses a two-phase stateful generation system.
 *
 * Heading: NA: 1d10@- Until Doubles, Then NA: 1d10@+
 *
 * Phase 1 (Entering): Roll 1d10 with disadvantage (@-)
 * Phase 2 (Exploring): Roll 1d10 with advantage (@+)
 *
 * Two-Pass method: starts with advantage (@+), switches to disadvantage (@-) after first doubles.
 */
class DungeonGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    /** Generate a dungeon name (3d10). Format: "[Dungeon] of the [Description] [Subject]" */
    fun generateName(): DungeonNameResult {
        val typeRoll = rollEngine.rollDie(10)
        val descRoll = rollEngine.rollDie(10)
        val subjRoll = rollEngine.rollDie(10)

        val dungeonType = DungeonData.dungeonTypes[typeRoll - 1]
        val description = DungeonData.dungeonDescriptions[descRoll - 1]
        val subject = DungeonData.dungeonSubjects[subjRoll - 1]

        return DungeonNameResult(
            typeRoll = typeRoll,
            dungeonType = dungeonType,
            descriptionRoll = descRoll,
            descriptionWord = description,
            subjectRoll = subjRoll,
            subject = subject,
        )
    }

    /**
     * Generate the next dungeon area.
     * @param isEntering true = entering (1d10@-), false = exploring (1d10@+)
     */
    fun generateNextArea(
        isEntering: Boolean = true,
    ): DungeonAreaResult {
        val result = if (isEntering) {
            rollEngine.rollWithDisadvantage(1, 10)
        } else {
            rollEngine.rollWithAdvantage(1, 10)
        }

        val areaRoll = result.chosenSum
        val areaType = DungeonData.dungeonAreaTypes[areaRoll - 1]
        val isDoubles = result.sum1 == result.sum2

        return DungeonAreaResult(
            phase = if (isEntering) "entering" else "exploring",
            roll1 = result.sum1,
            roll2 = result.sum2,
            chosenRoll = areaRoll,
            areaType = areaType,
            isDoubles = isDoubles,
            phaseChange = isDoubles,
        )
    }

    /**
     * Generate passage details.
     * @param useD6 for linear dungeons (d6), d10 for branching dungeons (d10)
     * @param skew determines dungeon size: disadvantage = smaller, advantage = larger
     */
    fun generatePassage(useD6: Boolean = false, skew: String = "none"): DungeonDetailResult {
        val dieSize = if (useD6) 6 else 10
        val (roll, diceResults) = when (skew) {
            "advantage" -> {
                val r = rollEngine.rollWithAdvantage(1, dieSize)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            "disadvantage" -> {
                val r = rollEngine.rollWithDisadvantage(1, dieSize)
                r.chosenSum to listOf(r.sum1, r.sum2)
            }
            else -> rollEngine.rollDie(dieSize) to listOf(rollEngine.rollDie(dieSize))
        }

        val passage = DungeonData.dungeonPassageTypes[roll - 1]

        return DungeonDetailResult(
            detailType = "Passage",
            roll = roll,
            result = passage,
        )
    }

    /**
     * Generate room condition.
     * @param useD6 for unoccupied areas (d6), d10 for occupied areas
     * @param skew determines condition quality: disadvantage = worse, advantage = better
     */
    fun generateCondition(useD6: Boolean = false, skew: String = "none"): DungeonDetailResult {
        val dieSize = if (useD6) 6 else 10
        val roll = when (skew) {
            "advantage" -> rollEngine.rollWithAdvantage(1, dieSize).chosenSum
            "disadvantage" -> rollEngine.rollWithDisadvantage(1, dieSize).chosenSum
            else -> rollEngine.rollDie(dieSize)
        }

        val condition = DungeonData.dungeonRoomConditions[roll - 1]

        return DungeonDetailResult(
            detailType = "Condition",
            roll = roll,
            result = condition,
        )
    }

    /**
     * Generate a complete area (area type + condition).
     */
    fun generateFullArea(
        isEntering: Boolean = true,
        isOccupied: Boolean = true,
        conditionSkew: String = "none",
    ): FullDungeonAreaResult {
        val area = generateNextArea(isEntering = isEntering)
        val condition = generateCondition(useD6 = !isOccupied, skew = conditionSkew)

        return FullDungeonAreaResult(
            areaType = area.areaType,
            conditionResult = condition.result,
        )
    }

    /** Roll for dungeon encounter type. */
    fun rollEncounterType(isLingering: Boolean = false, skew: String = "none"): DungeonDetailResult {
        val dieSize = if (isLingering) 6 else 10
        val roll = when (skew) {
            "advantage" -> rollEngine.rollWithAdvantage(1, dieSize).chosenSum
            "disadvantage" -> rollEngine.rollWithDisadvantage(1, dieSize).chosenSum
            else -> rollEngine.rollDie(dieSize)
        }

        val encounterType = DungeonData.dungeonEncounterTypes[roll - 1]

        return DungeonDetailResult(
            detailType = "Encounter",
            roll = roll,
            result = encounterType,
        )
    }

    /** Generate a monster description (2d10 for descriptor + ability). */
    fun rollMonsterDescription(): DungeonMonsterResult {
        val descRoll = rollEngine.rollDie(10)
        val abilityRoll = rollEngine.rollDie(10)

        return DungeonMonsterResult(
            descriptorRoll = descRoll,
            descriptor = DungeonData.dungeonMonsterDescriptors[descRoll - 1],
            abilityRoll = abilityRoll,
            ability = DungeonData.dungeonMonsterAbilities[abilityRoll - 1],
        )
    }

    /** Generate a trap (2d10 for action + subject). */
    fun rollTrap(): DungeonTrapResult {
        val actionRoll = rollEngine.rollDie(10)
        val subjectRoll = rollEngine.rollDie(10)

        return DungeonTrapResult(
            actionRoll = actionRoll,
            action = DungeonData.dungeonTrapActions[actionRoll - 1],
            subjectRoll = subjectRoll,
            subject = DungeonData.dungeonTrapSubjects[subjectRoll - 1],
        )
    }

    /**
     * Full Trap Procedure.
     * 1. Decide if searching (10 min) or not
     * 2. If searching: Active Perception @+ vs DC
     *    - Pass: AVOID, Fail: LOCATE
     * 3. If NOT searching: Passive Perception vs DC
     *    - Pass: LOCATE, Fail: TRIGGER
     */
    fun rollTrapProcedure(
        isSearching: Boolean = true,
        dcSkew: String = "none",
    ): TrapProcedureResult {
        val trap = rollTrap()

        val (dcRoll, dcRolls) = when (dcSkew) {
            "advantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                // Higher roll = lower DC = easier
                val chosen = if (r1 > r2) r1 else r2
                chosen to listOf(r1, r2)
            }
            "disadvantage" -> {
                val r1 = rollEngine.rollDie(10)
                val r2 = rollEngine.rollDie(10)
                val chosen = if (r1 < r2) r1 else r2
                chosen to listOf(r1, r2)
            }
            else -> {
                val r = rollEngine.rollDie(10)
                r to listOf(r)
            }
        }

        // Roll 1 = DC 17, Roll 10 = DC 8
        val dcValues = listOf(17, 16, 15, 14, 13, 12, 11, 10, 9, 8)
        val dcIndex = if (dcRoll == 10) 9 else dcRoll - 1
        val dc = dcValues[dcIndex]

        return TrapProcedureResult(
            trapAction = trap.action,
            trapSubject = trap.subject,
            isSearching = isSearching,
            dcRoll = dcRoll,
            dcRolls = dcRolls,
            dc = dc,
            dcSkew = dcSkew,
        )
    }

    /** Roll for a dungeon feature (1d10). */
    fun rollFeature(): DungeonDetailResult {
        val roll = rollEngine.rollDie(10)
        val feature = DungeonData.dungeonFeatureTypes[roll - 1]

        return DungeonDetailResult(
            detailType = "Feature",
            roll = roll,
            result = feature,
        )
    }

    /** Roll for a natural hazard (1d10 on first entry, 1d6 when lingering). */
    fun rollNaturalHazard(isLingering: Boolean = false): DungeonDetailResult {
        val dieSize = if (isLingering) 6 else 10
        val roll = rollEngine.rollDie(dieSize)
        val hazard = WildernessData.wildernessNaturalHazards[roll - 1]

        return DungeonDetailResult(
            detailType = "Natural Hazard",
            roll = roll,
            result = hazard,
        )
    }

    /** Generate a full dungeon encounter based on encounter type. */
    fun rollFullEncounter(
        isLingering: Boolean = false,
        skew: String = "none",
    ): DungeonEncounterResult {
        val encounterTypeRoll = rollEncounterType(isLingering = isLingering, skew = skew)
        val encounterType = encounterTypeRoll.result

        var monsterDescriptor: String? = null
        var monsterAbility: String? = null
        var trapAction: String? = null
        var trapSubject: String? = null

        if (encounterType == "Monster") {
            val monster = rollMonsterDescription()
            monsterDescriptor = monster.descriptor
            monsterAbility = monster.ability
        } else if (encounterType == "Trap") {
            val trap = rollTrap()
            trapAction = trap.action
            trapSubject = trap.subject
        }

        return DungeonEncounterResult(
            encounterType = encounterType,
            monsterDescriptor = monsterDescriptor,
            monsterAbility = monsterAbility,
            trapAction = trapAction,
            trapSubject = trapSubject,
        )
    }

    /**
     * Two-Pass method: generates areas with the opposite skew sequence.
     * Starts with ADVANTAGE (@+), switches to DISADVANTAGE (@-) after first doubles.
     * Second doubles: stop generating.
     */
    fun generateTwoPassArea(
        hasFirstDoubles: Boolean = false,
    ): TwoPassAreaResult {
        val useAdvantage = !hasFirstDoubles

        val result = if (useAdvantage) {
            rollEngine.rollWithAdvantage(1, 10)
        } else {
            rollEngine.rollWithDisadvantage(1, 10)
        }

        val areaRoll = result.chosenSum
        val areaType = DungeonData.dungeonAreaTypes[areaRoll - 1]
        val isDoubles = result.sum1 == result.sum2

        return TwoPassAreaResult(
            roll1 = result.sum1,
            roll2 = result.sum2,
            chosenRoll = areaRoll,
            areaType = areaType,
            isDoubles = isDoubles,
            hadFirstDoubles = hasFirstDoubles,
            isSecondDoubles = hasFirstDoubles && isDoubles,
            stopMapGeneration = hasFirstDoubles && isDoubles,
        )
    }
}
