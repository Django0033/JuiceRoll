package com.juiceroll.generator.world

import com.juiceroll.data.oracle.WildernessData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.MonsterLevelResult
import com.juiceroll.domain.model.WildernessAreaResult
import com.juiceroll.domain.model.WildernessDetailResult
import com.juiceroll.domain.model.WildernessEncounterResult
import com.juiceroll.domain.model.WildernessState
import com.juiceroll.domain.model.WildernessWeatherResult

/**
 * Wilderness Generator preset for the Juice Oracle.
 * Uses wilderness-table.md for terrain, encounters, and monster generation.
 *
 * Formula: 2dF Env → 1dF Type; W: 1d6@E+T; M: 1d6+E
 *
 * **Stateless Design:**
 * All methods take state as input and return results that include any new state.
 */
class WildernessGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    /** Initialize wilderness with a random starting environment. */
    fun initializeRandom(): WildernessAreaResult {
        val envRoll = rollEngine.rollDie(10)
        val envRow = if (envRoll == 10) 10 else envRoll

        val typeFate = rollEngine.rollFateDice(1)[0]
        val typeRow = clampTypeRow(envRow, typeFate)

        return WildernessAreaResult(
            envFateDice = listOf(0, 0),
            envRoll = envRoll,
            environment = WildernessData.wildernessEnvironments[envRow - 1],
            typeFateDie = typeFate,
            typeRoll = typeRow,
            typeName = (WildernessData.wildernessTypes[typeRow - 1]["name"] as String),
            typeModifier = (WildernessData.wildernessTypes[typeRow - 1]["modifier"] as Int),
            isTransition = false,
            previousEnvironment = null,
            newState = WildernessState(
                environmentRow = envRow,
                typeRow = typeRow,
                isLost = false,
            ),
        )
    }

    /** Initialize wilderness at a specific environment. */
    fun initializeAt(environmentRow: Int, typeRow: Int? = null, isLost: Boolean = false): WildernessAreaResult {
        val clampedEnv = environmentRow.coerceIn(1, 10)
        val clampedType = (typeRow ?: clampedEnv).coerceIn(1, 10)

        return WildernessAreaResult(
            envFateDice = listOf(0, 0),
            envRoll = clampedEnv,
            environment = WildernessData.wildernessEnvironments[clampedEnv - 1],
            typeFateDie = 0,
            typeRoll = clampedType,
            typeName = (WildernessData.wildernessTypes[clampedType - 1]["name"] as String),
            typeModifier = (WildernessData.wildernessTypes[clampedType - 1]["modifier"] as Int),
            isTransition = false,
            previousEnvironment = null,
            isManualSet = true,
            newState = WildernessState(
                environmentRow = clampedEnv,
                typeRow = clampedType,
                isLost = isLost,
            ),
        )
    }

    /** Transition to a new area using 2dF for environment + 1dF for type. */
    fun transition(currentState: WildernessState?): WildernessAreaResult {
        if (currentState == null) return initializeRandom()

        val previousEnv = currentState.environmentRow
        val previousEnvName = WildernessData.wildernessEnvironments[previousEnv - 1]

        val envFateDice = rollEngine.rollFateDice(2)
        val envOffset = envFateDice[0] + envFateDice[1]
        val newEnvRow = (previousEnv + envOffset).coerceIn(1, 10)

        val typeFate = rollEngine.rollFateDice(1)[0]
        val newTypeRow = clampTypeRow(newEnvRow, typeFate)

        return WildernessAreaResult(
            envFateDice = envFateDice,
            envRoll = newEnvRow,
            environment = WildernessData.wildernessEnvironments[newEnvRow - 1],
            typeFateDie = typeFate,
            typeRoll = newTypeRow,
            typeName = (WildernessData.wildernessTypes[newTypeRow - 1]["name"] as String),
            typeModifier = (WildernessData.wildernessTypes[newTypeRow - 1]["modifier"] as Int),
            isTransition = true,
            previousEnvironment = previousEnvName,
            newState = WildernessState(
                environmentRow = newEnvRow,
                typeRow = newTypeRow,
                isLost = currentState.isLost,
            ),
        )
    }

    /** Clamp type row so it doesn't wrap at edges. */
    fun clampTypeRow(envRow: Int, fateOffset: Int): Int {
        return (envRow + fateOffset).coerceIn(1, 10)
    }

    /** Roll for a wilderness encounter. Uses d10 when oriented, d6 when lost. */
    fun rollEncounter(
        currentState: WildernessState?,
        hasDangerousTerrain: Boolean? = null,
        hasMapOrGuide: Boolean? = null,
    ): WildernessEncounterResult {
        val isLost = currentState?.isLost ?: false
        val dieSize = if (isLost) 6 else 10

        val dangerSkew = if (hasDangerousTerrain == true) -1 else 0
        val guideSkew = if (hasMapOrGuide == true) 1 else 0
        val netSkew = dangerSkew + guideSkew

        val (roll, secondRoll, skewUsed) = when {
            netSkew > 0 -> {
                val r = rollEngine.rollWithAdvantage(1, dieSize)
                Triple(r.chosenSum, r.sum2, "advantage")
            }
            netSkew < 0 -> {
                val r = rollEngine.rollWithDisadvantage(1, dieSize)
                Triple(r.chosenSum, r.sum2, "disadvantage")
            }
            else -> Triple(rollEngine.rollDie(dieSize), null, "straight")
        }

        val index = if (roll == 10) 9 else roll - 1
        val encounterData = WildernessData.wildernessEncounters[index]
        val encounter = encounterData["name"] as String
        val isItalic = encounterData["isItalic"] as Boolean
        val partialItalic = encounterData["partialItalic"] as String?

        var becameLost = false
        var becameFound = false
        var newState: WildernessState? = null

        if (currentState != null) {
            if (encounter == "Destination/Lost" && !currentState.isLost) {
                becameLost = true
            } else if (encounter == "River/Road" && currentState.isLost) {
                becameFound = true
                newState = currentState.copy(isLost = false)
            }
        }

        return WildernessEncounterResult(
            roll = roll,
            secondRoll = secondRoll,
            encounter = encounter,
            requiresFollowUp = requiresFollowUp(encounter),
            dieSize = dieSize,
            skewUsed = skewUsed,
            wasLost = isLost,
            becameLost = becameLost,
            becameFound = becameFound,
            isItalic = isItalic,
            partialItalic = partialItalic,
            newState = newState,
        )
    }

    private fun requiresFollowUp(encounter: String): Boolean {
        return encounter == "Natural Hazard" ||
                encounter == "Monster" ||
                encounter == "Weather" ||
                encounter == "Challenge" ||
                encounter == "Dungeon" ||
                encounter == "Feature"
    }

    /** Roll for weather using formula: 1d6@environment_skew + type_modifier. */
    fun rollWeather(environmentRow: Int, typeRow: Int): WildernessWeatherResult {
        val envRow = environmentRow.coerceIn(1, 10)
        val tRow = typeRow.coerceIn(1, 10)

        val envSkew = WildernessData.wildernessTypes[envRow - 1]["skew"] as String
        val typeModifier = WildernessData.wildernessTypes[tRow - 1]["modifier"] as Int

        val (baseRoll, secondRoll) = when (envSkew) {
            "+" -> {
                val r = rollEngine.rollWithAdvantage(1, 6)
                r.chosenSum to (r.sum2 as Int?)
            }
            "-" -> {
                val r = rollEngine.rollWithDisadvantage(1, 6)
                r.chosenSum to (r.sum2 as Int?)
            }
            else -> rollEngine.rollDie(6) to null
        }

        val weatherRow = (baseRoll + typeModifier).coerceIn(1, 10)
        val weather = WildernessData.wildernessWeatherTypes[weatherRow - 1]

        return WildernessWeatherResult(
            baseRoll = baseRoll,
            secondRoll = secondRoll,
            weatherRow = weatherRow,
            weather = weather,
            typeModifier = typeModifier,
            environmentSkew = envSkew,
            environment = WildernessData.wildernessEnvironments[envRow - 1],
            typeName = WildernessData.wildernessTypes[tRow - 1]["name"] as String,
        )
    }

    /** Convenience method that takes WildernessState for weather roll. */
    fun rollWeatherFromState(state: WildernessState): WildernessWeatherResult {
        return rollWeather(environmentRow = state.environmentRow, typeRow = state.typeRow)
    }

    /** Roll for natural hazard. */
    fun rollNaturalHazard(): WildernessDetailResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val hazard = WildernessData.wildernessNaturalHazards[index]

        return WildernessDetailResult(
            detailType = "Natural Hazard",
            roll = roll,
            result = hazard,
        )
    }

    /** Roll for wilderness feature. */
    fun rollFeature(): WildernessDetailResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val feature = WildernessData.wildernessFeatures[index]

        return WildernessDetailResult(
            detailType = "Feature",
            roll = roll,
            result = feature,
        )
    }

    /** Calculate monster level using formula from the environment. M: 1d6+E */
    fun rollMonsterLevel(environmentRow: Int): MonsterLevelResult {
        val envIndex = (environmentRow - 1).coerceIn(0, 9)
        val formula = WildernessData.wildernessMonsterFormulas[envIndex]
        val modifier = formula["modifier"] as Int
        val advantageType = formula["advantage"] as String

        val (baseRoll, secondRoll) = when (advantageType) {
            "+" -> {
                val r = rollEngine.rollWithAdvantage(1, 6)
                r.chosenSum to (r.sum2 as Int?)
            }
            "-" -> {
                val r = rollEngine.rollWithDisadvantage(1, 6)
                r.chosenSum to (r.sum2 as Int?)
            }
            else -> rollEngine.rollDie(6) to null
        }

        val monsterLevel = baseRoll + modifier

        return MonsterLevelResult(
            baseRoll = baseRoll,
            secondRoll = secondRoll,
            modifier = modifier,
            advantageType = advantageType,
            monsterLevel = monsterLevel,
        )
    }

    /** Convenience method that takes WildernessState for monster level roll. */
    fun rollMonsterLevelFromState(state: WildernessState): MonsterLevelResult {
        return rollMonsterLevel(environmentRow = state.environmentRow)
    }

    /** Legacy method - generates a random wilderness area. */
    fun generateArea(): WildernessAreaResult = initializeRandom()
}
