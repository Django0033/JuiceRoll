package com.juiceroll.generator.character

import com.juiceroll.data.oracle.DialogGeneratorData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.DialogGridMoveResult
import com.juiceroll.domain.model.DialogGridState
import com.juiceroll.domain.model.DialogResult

/**
 * Dialog Generator preset for the Juice Oracle.
 *
 * The Dialog Grid is a 5x5 grid-based mini-game for generating NPC conversations.
 * You maintain state (position) throughout the conversation, moving around the grid
 * as dice are rolled.
 *
 * **IMPORTANT**: This generator is stateless. State is accepted as [DialogGridState]
 * and returned as part of [DialogGridMoveResult]. The ViewModel holds the state.
 *
 * How it works:
 * - Start at center "Fact" position (row 2, col 2)
 * - Roll 2d10: First die = direction/tone, Second die = subject
 * - If doubles: conversation ends
 * - Move on grid based on first die, wrap at edges
 * - Top 2 rows (0,1) = past tense
 * - Bottom 3 rows (2,3,4) = present tense
 */
class DialogGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    private val grid: List<List<String>> = DialogGeneratorData.grid
    private val fragmentDescriptions: Map<String, String> = DialogGeneratorData.fragmentDescriptions

    /**
     * Generate a dialog roll (2d10) from the given state, move on the grid,
     * and return the result with the new state.
     */
    fun roll(state: DialogGridState): DialogGridMoveResult {
        val active = if (!state.conversationActive) {
            DialogGridState(currentRow = 2, currentCol = 2, conversationActive = true)
        } else {
            state
        }

        val directionRoll = rollEngine.rollDie(10)
        val subjectRoll = rollEngine.rollDie(10)
        val isDoubles = directionRoll == subjectRoll

        val direction = getDirection(directionRoll)
        val tone = getTone(directionRoll)
        val subject = getSubject(subjectRoll)

        val oldRow = active.currentRow
        val oldCol = active.currentCol
        val oldFragment = grid[oldRow][oldCol]

        val (newRow, newCol) = if (!isDoubles) {
            move(active.currentRow, active.currentCol, direction)
        } else {
            active.currentRow to active.currentCol
        }

        val newFragment = grid[newRow][newCol]
        val isPast = newRow <= 1

        val newState = DialogGridState(
            currentRow = newRow,
            currentCol = newCol,
            conversationActive = !isDoubles,
        )

        val result = DialogResult(
            directionRoll = directionRoll,
            subjectRoll = subjectRoll,
            direction = direction,
            tone = tone,
            subject = subject,
            oldRow = oldRow,
            oldCol = oldCol,
            oldFragment = oldFragment,
            newRow = newRow,
            newCol = newCol,
            newFragment = newFragment,
            isPast = isPast,
            isDoubles = isDoubles,
            fragmentDescription = fragmentDescriptions[newFragment] ?: newFragment,
        )

        return DialogGridMoveResult(result = result, newState = newState)
    }

    /**
     * Generate a full conversation (multiple rolls) until doubles or max reached.
     * Returns all results and the final state.
     */
    fun generateConversation(
        maxExchanges: Int = 10,
    ): Pair<List<DialogResult>, DialogGridState> {
        var state = DialogGridState(currentRow = 2, currentCol = 2, conversationActive = true)
        val results = mutableListOf<DialogResult>()

        for (i in 0 until maxExchanges) {
            val moveResult = roll(state)
            results.add(moveResult.result)
            state = moveResult.newState
            if (moveResult.result.isDoubles) break
        }

        return results to state
    }

    /**
     * Reset to center position without ending conversation.
     */
    fun resetPosition(state: DialogGridState): DialogGridState {
        return state.copy(currentRow = 2, currentCol = 2, conversationActive = true)
    }

    /**
     * Set position to a specific cell, starting conversation if not active.
     */
    fun setPosition(state: DialogGridState, row: Int, col: Int): DialogGridState {
        if (row in 0..4 && col in 0..4) {
            return DialogGridState(
                currentRow = row,
                currentCol = col,
                conversationActive = true,
            )
        }
        return state
    }

    /**
     * Move on the 5x5 grid in the given direction with wrap-around.
     */
    private fun move(row: Int, col: Int, direction: String): Pair<Int, Int> {
        return when (direction) {
            "up" -> ((row - 1 + 5) % 5) to col
            "down" -> ((row + 1) % 5) to col
            "left" -> row to ((col - 1 + 5) % 5)
            "right" -> row to ((col + 1) % 5)
            else -> row to col
        }
    }

    companion object {
        /** Direction mapping based on first d10 roll. */
        fun getDirection(roll: Int): String {
            val n = if (roll == 10) 0 else roll
            return when (n) {
                in 1..2 -> "up"
                in 3..5 -> "left"
                in 6..8 -> "right"
                else -> "down"
            }
        }

        /** Tone mapping based on first d10 roll. */
        fun getTone(roll: Int): String {
            val n = if (roll == 10) 0 else roll
            return when (n) {
                in 1..2 -> "Neutral"
                in 3..5 -> "Defensive"
                in 6..8 -> "Aggressive"
                else -> "Helpful"
            }
        }

        /** Subject mapping based on second d10 roll. */
        fun getSubject(roll: Int): String {
            val n = if (roll == 10) 0 else roll
            return when (n) {
                in 1..2 -> "Them"
                in 3..5 -> "Me"
                in 6..8 -> "You"
                else -> "Us"
            }
        }

        /** The 5x5 Dialog Grid. */
        fun grid(): List<List<String>> = DialogGeneratorData.grid

        /** Fragment descriptions. */
        fun fragmentDescriptions(): Map<String, String> = DialogGeneratorData.fragmentDescriptions
    }
}
