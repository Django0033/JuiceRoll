package com.juiceroll.ui.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.*

/**
 * Entry point for rendering any RollResult as a composable display.
 * Uses an exhaustive [when] over the sealed [RollResult] class.
 * Adding a new sealed subtype without handling it here will produce a compiler warning.
 */
@Composable
fun ResultDisplay(
    result: RollResult,
    modifier: Modifier = Modifier,
) {
    when (result) {
        // ── Basic dice ──
        is RollResult.StandardRollResult -> StandardDiceDisplay(result, modifier)
        is RollResult.FateRollResult -> FateDiceDisplay(result, modifier)
        is RollResult.InvalidExpression -> InvalidDisplay(result, modifier)

        // ── Oracle ──
        is FateCheckResult -> FateCheckDisplay(result, modifier)
        is ExpectationCheckResult -> ExpectationCheckDisplay(result, modifier)
        is ScaleResult -> ScaleDisplay(result, modifier)
        is ScaledValueResult -> ScaledValueDisplay(result, modifier)
        is NextSceneResult -> NextSceneDisplay(result, modifier)
        is NextSceneWithFollowUpResult -> NextSceneWithFollowUpDisplay(result, modifier)
        is FocusResult -> FocusDisplay(result, modifier)
        is DiscoverMeaningResult -> DiscoverMeaningDisplay(result, modifier)

        // ── Random Event ──
        is RandomEventResult -> RandomEventDisplay(result, modifier)
        is RandomEventFocusResult -> RandomEventFocusDisplay(result, modifier)
        is IdeaResult -> IdeaDisplay(result, modifier)
        is SingleTableResult -> SingleTableDisplay(result, modifier)

        // ── NPC ──
        is NpcActionResult -> NpcActionDisplay(result, modifier)
        is MotiveWithFollowUpResult -> MotiveWithFollowUpDisplay(result, modifier)
        is SimpleNpcProfileResult -> SimpleNpcProfileDisplay(result, modifier)
        is NpcProfileResult -> NpcProfileDisplay(result, modifier)
        is DualPersonalityResult -> DualPersonalityDisplay(result, modifier)
        is ComplexNpcResult -> ComplexNpcDisplay(result, modifier)
        is InformationResult -> InformationDisplay(result, modifier)
        is CompanionResponseResult -> CompanionResponseDisplay(result, modifier)
        is DialogTopicResult -> DialogTopicDisplay(result, modifier)
        is DialogResult -> DialogDisplay(result, modifier)

        // ── Story ──
        is PayThePriceResult -> PayThePriceDisplay(result, modifier)
        is InterruptPlotPointResult -> InterruptPlotPointDisplay(result, modifier)
        is QuestResult -> QuestDisplay(result, modifier)
        is DetailResult -> DetailDisplay(result, modifier)
        is PropertyResult -> PropertyDisplay(result, modifier)
        is DualPropertyResult -> DualPropertyDisplay(result, modifier)
        is DetailWithFollowUpResult -> DetailWithFollowUpDisplay(result, modifier)

        // ── World ──
        is SettlementNameResult -> SettlementNameDisplay(result, modifier)
        is SettlementDetailResult -> SettlementDetailDisplay(result, modifier)
        is EstablishmentCountResult -> EstablishmentCountDisplay(result, modifier)
        is EstablishmentNameResult -> EstablishmentNameDisplay(result, modifier)
        is SettlementPropertiesResult -> SettlementPropertiesDisplay(result, modifier)
        is SimpleNpcResult -> SimpleNpcDisplay(result, modifier)
        is MultiEstablishmentResult -> MultiEstablishmentDisplay(result, modifier)
        is FullSettlementResult -> FullSettlementDisplay(result, modifier)
        is CompleteSettlementResult -> CompleteSettlementDisplay(result, modifier)
        is ObjectTreasureResult -> ObjectTreasureDisplay(result, modifier)
        is ItemCreationResult -> ItemCreationDisplay(result, modifier)
        is DungeonNameResult -> DungeonNameDisplay(result, modifier)
        is DungeonAreaResult -> DungeonAreaDisplay(result, modifier)
        is DungeonDetailResult -> DungeonDetailDisplay(result, modifier)
        is FullDungeonAreaResult -> FullDungeonAreaDisplay(result, modifier)
        is DungeonMonsterResult -> DungeonMonsterDisplay(result, modifier)
        is DungeonTrapResult -> DungeonTrapDisplay(result, modifier)
        is DungeonEncounterResult -> DungeonEncounterDisplay(result, modifier)
        is TwoPassAreaResult -> TwoPassAreaDisplay(result, modifier)
        is TrapProcedureResult -> TrapProcedureDisplay(result, modifier)
        is LocationResult -> LocationDisplay(result, modifier)
        is AbstractIconResult -> AbstractIconDisplay(result, modifier)
        is NameResult -> NameDisplay(result, modifier)

        // ── Basic ──
        is FullChallengeResult -> FullChallengeDisplay(result, modifier)
        is DcResult -> DcDisplay(result, modifier)
        is QuickDcResult -> QuickDcDisplay(result, modifier)
        is ChallengeSkillResult -> ChallengeSkillDisplay(result, modifier)
        is PercentageChanceResult -> PercentageChanceDisplay(result, modifier)
        is SensoryDetailResult -> SensoryDetailDisplay(result, modifier)
        is EmotionalAtmosphereResult -> EmotionalAtmosphereDisplay(result, modifier)
        is FullImmersionResult -> FullImmersionDisplay(result, modifier)
        is WildernessAreaResult -> WildernessAreaDisplay(result, modifier)
        is WildernessEncounterResult -> WildernessEncounterDisplay(result, modifier)
        is WildernessWeatherResult -> WildernessWeatherDisplay(result, modifier)
        is WildernessDetailResult -> WildernessDetailDisplay(result, modifier)
        is MonsterLevelResult -> MonsterLevelDisplay(result, modifier)
        is MonsterEncounterResult -> MonsterEncounterDisplay(result, modifier)
        is FullMonsterEncounterResult -> FullMonsterEncounterDisplay(result, modifier)
        is MonsterTracksResult -> MonsterTracksDisplay(result, modifier)

        // ── Ironsworn ──
        is IronswornActionResult -> IronswornActionDisplay(result, modifier)
        is IronswornProgressResult -> IronswornProgressDisplay(result, modifier)
        is IronswornOracleResult -> IronswornOracleDisplay(result, modifier)
        is IronswornYesNoResult -> IronswornYesNoDisplay(result, modifier)
        is IronswornCursedOracleResult -> IronswornCursedOracleDisplay(result, modifier)
        is IronswornMomentumBurnResult -> IronswornMomentumBurnDisplay(result, modifier)
    }
}

// ── Shared helper composables ──

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun RollResultCard(
    result: RollResult,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
    ) {
        Row {
            Text(
                text = result.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        content()
        if (result.interpretation != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = result.interpretation!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun DiceRow(
    values: List<Int>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        values.forEachIndexed { index, value ->
            if (index > 0) {
                Text(
                    text = " + ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = "$value",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

// ── Basic dice display composables ──

@Composable
fun StandardDiceDisplay(
    result: RollResult.StandardRollResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Row {
                DiceRow(result.values)
            }
            if (result.dropped.isNotEmpty()) {
                Text(
                    text = "Dropped: ${result.dropped.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            Text(
                text = "Total: ${result.total}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun FateDiceDisplay(
    result: RollResult.FateRollResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            FateDiceRow(result.values)
            Text(
                text = "Total: ${result.total}",
                style = MaterialTheme.typography.bodyLarge,
                color = when {
                    result.total > 0 -> MaterialTheme.colorScheme.tertiary
                    result.total < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.secondary
                },
            )
        }
    }
}

@Composable
fun InvalidDisplay(
    result: RollResult.InvalidExpression,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Text(
            text = "\u26A0 Invalid: ${result.expression}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

/**
 * Render fate dice values as symbols (+, ○, −).
 */
@Composable
fun FateDiceRow(
    dice: List<Int>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        dice.forEachIndexed { index, value ->
            if (index > 0) Text(" ", style = MaterialTheme.typography.bodyMedium)
            val symbol = when (value) {
                -1 -> "\u2212"  // minus sign
                0 -> "\u25CB"   // empty circle
                1 -> "+"
                else -> "?"
            }
            Text(
                text = symbol,
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    value > 0 -> MaterialTheme.colorScheme.tertiary
                    value < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}
