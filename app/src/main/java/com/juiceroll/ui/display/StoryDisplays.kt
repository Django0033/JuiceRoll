package com.juiceroll.ui.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.*

// ═══════════════════════════════════════════════════════════════
// Story Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun PayThePriceDisplay(
    result: PayThePriceResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            if (result.isMajorTwist) {
                Text(
                    text = "\u26A1 MAJOR PLOT TWIST!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            DetailRow(label = "Roll", value = "${result.roll}")
        }
    }
}

@Composable
fun InterruptPlotPointDisplay(
    result: InterruptPlotPointResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Category", value = "${result.category} (${result.categoryRoll})")
            DetailRow(label = "Event", value = "${result.event} (${result.eventRoll})")
        }
    }
}

@Composable
fun QuestDisplay(
    result: QuestResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.questSentence,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            DetailRow(label = "Objective", value = result.objective)
            DetailRow(label = "Focus", value = result.focus)
            DetailRow(label = "Location", value = result.location)
        }
    }
}

@Composable
fun DetailDisplay(
    result: DetailResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            val displayText = if (result.emoji != null) "${result.emoji} ${result.result}" else result.result
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = result.detailType, value = "${result.roll}")
        }
    }
}

@Composable
fun PropertyDisplay(
    result: PropertyResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.property} (intensity ${result.intensityRoll})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun DualPropertyDisplay(
    result: DualPropertyResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.property1Name} (${result.intensity1}) + ${result.property2Name} (${result.intensity2})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun DetailWithFollowUpDisplay(
    result: DetailWithFollowUpResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.result,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            result.followUpResult?.let { followUp ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "\u2192 $followUp",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}
