package com.juiceroll.generator.world

import com.juiceroll.data.oracle.DetailsData
import com.juiceroll.data.oracle.DungeonData
import com.juiceroll.data.oracle.QuestData
import com.juiceroll.data.oracle.RandomEventData
import com.juiceroll.data.oracle.WildernessData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.QuestResult
import com.juiceroll.generator.challenge.DetailsGenerator

/**
 * Quest generator preset for the Juice Oracle.
 * Uses quest.md to generate full quest descriptions.
 *
 * Entries in italics reference other tables and are automatically expanded.
 * Updated to use DungeonGenerator and WildernessGenerator as proper dependencies.
 */
class QuestGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val settlementGenerator: SettlementGenerator = SettlementGenerator(rollEngine),
    private val dungeonGenerator: DungeonGenerator = DungeonGenerator(rollEngine),
    private val wildernessGenerator: WildernessGenerator = WildernessGenerator(rollEngine),
) {
    /**
     * Generate a complete quest with expanded sub-table references.
     */
    fun generate(): QuestResult {
        val objRoll = rollEngine.rollDie(10)
        val descRoll = rollEngine.rollDie(10)
        val focusRoll = rollEngine.rollDie(10)
        val prepRoll = rollEngine.rollDie(10)
        val locRoll = rollEngine.rollDie(10)

        val objective = QuestData.objectives[if (objRoll == 10) 9 else objRoll - 1]
        val description = QuestData.descriptions[if (descRoll == 10) 9 else descRoll - 1]
        val focus = QuestData.focuses[if (focusRoll == 10) 9 else focusRoll - 1]
        val preposition = QuestData.prepositions[if (prepRoll == 10) 9 else prepRoll - 1]
        val location = QuestData.locations[if (locRoll == 10) 9 else locRoll - 1]

        val descriptionExpansion = expandDescription(description)
        val focusExpansion = expandFocus(focus)
        val locationExpansion = expandLocation(location)

        return QuestResult(
            objectiveRoll = objRoll,
            objective = objective,
            description = description,
            descriptionRoll = descRoll,
            descriptionSubRoll = descriptionExpansion?.first,
            descriptionExpanded = descriptionExpansion?.second,
            focusRoll = focusRoll,
            focus = focus,
            focusSubRoll = focusExpansion?.first,
            focusExpanded = focusExpansion?.second,
            prepositionRoll = prepRoll,
            preposition = preposition,
            locationRoll = locRoll,
            location = location,
            locationSubRoll = locationExpansion?.first,
            locationExpanded = locationExpansion?.second,
        )
    }

    /**
     * Expand a description entry by rolling on the appropriate sub-table.
     */
    private fun expandDescription(description: String): Pair<Int, String>? {
        if (!QuestData.italicDescriptions.contains(description)) return null

        val subRoll = rollEngine.rollDie(10)
        val subIndex = if (subRoll == 10) 9 else subRoll - 1

        return when (description) {
            "Colorful" -> subRoll to DetailsData.colors[subIndex]
            else -> null
        }
    }

    /**
     * Expand a focus entry by rolling on the appropriate sub-table.
     * Uses DungeonGenerator and WildernessGenerator instead of direct data access.
     */
    private fun expandFocus(focus: String): Pair<Int, String>? {
        if (!QuestData.italicFocuses.contains(focus)) return null

        val subRoll = rollEngine.rollDie(10)
        val subIndex = if (subRoll == 10) 9 else subRoll - 1

        return when (focus) {
            "Monster" -> subRoll to DungeonData.dungeonMonsterDescriptors[subIndex]
            "Event" -> subRoll to RandomEventData.eventWords[subIndex]
            "Environment" -> subRoll to WildernessData.wildernessEnvironments[subIndex]
            "Person" -> subRoll to RandomEventData.personWords[subIndex]
            "Location" -> {
                val name = settlementGenerator.generateName()
                subRoll to name.name
            }
            "Object" -> subRoll to RandomEventData.objectWords[subIndex]
            else -> null
        }
    }

    /**
     * Expand a location entry by rolling on the appropriate sub-table.
     * Uses DungeonGenerator and WildernessGenerator instead of direct data access.
     */
    private fun expandLocation(location: String): Pair<Int, String>? {
        if (!QuestData.italicLocations.contains(location)) return null

        val subRoll = rollEngine.rollDie(10)
        val subIndex = if (subRoll == 10) 9 else subRoll - 1

        return when (location) {
            "Dungeon Feature" -> subRoll to DungeonData.dungeonFeatureTypes[subIndex]
            "Dungeon" -> {
                val dungeonName = dungeonGenerator.generateName()
                subRoll to dungeonName.name
            }
            "Environment" -> subRoll to WildernessData.wildernessEnvironments[subIndex]
            "Event" -> subRoll to RandomEventData.eventWords[subIndex]
            "Natural Hazard" -> subRoll to WildernessData.wildernessNaturalHazards[subIndex]
            "Settlement" -> {
                val name = settlementGenerator.generateName()
                subRoll to name.name
            }
            "Wilderness Feature" -> subRoll to WildernessData.wildernessFeatures[subIndex]
            else -> null
        }
    }
}
