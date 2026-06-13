package com.juiceroll.domain.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Shared Json instance for all RollResult serialization.
 * Uses "className" discriminator matching the Flutter serialization scheme.
 */
val rollResultJson = Json {
    classDiscriminator = "className"
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        polymorphic(RollResult::class) {
            // Basic (3)
            subclass(RollResult.StandardRollResult::class)
            subclass(RollResult.FateRollResult::class)
            subclass(RollResult.InvalidExpression::class)

            // Oracle (7)
            subclass(FateCheckResult::class)
            subclass(ExpectationCheckResult::class)
            subclass(ScaleResult::class)
            subclass(NextSceneResult::class)
            subclass(NextSceneWithFollowUpResult::class)
            subclass(FocusResult::class)
            subclass(DiscoverMeaningResult::class)

            // Random Event (4)
            subclass(RandomEventResult::class)
            subclass(RandomEventFocusResult::class)
            subclass(IdeaResult::class)
            subclass(SingleTableResult::class)

            // NPC & Dialog (9)
            subclass(NpcActionResult::class)
            subclass(MotiveWithFollowUpResult::class)
            subclass(SimpleNpcProfileResult::class)
            subclass(NpcProfileResult::class)
            subclass(DualPersonalityResult::class)
            subclass(ComplexNpcResult::class)
            subclass(InformationResult::class)
            subclass(CompanionResponseResult::class)
            subclass(DialogTopicResult::class)

            // Story (7)
            subclass(PayThePriceResult::class)
            subclass(InterruptPlotPointResult::class)
            subclass(QuestResult::class)
            subclass(DetailResult::class)
            subclass(PropertyResult::class)
            subclass(DualPropertyResult::class)
            subclass(DetailWithFollowUpResult::class)

            // World (23)
            subclass(SettlementNameResult::class)
            subclass(SettlementDetailResult::class)
            subclass(EstablishmentCountResult::class)
            subclass(EstablishmentNameResult::class)
            subclass(SettlementPropertiesResult::class)
            subclass(SimpleNpcResult::class)
            subclass(MultiEstablishmentResult::class)
            subclass(FullSettlementResult::class)
            subclass(CompleteSettlementResult::class)
            subclass(ObjectTreasureResult::class)
            subclass(ItemCreationResult::class)
            subclass(DungeonNameResult::class)
            subclass(DungeonAreaResult::class)
            subclass(DungeonDetailResult::class)
            subclass(FullDungeonAreaResult::class)
            subclass(DungeonMonsterResult::class)
            subclass(DungeonTrapResult::class)
            subclass(DungeonEncounterResult::class)
            subclass(TwoPassAreaResult::class)
            subclass(TrapProcedureResult::class)
            subclass(LocationResult::class)
            subclass(AbstractIconResult::class)
            subclass(NameResult::class)

            // Basic (16)
            subclass(FullChallengeResult::class)
            subclass(DcResult::class)
            subclass(QuickDcResult::class)
            subclass(ChallengeSkillResult::class)
            subclass(PercentageChanceResult::class)
            subclass(SensoryDetailResult::class)
            subclass(EmotionalAtmosphereResult::class)
            subclass(FullImmersionResult::class)
            subclass(WildernessAreaResult::class)
            subclass(WildernessEncounterResult::class)
            subclass(WildernessWeatherResult::class)
            subclass(WildernessDetailResult::class)
            subclass(MonsterLevelResult::class)
            subclass(MonsterEncounterResult::class)
            subclass(FullMonsterEncounterResult::class)
            subclass(MonsterTracksResult::class)

            // Ironsworn (6)
            subclass(IronswornActionResult::class)
            subclass(IronswornProgressResult::class)
            subclass(IronswornOracleResult::class)
            subclass(IronswornYesNoResult::class)
            subclass(IronswornCursedOracleResult::class)
            subclass(IronswornMomentumBurnResult::class)
        }
    }
}
