package com.juiceroll.generator.world

import com.juiceroll.data.oracle.ObjectTreasureData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.ItemCreationResult
import com.juiceroll.domain.model.ObjectTreasureResult

/**
 * Object and Treasure generator preset for the Juice Oracle.
 * Uses object-treasure.md for generating treasure details.
 */
class ObjectTreasureGenerator(
    private val rollEngine: RollEngine = RollEngine,
) {
    /**
     * Roll 4d6 for a complete treasure.
     * First die = category, next 3 dice = properties.
     */
    fun generate(skew: String = "none"): ObjectTreasureResult {
        val die1 = rollEngine.rollDie(6)
        val die2 = rollEngine.rollDie(6)
        val die3 = rollEngine.rollDie(6)
        val die4 = rollEngine.rollDie(6)
        return generateFromRolls(die1, die2, die3, die4)
    }

    /**
     * Generate treasure from specific 4d6 rolls.
     */
    fun generateFromRolls(die1: Int, die2: Int, die3: Int, die4: Int): ObjectTreasureResult {
        return when (die1) {
            1 -> createTrinket(die2, die3, die4)
            2 -> createTreasure(die2, die3, die4)
            3 -> createDocument(die2, die3, die4)
            4 -> createAccessory(die2, die3, die4)
            5 -> createWeapon(die2, die3, die4)
            6 -> createArmor(die2, die3, die4)
            else -> createTrinket(die2, die3, die4)
        }
    }

    /**
     * Generate treasure of a specific type (1-6).
     */
    fun generateByType(type: Int, skew: String = "none"): ObjectTreasureResult {
        val r2 = rollEngine.rollDie(6)
        val r3 = rollEngine.rollDie(6)
        val r4 = rollEngine.rollDie(6)
        return when (type) {
            1 -> createTrinket(r2, r3, r4)
            2 -> createTreasure(r2, r3, r4)
            3 -> createDocument(r2, r3, r4)
            4 -> createAccessory(r2, r3, r4)
            5 -> createWeapon(r2, r3, r4)
            6 -> createArmor(r2, r3, r4)
            else -> createTrinket(r2, r3, r4)
        }
    }

    fun generateTrinket(skew: String = "none"): ObjectTreasureResult {
        val qualityRoll = rollEngine.rollDie(6)
        val materialRoll = rollEngine.rollDie(6)
        val typeRoll = rollEngine.rollDie(6)
        return createTrinket(qualityRoll, materialRoll, typeRoll)
    }

    fun generateTreasure(skew: String = "none"): ObjectTreasureResult {
        val qualityRoll = rollEngine.rollDie(6)
        val containerRoll = rollEngine.rollDie(6)
        val contentsRoll = rollEngine.rollDie(6)
        return createTreasure(qualityRoll, containerRoll, contentsRoll)
    }

    fun generateDocument(skew: String = "none"): ObjectTreasureResult {
        val typeRoll = rollEngine.rollDie(6)
        val contentRoll = rollEngine.rollDie(6)
        val subjectRoll = rollEngine.rollDie(6)
        return createDocument(typeRoll, contentRoll, subjectRoll)
    }

    fun generateAccessory(skew: String = "none"): ObjectTreasureResult {
        val qualityRoll = rollEngine.rollDie(6)
        val materialRoll = rollEngine.rollDie(6)
        val typeRoll = rollEngine.rollDie(6)
        return createAccessory(qualityRoll, materialRoll, typeRoll)
    }

    fun generateWeapon(skew: String = "none"): ObjectTreasureResult {
        val qualityRoll = rollEngine.rollDie(6)
        val materialRoll = rollEngine.rollDie(6)
        val typeRoll = rollEngine.rollDie(6)
        return createWeapon(qualityRoll, materialRoll, typeRoll)
    }

    fun generateArmor(skew: String = "none"): ObjectTreasureResult {
        val qualityRoll = rollEngine.rollDie(6)
        val materialRoll = rollEngine.rollDie(6)
        val typeRoll = rollEngine.rollDie(6)
        return createArmor(qualityRoll, materialRoll, typeRoll)
    }

    /**
     * Generate a full item with properties and optional color.
     */
    fun generateFullItem(
        skew: String = "none",
        includeColor: Boolean = false,
    ): ItemCreationResult {
        val baseItem = generate(skew = skew)
        val prop1Roll = rollEngine.rollDie(10)
        val prop1Intensity = rollEngine.rollDie(6)
        val prop2Roll = rollEngine.rollDie(10)
        val prop2Intensity = rollEngine.rollDie(6)

        val prop1Name = com.juiceroll.data.oracle.DetailsData.properties[
            if (prop1Roll == 10) 9 else prop1Roll - 1
        ]
        val prop2Name = com.juiceroll.data.oracle.DetailsData.properties[
            if (prop2Roll == 10) 9 else prop2Roll - 1
        ]

        return ItemCreationResult(
            baseItem = baseItem,
            property1Name = prop1Name,
            property1Intensity = prop1Intensity,
            property2Name = prop2Name,
            property2Intensity = prop2Intensity,
        )
    }

    // ── Private factory methods ──

    private fun createTrinket(qualityRoll: Int, materialRoll: Int, typeRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Trinket",
            quality = ObjectTreasureData.trinketQualities[qualityRoll - 1],
            material = ObjectTreasureData.trinketMaterials[materialRoll - 1],
            itemType = ObjectTreasureData.trinketTypes[typeRoll - 1],
            rolls = listOf(1, qualityRoll, materialRoll, typeRoll),
            columnLabels = listOf("Quality", "Material", "Type"),
        )
    }

    private fun createTreasure(qualityRoll: Int, containerRoll: Int, contentsRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Treasure",
            quality = ObjectTreasureData.treasureQualities[qualityRoll - 1],
            material = ObjectTreasureData.treasureContainers[containerRoll - 1],
            itemType = ObjectTreasureData.treasureContents[contentsRoll - 1],
            rolls = listOf(2, qualityRoll, containerRoll, contentsRoll),
            columnLabels = listOf("Quality", "Container", "Contents"),
        )
    }

    private fun createDocument(typeRoll: Int, contentRoll: Int, subjectRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Document",
            quality = ObjectTreasureData.documentTypes[typeRoll - 1],
            material = ObjectTreasureData.documentContents[contentRoll - 1],
            itemType = ObjectTreasureData.documentSubjects[subjectRoll - 1],
            rolls = listOf(3, typeRoll, contentRoll, subjectRoll),
            columnLabels = listOf("Type", "Content", "Subject"),
        )
    }

    private fun createAccessory(qualityRoll: Int, materialRoll: Int, typeRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Accessory",
            quality = ObjectTreasureData.accessoryQualities[qualityRoll - 1],
            material = ObjectTreasureData.accessoryMaterials[materialRoll - 1],
            itemType = ObjectTreasureData.accessoryTypes[typeRoll - 1],
            rolls = listOf(4, qualityRoll, materialRoll, typeRoll),
            columnLabels = listOf("Quality", "Material", "Type"),
        )
    }

    private fun createWeapon(qualityRoll: Int, materialRoll: Int, typeRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Weapon",
            quality = ObjectTreasureData.weaponQualities[qualityRoll - 1],
            material = ObjectTreasureData.weaponMaterials[materialRoll - 1],
            itemType = ObjectTreasureData.weaponTypes[typeRoll - 1],
            rolls = listOf(5, qualityRoll, materialRoll, typeRoll),
            columnLabels = listOf("Quality", "Material", "Type"),
        )
    }

    private fun createArmor(qualityRoll: Int, materialRoll: Int, typeRoll: Int): ObjectTreasureResult {
        return ObjectTreasureResult(
            category = "Armor",
            quality = ObjectTreasureData.armorQualities[qualityRoll - 1],
            material = ObjectTreasureData.armorMaterials[materialRoll - 1],
            itemType = ObjectTreasureData.armorTypes[typeRoll - 1],
            rolls = listOf(6, qualityRoll, materialRoll, typeRoll),
            columnLabels = listOf("Quality", "Material", "Type"),
        )
    }
}
