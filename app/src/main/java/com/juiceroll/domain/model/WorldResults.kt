package com.juiceroll.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ═══════════════════════════════════════════
// Settlement Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("SettlementNameResult")
data class SettlementNameResult(
    val prefixRoll: Int,
    val prefix: String,
    val suffixRoll: Int,
    val suffix: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Settlement Name"
    override val diceResults: List<Int> get() = listOf(prefixRoll, suffixRoll)
    override val total: Int get() = prefixRoll + suffixRoll
    override val interpretation: String? get() = "$prefix$suffix"
    val name: String get() = "$prefix$suffix"
}

@Serializable
@SerialName("SettlementDetailResult")
data class SettlementDetailResult(
    val detailType: String,
    val roll: Int,
    val result: String,
    val subRoll: Int? = null,
    val subResult: String? = null,
    val detailDescription: String? = null,
    val dieSize: Int? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Settlement $detailType"
    override val diceResults: List<Int>
        get() = if (subRoll != null) listOf(roll, subRoll) else listOf(roll)
    override val total: Int get() = roll + (subRoll ?: 0)
    override val interpretation: String? get() = result
}

@Serializable
@SerialName("EstablishmentCountResult")
data class EstablishmentCountResult(
    val count: Int,
    val dice: List<Int>,
    val settlementType: String = "village",
    val skewUsed: String = "straight",
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Establishment Count"
    override val diceResults: List<Int> get() = dice
    override val total: Int get() = count
    override val interpretation: String? get() = "$count establishments"
}

@Serializable
@SerialName("EstablishmentNameResult")
data class EstablishmentNameResult(
    val colorRoll: Int,
    val color: String,
    val shortColor: String,
    val colorEmoji: String,
    val objectRoll: Int,
    val `object`: String,
    val name: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Establishment Name"
    override val diceResults: List<Int> get() = listOf(colorRoll, objectRoll)
    override val total: Int get() = colorRoll + objectRoll
    override val interpretation: String? get() = "$colorEmoji $name"
}

@Serializable
@SerialName("SettlementPropertiesResult")
data class SettlementPropertiesResult(
    val property1Roll: Int,
    val property1Name: String,
    val intensity1: Int,
    val property2Roll: Int,
    val property2Name: String,
    val intensity2: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Settlement Properties"
    override val diceResults: List<Int>
        get() = listOf(property1Roll, intensity1, property2Roll, intensity2)
    override val total: Int get() = property1Roll + property2Roll
    override val interpretation: String? get() = "$property1Name + $property2Name"
}

@Serializable
@SerialName("SimpleNpcResult")
data class SimpleNpcResult(
    val name: String,
    val personality: String,
    val need: String,
    val motive: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Simple NPC"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = "$name: $personality, $need, $motive"
}

@Serializable
@SerialName("MultiEstablishmentResult")
data class MultiEstablishmentResult(
    val count: Int,
    val establishments: List<String>,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Settlement Establishments"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = establishments.size
    override val interpretation: String? get() = establishments.joinToString(", ")
}

@Serializable
@SerialName("FullSettlementResult")
data class FullSettlementResult(
    val name: String,
    val establishment: String,
    val news: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String get() = "Settlement"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = "$name - $establishment - $news"
}

@Serializable
@SerialName("CompleteSettlementResult")
data class CompleteSettlementResult(
    val settlementType: String = "village",
    val name: String,
    val establishmentNames: List<String>,
    val news: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.SETTLEMENT
    override val description: String
        get() = if (settlementType == "village") "Village" else "City"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = name
}

// ═══════════════════════════════════════════
// Object Treasure & Item Creation
// ═══════════════════════════════════════════

@Serializable
@SerialName("ObjectTreasureResult")
data class ObjectTreasureResult(
    val category: String,
    val quality: String,
    val material: String,
    val itemType: String,
    val rolls: List<Int>,
    val columnLabels: List<String> = listOf("Quality", "Material", "Type"),
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.OBJECT_TREASURE
    override val description: String get() = category
    override val diceResults: List<Int> get() = rolls
    override val total: Int get() = if (rolls.isNotEmpty()) rolls.reduce { a, b -> a + b } else 0
    override val interpretation: String? get() = "$quality $material $itemType"
}

@Serializable
@SerialName("ItemCreationResult")
data class ItemCreationResult(
    val baseItem: ObjectTreasureResult? = null,
    val property1Name: String,
    val property1Intensity: Int,
    val property2Name: String,
    val property2Intensity: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.OBJECT_TREASURE
    override val description: String get() = "Item Creation"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = "$property1Name + $property2Name"
}

// ═══════════════════════════════════════════
// Dungeon Results
// ═══════════════════════════════════════════

@Serializable
@SerialName("DungeonNameResult")
data class DungeonNameResult(
    val typeRoll: Int,
    val dungeonType: String,
    val descriptionRoll: Int,
    val descriptionWord: String,
    val subjectRoll: Int,
    val subject: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Name"
    override val diceResults: List<Int> get() = listOf(typeRoll, descriptionRoll, subjectRoll)
    override val total: Int get() = typeRoll + descriptionRoll + subjectRoll
    override val interpretation: String? get() = "$dungeonType of the $descriptionWord $subject"
    val name: String get() = "$dungeonType of the $descriptionWord $subject"
}

@Serializable
@SerialName("DungeonAreaResult")
data class DungeonAreaResult(
    val phase: String,
    val roll1: Int,
    val roll2: Int,
    val chosenRoll: Int,
    val areaType: String,
    val isDoubles: Boolean = false,
    val phaseChange: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Area"
    override val diceResults: List<Int> get() = listOf(roll1, roll2)
    override val total: Int get() = chosenRoll
    override val interpretation: String? get() = areaType
}

@Serializable
@SerialName("DungeonDetailResult")
data class DungeonDetailResult(
    val detailType: String,
    val roll: Int,
    val result: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon $detailType"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = result
}

@Serializable
@SerialName("FullDungeonAreaResult")
data class FullDungeonAreaResult(
    val areaType: String,
    val conditionResult: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Area"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = "$areaType ($conditionResult)"
}

@Serializable
@SerialName("DungeonMonsterResult")
data class DungeonMonsterResult(
    val descriptorRoll: Int,
    val descriptor: String,
    val abilityRoll: Int,
    val ability: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Monster"
    override val diceResults: List<Int> get() = listOf(descriptorRoll, abilityRoll)
    override val total: Int get() = descriptorRoll + abilityRoll
    override val interpretation: String? get() = "$descriptor creature with $ability"
}

@Serializable
@SerialName("DungeonTrapResult")
data class DungeonTrapResult(
    val actionRoll: Int,
    val action: String,
    val subjectRoll: Int,
    val subject: String,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Trap"
    override val diceResults: List<Int> get() = listOf(actionRoll, subjectRoll)
    override val total: Int get() = actionRoll + subjectRoll
    override val interpretation: String? get() = "$action trap with $subject"
}

@Serializable
@SerialName("DungeonEncounterResult")
data class DungeonEncounterResult(
    val encounterType: String,
    val monsterDescriptor: String? = null,
    val monsterAbility: String? = null,
    val trapAction: String? = null,
    val trapSubject: String? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Dungeon Encounter"
    override val diceResults: List<Int> get() = emptyList()
    override val total: Int get() = 0
    override val interpretation: String? get() = encounterType
}

@Serializable
@SerialName("TwoPassAreaResult")
data class TwoPassAreaResult(
    val roll1: Int,
    val roll2: Int,
    val chosenRoll: Int,
    val areaType: String,
    val isDoubles: Boolean = false,
    val hadFirstDoubles: Boolean = false,
    val isSecondDoubles: Boolean = false,
    val stopMapGeneration: Boolean = false,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Two-Pass Map"
    override val diceResults: List<Int> get() = listOf(roll1, roll2)
    override val total: Int get() = chosenRoll
    override val interpretation: String? get() = areaType
}

@Serializable
@SerialName("TrapProcedureResult")
data class TrapProcedureResult(
    val trapAction: String,
    val trapSubject: String,
    val isSearching: Boolean = false,
    val dcRoll: Int,
    val dcRolls: List<Int>,
    val dc: Int,
    val dcSkew: String = "none",
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.DUNGEON
    override val description: String get() = "Trap Procedure"
    override val diceResults: List<Int> get() = dcRolls
    override val total: Int get() = dc
    override val interpretation: String? get() = "$trapAction trap with $trapSubject"
}

// ═══════════════════════════════════════════
// Location Result
// ═══════════════════════════════════════════

@Serializable
@SerialName("LocationResult")
data class LocationResult(
    val roll: Int,
    val row: Int,
    val column: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.LOCATION
    override val description: String get() = "Location Grid"
    override val diceResults: List<Int> get() = listOf(roll)
    override val total: Int get() = roll
    override val interpretation: String? get() = "[$row,$column]"
}

// ═══════════════════════════════════════════
// Abstract Icons Result
// ═══════════════════════════════════════════

@Serializable
@SerialName("AbstractIconResult")
data class AbstractIconResult(
    val d10Roll: Int,
    val d6Roll: Int,
    val rowLabel: Int,
    val colLabel: Int,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.ABSTRACT_ICONS
    override val description: String get() = "Abstract Icons"
    override val diceResults: List<Int> get() = listOf(d10Roll, d6Roll)
    override val total: Int get() = d10Roll + d6Roll
    override val interpretation: String? get() = "($rowLabel, $colLabel)"
}

// ═══════════════════════════════════════════
// Name Result
// ═══════════════════════════════════════════

@Serializable
@SerialName("NameResult")
data class NameResult(
    val rolls: List<Int>,
    val syllables: List<String>,
    val name: String,
    val style: String = "neutral",
    val method: String = "simple",
    val pattern: String? = null,
    override val timestamp: Long = System.currentTimeMillis(),
) : RollResult() {
    override val type: RollType get() = RollType.NAME_GENERATOR
    override val description: String get() = "Name Generator"
    override val diceResults: List<Int> get() = rolls
    override val total: Int get() = if (rolls.isNotEmpty()) rolls.reduce { a, b -> a + b } else 0
    override val interpretation: String? get() = name
}
