package com.juiceroll.generator.world

import com.juiceroll.data.oracle.NpcActionData
import com.juiceroll.data.oracle.SettlementData
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.CompleteSettlementResult
import com.juiceroll.domain.model.EstablishmentCountResult
import com.juiceroll.domain.model.EstablishmentNameResult
import com.juiceroll.domain.model.FullSettlementResult
import com.juiceroll.domain.model.MultiEstablishmentResult
import com.juiceroll.domain.model.SettlementDetailResult
import com.juiceroll.domain.model.SettlementNameResult
import com.juiceroll.domain.model.SettlementPropertiesResult
import com.juiceroll.domain.model.SimpleNpcResult
import com.juiceroll.generator.challenge.DetailsGenerator
import com.juiceroll.generator.character.NameGenerator
import com.juiceroll.generator.oracle.RandomEventGenerator

/**
 * Settlement generator preset for the Juice Oracle.
 * Uses settlement.md for generating settlement details.
 *
 * Per Juice rules:
 * - Villages: roll d6 for establishment type, @- for count
 * - Cities: roll d10 for establishment type, @+ for count
 */
class SettlementGenerator(
    private val rollEngine: RollEngine = RollEngine,
    private val detailsGenerator: DetailsGenerator = DetailsGenerator(rollEngine),
    private val randomEventGenerator: RandomEventGenerator = RandomEventGenerator(rollEngine),
    private val nameGenerator: NameGenerator = NameGenerator(rollEngine),
) {
    /**
     * Generate an establishment name using Color + Object pattern.
     */
    fun generateEstablishmentName(): EstablishmentNameResult {
        val colorResult = detailsGenerator.rollColor()
        val objectResult = randomEventGenerator.rollObject()

        val colorParts = colorResult.result.split(" ")
        val shortColor = if (colorParts.isNotEmpty()) colorParts[0] else colorResult.result

        val name = "The $shortColor ${objectResult.result}"

        return EstablishmentNameResult(
            colorRoll = colorResult.roll,
            color = colorResult.result,
            shortColor = shortColor,
            colorEmoji = colorResult.emoji ?: "",
            objectRoll = objectResult.roll,
            `object` = objectResult.result,
            name = name,
        )
    }

    /**
     * Generate settlement properties (roll two properties with intensity).
     */
    fun generateProperties(): SettlementPropertiesResult {
        val prop1 = detailsGenerator.rollProperty()
        val prop2 = detailsGenerator.rollProperty()

        return SettlementPropertiesResult(
            property1Roll = prop1.propertyRoll,
            property1Name = prop1.property,
            intensity1 = prop1.intensityRoll,
            property2Roll = prop2.propertyRoll,
            property2Name = prop2.property,
            intensity2 = prop2.intensityRoll,
        )
    }

    /**
     * Generate a simple NPC (name + personality + need + motive).
     */
    fun generateSimpleNpc(): SimpleNpcResult {
        val nameResult = nameGenerator.generate()

        val persRoll = rollEngine.rollDie(10)
        val needRoll = rollEngine.rollDie(10)
        val motiveRoll = rollEngine.rollDie(10)

        val personality = NpcActionData.npcPersonalities[
            if (persRoll == 10) 9 else persRoll - 1
        ]
        val need = NpcActionData.npcNeeds[
            if (needRoll == 10) 9 else needRoll - 1
        ]
        val motive = NpcActionData.npcMotives[
            if (motiveRoll == 10) 9 else motiveRoll - 1
        ]

        return SimpleNpcResult(
            name = nameResult.name,
            personality = personality,
            need = need,
            motive = motive,
        )
    }

    /**
     * Generate a settlement name.
     */
    fun generateName(): SettlementNameResult {
        val prefixRoll = rollEngine.rollDie(10)
        val suffixRoll = rollEngine.rollDie(10)
        val prefix = SettlementData.settlementNamePrefixes[
            if (prefixRoll == 10) 9 else prefixRoll - 1
        ]
        val suffix = SettlementData.settlementNameSuffixes[
            if (suffixRoll == 10) 9 else suffixRoll - 1
        ]

        return SettlementNameResult(
            prefixRoll = prefixRoll,
            prefix = prefix,
            suffixRoll = suffixRoll,
            suffix = suffix,
        )
    }

    /**
     * Roll for an establishment.
     * [isVillage] determines die size: d6 for villages, d10 for cities.
     */
    fun rollEstablishment(isVillage: Boolean = false): SettlementDetailResult {
        val dieSize = if (isVillage) 6 else 10
        val roll = rollEngine.rollDie(dieSize)
        val index = if (roll == dieSize) dieSize - 1 else roll - 1
        var establishment = SettlementData.settlementEstablishments[index]
        val description = SettlementData.settlementEstablishmentDescriptions[establishment]

        var artisan: String? = null
        var artisanRoll: Int? = null
        var artisanDescription: String? = null

        if (establishment == "Artisan") {
            artisanRoll = rollEngine.rollDie(10)
            val artisanIndex = if (artisanRoll == 10) 9 else artisanRoll - 1
            artisan = SettlementData.settlementArtisans[artisanIndex]
            artisanDescription = SettlementData.settlementArtisanDescriptions[artisan]
            establishment = "$artisan (Artisan)"
        }

        return SettlementDetailResult(
            detailType = "Establishment",
            roll = roll,
            result = establishment,
            subRoll = artisanRoll,
            subResult = artisan,
            detailDescription = artisanDescription ?: description,
            dieSize = dieSize,
        )
    }

    /**
     * Roll for settlement news.
     */
    fun rollNews(): SettlementDetailResult {
        val roll = rollEngine.rollDie(10)
        val index = if (roll == 10) 9 else roll - 1
        val newsItem = SettlementData.settlementNews[index]
        val description = SettlementData.settlementNewsDescriptions[newsItem]

        return SettlementDetailResult(
            detailType = "News",
            roll = roll,
            result = newsItem,
            detailDescription = description,
        )
    }

    /**
     * Generate establishment count for a settlement.
     * Villages: disadvantage (take lower), Cities: advantage (take higher).
     */
    fun rollEstablishmentCount(settlementType: String = "village"): EstablishmentCountResult {
        val dice = listOf(rollEngine.rollDie(6), rollEngine.rollDie(6))
        val (count, skewUsed) = if (settlementType == "village") {
            (if (dice[0] < dice[1]) dice[0] else dice[1]) to "@- (disadvantage)"
        } else {
            (if (dice[0] > dice[1]) dice[0] else dice[1]) to "@+ (advantage)"
        }

        return EstablishmentCountResult(
            count = count,
            dice = dice,
            settlementType = settlementType,
            skewUsed = skewUsed,
        )
    }

    /**
     * Generate multiple establishments for a settlement.
     */
    fun generateEstablishments(settlementType: String = "village"): MultiEstablishmentResult {
        val countResult = rollEstablishmentCount(settlementType = settlementType)
        val isVillage = settlementType == "village"
        val establishments = (1..countResult.count).map {
            rollEstablishment(isVillage = isVillage)
        }

        return MultiEstablishmentResult(
            count = countResult.count,
            establishments = establishments.map { it.result },
        )
    }

    /**
     * Generate a full settlement (name + establishment + news).
     */
    fun generateFull(): FullSettlementResult {
        val name = generateName()
        val establishment = rollEstablishment()
        val newsItem = rollNews()

        return FullSettlementResult(
            name = name.name,
            establishment = establishment.result,
            news = newsItem.result,
        )
    }

    /**
     * Generate a complete village with name, multiple establishments, and news.
     */
    fun generateVillage(): CompleteSettlementResult {
        val name = generateName()
        val establishments = generateEstablishments(settlementType = "village")
        val newsItem = rollNews()

        return CompleteSettlementResult(
            settlementType = "village",
            name = name.name,
            establishmentNames = establishments.establishments,
            news = newsItem.result,
        )
    }

    /**
     * Generate a complete city with name, multiple establishments, and news.
     */
    fun generateCity(): CompleteSettlementResult {
        val name = generateName()
        val establishments = generateEstablishments(settlementType = "city")
        val newsItem = rollNews()

        return CompleteSettlementResult(
            settlementType = "city",
            name = name.name,
            establishmentNames = establishments.establishments,
            news = newsItem.result,
        )
    }
}
