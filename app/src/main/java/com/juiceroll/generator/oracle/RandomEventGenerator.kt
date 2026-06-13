package com.juiceroll.generator.oracle

import com.juiceroll.data.oracle.RandomEventData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.IdeaCategory
import com.juiceroll.domain.model.IdeaResult
import com.juiceroll.domain.model.RandomEventFocusResult
import com.juiceroll.domain.model.RandomEventResult
import com.juiceroll.domain.model.SingleTableResult

/**
 * Random Event preset for the Juice Oracle.
 * Generates random events using 3d10: Event Focus + Modifier + Idea.
 */
class RandomEventGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    fun generate(): RandomEventResult {
        val focusRoll = rollEngine.rollDie(10)
        val focusIndex = if (focusRoll == 10) 9 else focusRoll - 1
        val focus = RandomEventData.eventFocusTypes[focusIndex]

        val modifierRoll = rollEngine.rollDie(10)
        val modifierIndex = if (modifierRoll == 10) 9 else modifierRoll - 1
        val modifier = RandomEventData.modifierWords[modifierIndex]

        val categoryRoll = rollEngine.rollDie(10)
        val ideaRoll = rollEngine.rollDie(10)
        val ideaIndex = if (ideaRoll == 10) 9 else ideaRoll - 1

        val (idea, ideaCategory) = when (categoryRoll) {
            in 1..3 -> RandomEventData.ideaWords[ideaIndex] to "Idea"
            in 4..6 -> RandomEventData.eventWords[ideaIndex] to "Event"
            in 7..8 -> RandomEventData.personWords[ideaIndex] to "Person"
            else -> RandomEventData.objectWords[ideaIndex] to "Object"
        }

        return RandomEventResult(
            focusRoll = focusRoll,
            focus = focus,
            modifierRoll = modifierRoll,
            modifier = modifier,
            ideaRoll = ideaRoll,
            idea = idea,
            ideaCategory = ideaCategory,
        )
    }

    fun generateIdea(category: IdeaCategory? = null): IdeaResult {
        val modifierRoll = rollEngine.rollDie(10)
        val modifierIndex = if (modifierRoll == 10) 9 else modifierRoll - 1
        val modifier = RandomEventData.modifierWords[modifierIndex]

        val ideaRoll = rollEngine.rollDie(10)
        val ideaIndex = if (ideaRoll == 10) 9 else ideaRoll - 1

        val resolvedCategory = category ?: run {
            val catRoll = rollEngine.rollDie(10)
            when (catRoll) {
                in 1..3 -> IdeaCategory.IDEA
                in 4..6 -> IdeaCategory.EVENT
                in 7..8 -> IdeaCategory.PERSON
                else -> IdeaCategory.OBJECT
            }
        }

        val (idea, ideaCategory) = when (resolvedCategory) {
            IdeaCategory.IDEA -> RandomEventData.ideaWords[ideaIndex] to "Idea"
            IdeaCategory.EVENT -> RandomEventData.eventWords[ideaIndex] to "Event"
            IdeaCategory.PERSON -> RandomEventData.personWords[ideaIndex] to "Person"
            IdeaCategory.OBJECT -> RandomEventData.objectWords[ideaIndex] to "Object"
        }

        return IdeaResult(
            modifierRoll = modifierRoll,
            modifier = modifier,
            ideaRoll = ideaRoll,
            idea = idea,
            ideaCategory = ideaCategory,
        )
    }

    fun generateFocus(): RandomEventFocusResult {
        val focusRoll = rollEngine.rollDie(10)
        val focusIndex = if (focusRoll == 10) 9 else focusRoll - 1
        val focus = RandomEventData.eventFocusTypes[focusIndex]

        return RandomEventFocusResult(
            focusRoll = focusRoll,
            focus = focus,
        )
    }

    fun rollSingleTableResult(tableName: String): SingleTableResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val result = when (tableName) {
            "Modifier" -> RandomEventData.modifierWords[index]
            "Idea" -> RandomEventData.ideaWords[index]
            "Event" -> RandomEventData.eventWords[index]
            "Person" -> RandomEventData.personWords[index]
            "Object" -> RandomEventData.objectWords[index]
            else -> RandomEventData.modifierWords[index]
        }
        return SingleTableResult(roll = roll, result = result, tableName = tableName)
    }

    fun rollModifier(): SingleTableResult = rollSingleTableResult("Modifier")

    fun rollIdea(): SingleTableResult = rollSingleTableResult("Idea")

    fun rollEvent(): SingleTableResult = rollSingleTableResult("Event")

    fun rollPerson(): SingleTableResult = rollSingleTableResult("Person")

    fun rollObject(): SingleTableResult = rollSingleTableResult("Object")
}
