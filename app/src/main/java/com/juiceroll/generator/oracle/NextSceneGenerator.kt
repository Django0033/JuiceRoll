package com.juiceroll.generator.oracle

import com.juiceroll.data.oracle.NextSceneData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.FocusResult
import com.juiceroll.domain.model.NextSceneResult

/**
 * Next Scene preset for the Juice Oracle.
 * Uses the Next Scene column from the Fate Check table (2dF).
 * Determines if the next scene proceeds normally, is altered, or is interrupted.
 *
 * At the end of a scene, you probably have an idea of what the next scene may look like.
 * Mythic prompts you to challenge that expectation, and Juice does it in a streamlined fashion.
 */
class NextSceneGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val interruptPlotPointGenerator: InterruptPlotPointGenerator =
        InterruptPlotPointGenerator(rollEngine),
) {
    fun determineScene(): NextSceneResult {
        val fateDice = rollEngine.rollFateDice(2)
        val leftDie = fateDice[0]
        val rightDie = fateDice[1]
        val fateSum = leftDie + rightDie
        val sceneType = interpretFateDice(leftDie, rightDie)

        return NextSceneResult(
            fateDice = fateDice,
            fateSum = fateSum,
            sceneType = sceneType,
        )
    }

    fun rollFocus(): FocusResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val focus = NextSceneData.focuses[index]
        return FocusResult(roll = roll, focus = focus)
    }

    /**
     * Interpret the Fate dice for Next Scene.
     * Based on fate-check.md Next Scene column.
     */
    fun interpretFateDice(left: Int, right: Int): String {
        // + + = Alter (Add)
        if (left == 1 && right == 1) return "alterAdd"
        // + - = Alter (Remove)
        if (left == 1 && right == -1) return "alterRemove"
        // - + = Interrupt (Favorable)
        if (left == -1 && right == 1) return "interruptFavorable"
        // - - = Interrupt (Unfavorable)
        if (left == -1 && right == -1) return "interruptUnfavorable"
        // All other combinations = Normal
        return "normal"
    }
}
