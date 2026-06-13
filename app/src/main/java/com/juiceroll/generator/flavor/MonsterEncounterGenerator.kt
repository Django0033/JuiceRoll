package com.juiceroll.generator.flavor

import com.juiceroll.data.oracle.MonsterEncounterData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.FullMonsterEncounterResult
import com.juiceroll.domain.model.MonsterCountData
import com.juiceroll.domain.model.MonsterEncounterResult
import com.juiceroll.domain.model.MonsterTracksResult

/**
 * Monster encounter tables from wilderness exploration.
 *
 * Uses MonsterEncounterData for table data. Converts from Flutter's static-only
 * MonsterEncounter class to a class with constructor-injected RollEngine.
 */
class MonsterEncounterGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    /** Convert difficulty enum value to column index */
    private fun difficultyToColumn(difficulty: String): Int {
        return when (difficulty) {
            "easy" -> 1
            "medium" -> 2
            "hard" -> 3
            "boss" -> 4
            else -> 2
        }
    }

    /** Roll for monster tracks (1d6-1 modifier, d10 with disadvantage for row). */
    fun rollTracks(row: Int? = null): MonsterTracksResult {
        val trackRoll = rollEngine.rollDie(6)
        val modifier = trackRoll - 1
        val allDice = mutableListOf(trackRoll)

        val actualRow: Int
        if (row != null) {
            actualRow = row.coerceIn(0, 11)
        } else {
            val rowRoll = rollEngine.rollWithDisadvantage(1, 10)
            actualRow = if (rowRoll.chosenSum == 10) 9 else rowRoll.chosenSum - 1
            allDice.addAll(listOf(rowRoll.sum1, rowRoll.sum2))
        }

        val tracksCode = MonsterEncounterData.monsterTable[actualRow][0]
        val tracksName = MonsterEncounterData.monsterFullNames[tracksCode] ?: tracksCode

        return MonsterTracksResult(
            diceResults = allDice,
            row = actualRow,
            tracks = tracksName,
            modifier = modifier,
        )
    }

    /** Roll for a monster encounter using 2d10 (row + difficulty, doubles = boss). */
    fun rollEncounter(
        forcedRow: Int? = null,
        forcedDifficulty: String? = null,
    ): MonsterEncounterResult {
        val allDice = mutableListOf<Int>()
        val row: Int
        val difficulty: String
        var wasDoubles = false
        var difficultyRollValue: Int? = null

        if (forcedRow != null && forcedDifficulty != null) {
            row = forcedRow.coerceIn(0, 11)
            difficulty = forcedDifficulty
        } else {
            val die1 = rollEngine.rollDie(10)
            val die2 = rollEngine.rollDie(10)
            allDice.addAll(listOf(die1, die2))

            val rowRoll = die1
            val diffRoll = die2
            difficultyRollValue = diffRoll

            wasDoubles = rowRoll == diffRoll

            row = forcedRow ?: (if (rowRoll == 10) 9 else rowRoll - 1)

            difficulty = when {
                wasDoubles -> "boss"
                forcedDifficulty != null -> forcedDifficulty
                diffRoll in 1..4 -> "easy"
                diffRoll in 5..8 -> "medium"
                else -> "hard"
            }
        }

        val columnIndex = difficultyToColumn(difficulty)
        val monsterCode = MonsterEncounterData.monsterTable[row][columnIndex]
        val monsterName = MonsterEncounterData.monsterFullNames[monsterCode] ?: monsterCode
        val isDeadly = monsterCode.startsWith("-") || difficulty == "boss"

        return MonsterEncounterResult(
            diceResults = allDice,
            row = row,
            difficulty = difficulty,
            monster = monsterName,
            isDeadly = isDeadly,
            difficultyRoll = difficultyRollValue,
            wasDoubles = wasDoubles,
        )
    }

    /** Roll for a specific row with specified difficulty. */
    fun getMonster(row: Int, difficulty: String): MonsterEncounterResult {
        val clampedRow = row.coerceIn(0, 11)
        val columnIndex = difficultyToColumn(difficulty)
        val monsterCode = MonsterEncounterData.monsterTable[clampedRow][columnIndex]
        val monsterName = MonsterEncounterData.monsterFullNames[monsterCode] ?: monsterCode
        val isDeadly = monsterCode.startsWith("-") || difficulty == "boss"

        return MonsterEncounterResult(
            diceResults = emptyList(),
            row = clampedRow,
            difficulty = difficulty,
            monster = monsterName,
            isDeadly = isDeadly,
        )
    }

    /** Roll for special rows (* or **). Row 10 = * (nature/plants), Row 11 = ** (humanoids). */
    fun rollSpecialRow(humanoid: Boolean = false, difficulty: String? = null): MonsterEncounterResult {
        val row = if (humanoid) 11 else 10

        if (difficulty != null) {
            return getMonster(row, difficulty)
        }

        val diffRoll = rollEngine.rollDie(10)
        val actualDifficulty = when {
            diffRoll in 1..4 -> "easy"
            diffRoll in 5..8 -> "medium"
            else -> "hard"
        }

        val columnIndex = difficultyToColumn(actualDifficulty)
        val monsterCode = MonsterEncounterData.monsterTable[row][columnIndex]
        val monsterName = MonsterEncounterData.monsterFullNames[monsterCode] ?: monsterCode
        val isDeadly = monsterCode.startsWith("-")

        return MonsterEncounterResult(
            diceResults = listOf(diffRoll),
            row = row,
            difficulty = actualDifficulty,
            monster = monsterName,
            isDeadly = isDeadly,
            difficultyRoll = diffRoll,
        )
    }

    /** Get display name for difficulty. */
    fun difficultyName(difficulty: String): String {
        return when (difficulty) {
            "easy" -> "Easy (1-4)"
            "medium" -> "Medium (5-8)"
            "hard" -> "Hard (9-0)"
            "boss" -> "Boss (Doubles)"
            else -> difficulty
        }
    }

    /** Get the formula string for an environment. */
    fun getEnvironmentFormula(environmentRow: Int): String {
        val idx = (environmentRow - 1).coerceIn(0, 9)
        val formula = MonsterEncounterData.environmentFormulas[idx]
        val mod = formula["modifier"] as Int
        val adv = formula["advantage"] as String
        return "+$mod@$adv"
    }

    /**
     * Internal result for monster row by environment calculation.
     */
    data class MonsterRowResult(
        val row: Int,
        val dice: List<Int>,
        val wasDoubles: Boolean,
        val isForest: Boolean,
    )

    /** Roll for monster row based on environment formula. */
    fun rollMonsterRowByEnvironment(environmentRow: Int): MonsterRowResult {
        val idx = (environmentRow - 1).coerceIn(0, 9)
        val formula = MonsterEncounterData.environmentFormulas[idx]
        val modifier = formula["modifier"] as Int
        val advantageType = formula["advantage"] as String
        val isForest = environmentRow == 6

        var baseRoll: Int
        var secondRoll: Int? = null
        var wasDoubles = false

        when (advantageType) {
            "+" -> {
                val die1 = rollEngine.rollDie(6)
                val die2 = rollEngine.rollDie(6)
                wasDoubles = die1 == die2
                baseRoll = if (wasDoubles) die1 else maxOf(die1, die2)
                secondRoll = die2
            }
            "-" -> {
                val die1 = rollEngine.rollDie(6)
                val die2 = rollEngine.rollDie(6)
                wasDoubles = die1 == die2
                baseRoll = if (wasDoubles) die1 else minOf(die1, die2)
                secondRoll = die2
            }
            else -> {
                baseRoll = rollEngine.rollDie(6)
            }
        }

        // If doubles, this is a Bandit encounter
        if (wasDoubles) {
            return MonsterRowResult(
                row = 11,
                dice = if (secondRoll != null) listOf(baseRoll, secondRoll) else listOf(baseRoll),
                wasDoubles = true,
                isForest = isForest,
            )
        }

        var computedRow = (baseRoll + modifier).coerceIn(1, 10) - 1

        // Forest special case: row 6 (index 5) = use Blights (* row)
        if (isForest && computedRow == 5) {
            computedRow = 10
        }

        return MonsterRowResult(
            row = computedRow,
            dice = if (secondRoll != null) listOf(baseRoll, secondRoll) else listOf(baseRoll),
            wasDoubles = false,
            isForest = isForest,
        )
    }

    /**
     * Roll for the number of a specific monster type.
     * Uses 1d6-1 with advantage/disadvantage based on skew symbol.
     * Note: In Juice, "+" means advantage (take lower die = fewer monsters).
     */
    fun rollMonsterCount(skewSymbol: String): Int {
        val baseRoll = when (skewSymbol) {
            "+" -> {
                // Advantage in Juice: take LOWER die (fewer monsters)
                rollEngine.rollWithDisadvantage(1, 6).chosenSum
            }
            "-" -> {
                // Disadvantage in Juice: take HIGHER die (more monsters)
                rollEngine.rollWithAdvantage(1, 6).chosenSum
            }
            else -> rollEngine.rollDie(6)
        }

        return (baseRoll - 1).coerceIn(0, 5)
    }

    /**
     * Generate a full monster encounter based on environment.
     * 1. Roll monster row using environment formula
     * 2. Roll 2d10 for difficulty (doubles = boss)
     * 3. Roll counts for each monster type
     */
    fun generateFullEncounter(environmentRow: Int): FullMonsterEncounterResult {
        val allDice = mutableListOf<Int>()

        // Step 1: Roll for monster row
        val rowResult = rollMonsterRowByEnvironment(environmentRow)
        allDice.addAll(rowResult.dice)
        val row = rowResult.row
        val monsterRowDoubles = rowResult.wasDoubles
        val isForest = rowResult.isForest

        // Step 2: Roll 2d10 for difficulty
        val diffDie1 = rollEngine.rollDie(10)
        val diffDie2 = rollEngine.rollDie(10)
        allDice.addAll(listOf(diffDie1, diffDie2))

        val wasDoubles = diffDie1 == diffDie2
        val diffRoll = diffDie1

        val difficulty = when {
            wasDoubles -> "boss"
            diffRoll in 1..4 -> "easy"
            diffRoll in 5..8 -> "medium"
            else -> "hard"
        }

        // Step 3: Determine monsters
        val maxColumn = difficultyToColumn(difficulty)
        val hasBoss = wasDoubles
        var bossMonster: String? = null

        if (hasBoss) {
            val bossCode = MonsterEncounterData.monsterTable[row][4]
            bossMonster = MonsterEncounterData.monsterFullNames[bossCode] ?: bossCode
        }

        val monsters = mutableListOf<MonsterCountData>()
        for (col in 1..maxColumn.coerceIn(1, 3)) {
            val monsterCode = MonsterEncounterData.monsterTable[row][col]
            val monsterName = MonsterEncounterData.monsterFullNames[monsterCode] ?: monsterCode

            val skewSymbol = when {
                monsterCode.startsWith("+ ") -> "+"
                monsterCode.startsWith("- ") -> "-"
                else -> ""
            }

            val count = rollMonsterCount(skewSymbol)
            monsters.add(
                MonsterCountData(
                    code = monsterCode,
                    name = monsterName,
                    count = count,
                    skewSymbol = skewSymbol,
                )
            )
        }

        return FullMonsterEncounterResult(
            diceResults = allDice,
            row = row,
            difficulty = difficulty,
            hasBoss = hasBoss,
            bossMonster = bossMonster,
            monsters = monsters,
            environmentRow = environmentRow,
            environmentFormula = getEnvironmentFormula(environmentRow),
            wasDoubles = wasDoubles || monsterRowDoubles,
        )
    }
}
