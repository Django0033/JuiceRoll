package com.juiceroll.generator.character

import com.juiceroll.data.oracle.NameGeneratorData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.NameResult

/**
 * Name Generator preset for the Juice Oracle.
 * Generates fantasy names using d20 syllable tables.
 *
 * Based on Juice instructions (pages 61-64).
 *
 * **Simple Method**: Roll 3d20 on columns 1, 2, 3 for a random name.
 * Alternatively, roll on column 1 three times for a similar effect.
 *
 * **Pattern Method**: Roll 1d20 to select a pattern, then follow it.
 * - Roll with disadvantage (@-) on the pattern roll for masculine names
 * - Roll with advantage (@+) on the pattern roll for feminine names
 * - The pattern itself determines which columns/modifiers to use
 */
class NameGenerator(
    private val rollEngine: RollEngine = RollEngine
) {
    // ============================================================
    // SIMPLE METHOD - Roll 3d20 on columns 1, 2, 3
    // ============================================================

    fun generate(): NameResult {
        val roll1 = rollEngine.rollDie(20)
        val roll2 = rollEngine.rollDie(20)
        val roll3 = rollEngine.rollDie(20)
        return buildSimpleResult(roll1, roll2, roll3)
    }

    fun generateColumn1Only(): NameResult {
        val roll1 = rollEngine.rollDie(20)
        val roll2 = rollEngine.rollDie(20)
        val roll3 = rollEngine.rollDie(20)
        return buildColumn1Result(roll1, roll2, roll3)
    }

    private fun buildSimpleResult(roll1: Int, roll2: Int, roll3: Int): NameResult {
        val syl1 = NameGeneratorData.syllables1[roll1 - 1]
        val syl2 = NameGeneratorData.syllables2[roll2 - 1]
        val syl3 = NameGeneratorData.syllables3[roll3 - 1]

        val name = syl1 + syl2 + syl3
        val capitalizedName = name.replaceFirstChar { it.uppercaseChar() }

        return NameResult(
            rolls = listOf(roll1, roll2, roll3),
            syllables = listOf(syl1, syl2, syl3),
            name = capitalizedName,
            style = "neutral",
            method = "simple",
        )
    }

    private fun buildColumn1Result(roll1: Int, roll2: Int, roll3: Int): NameResult {
        val syl1 = NameGeneratorData.syllables1[roll1 - 1]
        val syl2 = if (roll2 <= 5) NameGeneratorData.syllables1Alt[roll2 - 1]
        else NameGeneratorData.syllables1[roll2 - 1]
        val syl3 = if (roll3 <= 5) NameGeneratorData.syllables1Alt[roll3 - 1]
        else NameGeneratorData.syllables1[roll3 - 1]

        val name = syl1 + syl2 + syl3
        val capitalizedName = name.replaceFirstChar { it.uppercaseChar() }

        return NameResult(
            rolls = listOf(roll1, roll2, roll3),
            syllables = listOf(syl1, syl2, syl3),
            name = capitalizedName,
            style = "neutral",
            method = "column1",
        )
    }

    // ============================================================
    // PATTERN METHOD - Roll 1d20 for pattern, then follow it
    // ============================================================

    fun generatePatternNeutral(): NameResult {
        val patternRoll = rollEngine.rollDie(20)
        return generateFromPattern(patternRoll, "neutral")
    }

    fun generateMasculine(): NameResult {
        val result = rollEngine.rollWithDisadvantage(1, 20)
        return generateFromPattern(result.chosenSum, "masculine")
    }

    fun generateFeminine(): NameResult {
        val result = rollEngine.rollWithAdvantage(1, 20)
        return generateFromPattern(result.chosenSum, "feminine")
    }

    private fun generateFromPattern(patternRoll: Int, style: String): NameResult {
        val pattern = NameGeneratorData.patterns[patternRoll.coerceIn(1, 20) - 1]
        val rolls = mutableListOf(patternRoll)
        val syllables = mutableListOf<String>()
        var suffix = ""

        var i = 0
        while (i < pattern.length) {
            val char = pattern[i]
            when (char) {
                '1' -> {
                    val roll = rollEngine.rollDie(20)
                    rolls.add(roll)
                    if (syllables.isEmpty() && roll <= 5) {
                        syllables.add(NameGeneratorData.syllables1[roll - 1])
                    } else if (roll <= 5) {
                        syllables.add(NameGeneratorData.syllables1Alt[roll - 1])
                    } else {
                        syllables.add(NameGeneratorData.syllables1[roll - 1])
                    }
                }
                '2' -> {
                    val roll = rollEngine.rollDie(20)
                    rolls.add(roll)
                    syllables.add(NameGeneratorData.syllables2[roll - 1])
                }
                '3' -> {
                    val hasPlus = i + 1 < pattern.length && pattern[i + 1] == '+'
                    val hasMinus = i + 1 < pattern.length && pattern[i + 1] == '-'
                    val roll = if (hasPlus) {
                        i++
                        rollEngine.rollDie(10) + 10
                    } else if (hasMinus) {
                        i++
                        rollEngine.rollDie(10)
                    } else {
                        rollEngine.rollDie(20)
                    }
                    rolls.add(roll)
                    syllables.add(NameGeneratorData.syllables3[roll - 1])
                }
                'o', 'a', 'i' -> {
                    suffix = char.toString()
                }
            }
            i++
        }

        val name = syllables.joinToString("") + suffix
        val capitalizedName = name.replaceFirstChar { it.uppercaseChar() }

        return NameResult(
            rolls = rolls,
            syllables = syllables,
            name = capitalizedName,
            style = style,
            method = "pattern",
            pattern = pattern,
        )
    }
}
