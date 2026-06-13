package com.juiceroll.ui.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.*

// ═══════════════════════════════════════════════════════════════
// Ironsworn Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun IronswornActionDisplay(
    result: IronswornActionResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Row {
                Text(
                    text = "d6: ${result.actionDie}  |  ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "d10: ${result.challengeDice.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            DetailRow(label = "Score", value = "${result.actionScore}")
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = outcomeText(result.outcome),
                style = MaterialTheme.typography.bodyMedium,
                color = outcomeColor(result.outcome),
            )
            if (result.isMatch) {
                Text(
                    text = "\u2694 Match!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun IronswornProgressDisplay(
    result: IronswornProgressResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Progress", value = "${result.progressScore}")
            DetailRow(label = "Challenge", value = result.challengeDice.joinToString(", "))
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = outcomeText(result.outcome),
                style = MaterialTheme.typography.bodyMedium,
                color = outcomeColor(result.outcome),
            )
            if (result.isMatch) {
                Text(
                    text = "\u2694 Match!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun IronswornOracleDisplay(
    result: IronswornOracleResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            result.oracleTable?.let { DetailRow(label = "Table", value = it) }
            DetailRow(label = "Roll", value = "${result.oracleRoll}")
            if (result.isMatch) {
                Text(
                    text = "\u2694 Match on oracle!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
fun IronswornYesNoDisplay(
    result: IronswornYesNoResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = if (result.isYes) "YES" else "NO",
                style = MaterialTheme.typography.bodyLarge,
                color = if (result.isYes) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
            )
            DetailRow(label = "Odds", value = result.odds.name)
            DetailRow(label = "Roll", value = "${result.roll}")
            if (result.isMatch) {
                Text(
                    text = "\u2694 Match!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
fun IronswornCursedOracleDisplay(
    result: IronswornCursedOracleResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            result.oracleTable?.let { DetailRow(label = "Table", value = it) }
            DetailRow(label = "Oracle", value = "${result.oracleRoll}")
            DetailRow(label = "Cursed Die", value = "${result.cursedDie}")
            if (result.isCursed) {
                Text(
                    text = "\u2620 CURSED!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            if (result.isMatch) {
                Text(
                    text = "\u2694 Match!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Composable
fun IronswornMomentumBurnDisplay(
    result: IronswornMomentumBurnResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Original", value = "${result.originalActionScore} (${outcomeText(result.originalOutcome)})")
            DetailRow(label = "Burned", value = "${result.momentumValue} -> ${outcomeText(result.burnedOutcome)}")
            if (result.wasUpgraded) {
                Text(
                    text = "\u2191 Upgraded!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

// ── Shared helpers ──

private fun outcomeText(outcome: IronswornOutcome): String = when (outcome) {
    IronswornOutcome.STRONG_HIT -> "Strong Hit"
    IronswornOutcome.WEAK_HIT -> "Weak Hit"
    IronswornOutcome.MISS -> "Miss"
}

@Composable
private fun outcomeColor(outcome: IronswornOutcome) = when (outcome) {
    IronswornOutcome.STRONG_HIT -> MaterialTheme.colorScheme.tertiary
    IronswornOutcome.WEAK_HIT -> MaterialTheme.colorScheme.secondary
    IronswornOutcome.MISS -> MaterialTheme.colorScheme.error
}
