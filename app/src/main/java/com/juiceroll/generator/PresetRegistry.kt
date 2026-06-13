package com.juiceroll.generator

import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.generator.challenge.ChallengeGenerator
import com.juiceroll.generator.challenge.DetailsGenerator
import com.juiceroll.generator.challenge.PayThePriceGenerator
import com.juiceroll.generator.character.DialogGenerator
import com.juiceroll.generator.character.ExtendedNpcConversationGenerator
import com.juiceroll.generator.character.NameGenerator
import com.juiceroll.generator.character.NpcActionGenerator
import com.juiceroll.generator.flavor.AbstractIconsGenerator
import com.juiceroll.generator.flavor.ImmersionGenerator
import com.juiceroll.generator.flavor.LocationGenerator
import com.juiceroll.generator.flavor.MonsterEncounterGenerator
import com.juiceroll.generator.oracle.DiscoverMeaningGenerator
import com.juiceroll.generator.oracle.ExpectationCheckGenerator
import com.juiceroll.generator.oracle.FateCheckGenerator
import com.juiceroll.generator.oracle.InterruptPlotPointGenerator
import com.juiceroll.generator.oracle.NextSceneGenerator
import com.juiceroll.generator.oracle.RandomEventGenerator
import com.juiceroll.generator.oracle.ScaleGenerator
import com.juiceroll.generator.world.DungeonGenerator
import com.juiceroll.generator.world.ObjectTreasureGenerator
import com.juiceroll.generator.world.QuestGenerator
import com.juiceroll.generator.world.SettlementGenerator
import com.juiceroll.generator.world.WildernessGenerator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Registry for all 23 preset generators.
 *
 * Provides lazy-init access to all generators with proper dependency wiring.
 * All generators share the singleton RollEngine via default parameter.
 * Circular dependency between NpcActionGenerator and SettlementGenerator
 * is handled via lazy initialization.
 */
@Singleton
class PresetRegistry @Inject constructor() {
    // ── Leaf generators (no generator dependencies) ──

    val discoverMeaning by lazy { DiscoverMeaningGenerator() }
    val randomEvent by lazy { RandomEventGenerator() }
    val interruptPlotPoint by lazy { InterruptPlotPointGenerator() }
    val expectationCheck by lazy { ExpectationCheckGenerator() }
    val scale by lazy { ScaleGenerator() }
    val details by lazy { DetailsGenerator() }
    val challenge by lazy { ChallengeGenerator() }
    val payThePrice by lazy { PayThePriceGenerator() }
    val nameGenerator by lazy { NameGenerator() }
    val dialog by lazy { DialogGenerator() }
    val extendedNpcConversation by lazy { ExtendedNpcConversationGenerator() }
    val immersion by lazy { ImmersionGenerator() }
    val location by lazy { LocationGenerator() }
    val abstractIcons by lazy { AbstractIconsGenerator() }
    val objectTreasure by lazy { ObjectTreasureGenerator() }

    // ── Wave C: New simple generators (RollEngine only) ──

    val dungeon by lazy { DungeonGenerator() }
    val wilderness by lazy { WildernessGenerator() }
    val monsterEncounter by lazy { MonsterEncounterGenerator() }

    // ── Generators with dependencies ──

    val nextScene by lazy {
        NextSceneGenerator(interruptPlotPointGenerator = interruptPlotPoint)
    }

    val fateCheck by lazy {
        FateCheckGenerator(randomEventGenerator = randomEvent)
    }

    val settlement by lazy {
        SettlementGenerator(
            detailsGenerator = details,
            randomEventGenerator = randomEvent,
            nameGenerator = nameGenerator,
        )
    }

    // Circular dependency handled: npcAction receives settlement eagerly
    // (settlement is created before npcAction in the registry)
    val npcAction by lazy {
        NpcActionGenerator(
            detailsGenerator = details,
            nextSceneGenerator = nextScene,
            settlementGenerator = settlement,
            nameGenerator = nameGenerator,
            randomEventGenerator = randomEvent,
            dungeonGenerator = dungeon,
        )
    }

    val quest by lazy {
        QuestGenerator(
            settlementGenerator = settlement,
            dungeonGenerator = dungeon,
            wildernessGenerator = wilderness,
        )
    }

    /**
     * Return all 23 generators as a list for inspection.
     */
    fun allGenerators(): List<Any> = listOf(
        discoverMeaning, fateCheck, randomEvent,
        nextScene, expectationCheck, interruptPlotPoint, scale,
        details, challenge, payThePrice,
        nameGenerator, dialog, extendedNpcConversation,
        immersion, location, abstractIcons,
        objectTreasure, settlement, quest,
        dungeon, wilderness, monsterEncounter, npcAction,
    )
}
