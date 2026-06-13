package com.juiceroll.domain.engine

import kotlin.random.Random

/**
 * A table entry with a range of values and the result.
 */
data class TableEntry<T>(
    val minValue: Int,
    val maxValue: Int,
    val result: T
) {
    /** Returns true if [value] falls within this entry's range (inclusive). */
    fun matches(value: Int): Boolean = value in minValue..maxValue
}

/**
 * A lookup table that maps dice roll ranges to results.
 */
data class LookupTable<T>(
    val name: String,
    val entries: List<TableEntry<T>>
) {
    /**
     * Look up a result based on the roll value.
     * Scans entries in declaration order; returns first match, or null.
     */
    fun lookup(value: Int): T? {
        for (entry in entries) {
            if (entry.matches(value)) return entry.result
        }
        return null
    }

    /** Computed min/max range across all entries. Returns 0..0 if empty. */
    val range: Pair<Int, Int>
        get() {
            if (entries.isEmpty()) return 0 to 0
            val minVal = entries.minOf { it.minValue }
            val maxVal = entries.maxOf { it.maxValue }
            return minVal to maxVal
        }
}

/**
 * Result from a table lookup roll.
 */
data class TableLookupResult<T>(
    val tableName: String,
    val rollValue: Int,
    val result: T
) {
    override fun toString(): String = "$tableName: rolled $rollValue -> $result"
}

/**
 * Table roller that combines random dice rolling with table lookups.
 *
 * @param random The RNG source (default: `kotlin.random.Random`).
 */
class TableRoller(private val random: Random = Random) {

    /**
     * Roll [diceCount] dice of [diceSides] sides, sum the results,
     * add [modifier], and look up in [table].
     */
    fun <T> rollOnTable(
        table: LookupTable<T>,
        diceCount: Int,
        diceSides: Int,
        modifier: Int = 0
    ): TableLookupResult<T>? {
        val sum = (1..diceCount).sumOf { random.nextInt(diceSides) + 1 } + modifier
        val result = table.lookup(sum) ?: return null
        return TableLookupResult(
            tableName = table.name,
            rollValue = sum,
            result = result
        )
    }

    /** Convenience: rollOnTable with diceCount=1, diceSides=100. */
    fun <T> rollPercentile(table: LookupTable<T>, modifier: Int = 0): TableLookupResult<T>? =
        rollOnTable(table, diceCount = 1, diceSides = 100, modifier = modifier)

    /** Convenience: rollOnTable with diceCount=2, diceSides=6. */
    fun <T> roll2d6(table: LookupTable<T>, modifier: Int = 0): TableLookupResult<T>? =
        rollOnTable(table, diceCount = 2, diceSides = 6, modifier = modifier)
}
