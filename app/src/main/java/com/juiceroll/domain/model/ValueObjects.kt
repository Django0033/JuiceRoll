package com.juiceroll.domain.model

import kotlinx.serialization.Serializable

/** Data for a random event (focus + modifier + idea). Used as embedded data in FateCheckResult. */
@Serializable
data class RandomEventData(
    val focusRoll: Int,
    val focus: String,
    val modifierRoll: Int,
    val modifier: String,
    val ideaRoll: Int,
    val idea: String,
    val ideaCategory: String = "Idea",
)

/** Data for a modifier + idea pair. Used in altered scenes, simple events, etc. */
@Serializable
data class IdeaData(
    val modifierRoll: Int,
    val modifier: String,
    val ideaRoll: Int,
    val idea: String,
    val ideaCategory: String = "Idea",
) {
    val phrase: String get() = "$modifier $idea"
}

/** Data for a property with intensity value. Used for NPC traits, location qualities, item properties. */
@Serializable
data class PropertyData(
    val propertyRoll: Int,
    val property: String,
    val intensityRoll: Int,
) {
    val intensityText: String
        get() = when (intensityRoll) {
            1 -> "barely"
            2 -> "slightly"
            3 -> "somewhat"
            4 -> "notably"
            5 -> "very"
            6 -> "extremely"
            else -> "moderately"
        }
    val fullDescription: String get() = "$intensityText $property"
}

/** Data for a simple table lookup result. Used for colors, sizes, shapes, conditions, etc. */
@Serializable
data class DetailData(
    val roll: Int,
    val value: String,
    val emoji: String? = null,
) {
    val displayValue: String get() = if (emoji != null) "$emoji $value" else value
}

/** Data for NPC personality traits. */
@Serializable
data class PersonalityData(
    val traitRoll: Int,
    val trait: String,
    val intensityRoll: Int,
    val isPrimary: Boolean = true,
) {
    val intensityText: String
        get() = when (intensityRoll) {
            1 -> "barely"
            2 -> "slightly"
            3 -> "somewhat"
            4 -> "notably"
            5 -> "very"
            6 -> "extremely"
            else -> "moderately"
        }
    val fullDescription: String get() = "$intensityText $trait"
}

/** Data for an NPC's motive (need + motivation). */
@Serializable
data class MotiveData(
    val needRoll: Int,
    val need: String,
    val motiveRoll: Int,
    val motivation: String,
) {
    val fullDescription: String get() = "$need $motivation"
}

/** Data for a dungeon area (type + shape + size). */
@Serializable
data class AreaData(
    val typeRoll: Int,
    val areaType: String,
    val shapeRoll: Int,
    val shape: String,
    val sizeRoll: Int,
    val size: String,
    val hasExit: Boolean = true,
    val isDoubles: Boolean = false,
) {
    val fullDescription: String get() = "$size $shape $areaType"
}

/** Data for an encounter (type + optional details). */
@Serializable
data class EncounterData(
    val roll: Int,
    val encounterType: String,
    val detail: String? = null,
    val detailRoll: Int? = null,
) {
    val fullDescription: String
        get() = if (detail != null) "$encounterType: $detail" else encounterType
}

/** Data for a passage/exit in a dungeon. */
@Serializable
data class PassageData(
    val roll: Int,
    val direction: String,
    val description: String? = null,
) {
    val fullDescription: String
        get() = if (description != null) "$direction: $description" else direction
}

/** Data for room condition. */
@Serializable
data class ConditionData(
    val roll: Int,
    val condition: String,
    val effect: String? = null,
) {
    val fullDescription: String
        get() = if (effect != null) "$condition: $effect" else condition
}

/** Data for a color lookup. */
@Serializable
data class ColorData(
    val roll: Int,
    val color: String,
    val emoji: String,
) {
    val displayValue: String get() = "$emoji $color"
}

/** Data for a name (first + last or establishment name). */
@Serializable
data class NameData(
    val roll1: Int,
    val part1: String,
    val roll2: Int? = null,
    val part2: String? = null,
) {
    val fullName: String get() = if (part2 != null) "$part1 $part2" else part1
}

/** Data for a challenge (physical/mental + DC). */
@Serializable
data class ChallengeData(
    val roll: Int,
    val challenge: String,
    val dcRoll: Int,
    val dc: Int,
) {
    val fullDescription: String get() = "$challenge (DC $dc)"
}

/** Data for wilderness exploration state. */
@Serializable
data class WildernessStateData(
    val environmentRow: Int,
    val typeRow: Int,
    val isLost: Boolean = false,
)

/** Data for fate dice results with symbol conversion. */
@Serializable
data class FateDiceData(
    val dice: List<Int>,
) {
    val sum: Int get() = dice.sum()
    val isDoubles: Boolean get() = dice.size == 2 && dice[0] == dice[1]
    val isDoubleBlanks: Boolean get() = dice.size == 2 && dice[0] == 0 && dice[1] == 0
    val isDoublePlus: Boolean get() = dice.size == 2 && dice[0] == 1 && dice[1] == 1
    val isDoubleMinus: Boolean get() = dice.size == 2 && dice[0] == -1 && dice[1] == -1
}

/** Data for sensory detail (sense + detail + location). */
@Serializable
data class SensoryData(
    val senseRoll: Int,
    val sense: String,
    val detailRoll: Int,
    val detail: String,
    val whereRoll: Int,
    val where: String,
) {
    val fullDescription: String get() = "You $sense something $detail $where"
}

/** Data for emotional atmosphere (emotion pair + cause). */
@Serializable
data class EmotionData(
    val emotionRoll: Int,
    val negativeEmotion: String,
    val positiveEmotion: String,
    val selectedEmotion: String,
    val isPositive: Boolean,
    val causeRoll: Int,
    val cause: String,
) {
    val fullDescription: String get() = "It causes $selectedEmotion because $cause"
}

/** Data for a focus table roll. */
@Serializable
data class FocusData(
    val roll: Int,
    val focus: String,
    val requiresExpansion: Boolean = false,
    val expansionRoll: Int? = null,
    val expandedValue: String? = null,
) {
    val fullDescription: String
        get() = if (expandedValue != null) "$focus → $expandedValue" else focus
}

/** Data for object/treasure generation (4-column). */
@Serializable
data class ObjectTreasureData(
    val categoryRoll: Int,
    val category: String,
    val qualityRoll: Int,
    val quality: String,
    val materialRoll: Int,
    val material: String,
    val typeRoll: Int,
    val itemType: String,
    val columnLabels: List<String> = listOf("Quality", "Material", "Type"),
)

/** Data for a trap (trigger + effect + DC). */
@Serializable
data class TrapData(
    val triggerRoll: Int,
    val trigger: String,
    val effectRoll: Int,
    val effect: String,
    val dcRoll: Int? = null,
    val dc: Int? = null,
) {
    val fullDescription: String
        get() = if (dc != null) "$trigger → $effect (DC $dc)" else "$trigger → $effect"
}

/** Data for a monster encounter. */
@Serializable
data class MonsterData(
    val levelRoll: Int,
    val level: String,
    val typeRoll: Int,
    val monsterType: String,
    val behaviorRoll: Int? = null,
    val behavior: String? = null,
) {
    val fullDescription: String
        get() = if (behavior != null) "$level $monsterType ($behavior)" else "$level $monsterType"
}

/** Data for an interrupt plot point. */
@Serializable
data class PlotPointData(
    val categoryRoll: Int,
    val category: String,
    val eventRoll: Int,
    val event: String,
) {
    val fullDescription: String get() = "$category: $event"
}

/** Data for discover meaning (adjective + noun pair). */
@Serializable
data class MeaningData(
    val adjectiveRoll: Int,
    val adjective: String,
    val nounRoll: Int,
    val noun: String,
) {
    val phrase: String get() = "$adjective $noun"
}

/** Data for a weather result. */
@Serializable
data class WeatherData(
    val roll: Int,
    val weather: String,
    val environmentModifier: Int,
    val severity: String = "normal",
) {
    val fullDescription: String
        get() = if (severity != "normal") "$severity $weather" else weather
}

/** Data for a dialog/conversation response. */
@Serializable
data class DialogData(
    val responseRoll: Int,
    val response: String,
    val topicRoll: Int? = null,
    val topic: String? = null,
    val mood: String? = null,
) {
    val fullDescription: String
        get() {
            val parts = mutableListOf(response)
            if (topic != null) parts.add("about $topic")
            if (mood != null) parts.add("($mood)")
            return parts.joinToString(" ")
        }
}

/** Data for a quest (focus + location + objective pattern). */
@Serializable
data class QuestData(
    val focusRoll: Int,
    val focus: String,
    val locationRoll: Int,
    val location: String,
    val objectiveRoll: Int? = null,
    val objective: String? = null,
    val focusExpanded: String? = null,
    val locationExpanded: String? = null,
) {
    val fullDescription: String
        get() {
            val df = focusExpanded ?: focus
            val dl = locationExpanded ?: location
            return if (objective != null) "$df at $dl: $objective" else "$df at $dl"
        }
}

/** Data for dungeon phase tracking. */
@Serializable
data class DungeonPhaseData(
    val phase: String,
    val roll1: Int,
    val roll2: Int,
    val chosenRoll: Int,
    val isDoubles: Boolean = false,
    val phaseChange: Boolean = false,
)
