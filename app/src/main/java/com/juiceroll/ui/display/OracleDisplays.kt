package com.juiceroll.ui.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.*

// ═══════════════════════════════════════════════════════════════
// Oracle Displays — FateCheck, ExpectationCheck, Scale,
//                    NextScene, Focus, DiscoverMeaning
// ═══════════════════════════════════════════════════════════════

@Composable
fun FateCheckDisplay(
    result: FateCheckResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            // Likelihood
            DetailRow(label = "Likelihood", value = result.likelihood)
            Spacer(modifier = Modifier.height(2.dp))
            // Fate dice
            Row {
                Text(
                    text = "Dice: ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FateDiceRow(result.fateDice)
            }
            Spacer(modifier = Modifier.height(2.dp))
            // Sum + Intensity
            DetailRow(label = "Sum", value = "${result.fateSum}")
            DetailRow(label = "Intensity", value = "${result.intensity}")
            // Outcome
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = result.outcome.name,
                style = MaterialTheme.typography.bodyMedium,
                color = when (result.outcome) {
                    FateCheckOutcome.YES_AND, FateCheckOutcome.YES,
                    FateCheckOutcome.YES_BECAUSE, FateCheckOutcome.YES_AND,
                    FateCheckOutcome.FAVORABLE -> MaterialTheme.colorScheme.tertiary
                    FateCheckOutcome.NO_AND, FateCheckOutcome.NO,
                    FateCheckOutcome.NO_BECAUSE, FateCheckOutcome.NO_BUT,
                    FateCheckOutcome.UNFAVORABLE -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.secondary
                },
            )
        }
    }
}

@Composable
fun ExpectationCheckDisplay(
    result: ExpectationCheckResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Outcome", value = result.outcome.name)
            FateDiceRow(result.fateDice)
        }
    }
}

@Composable
fun ScaleDisplay(
    result: ScaleResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Scale", value = result.modifier)
            DetailRow(label = "Sum", value = "${result.fateSum}")
            DetailRow(label = "Intensity", value = "${result.intensity}")
        }
    }
}

@Composable
fun NextSceneDisplay(
    result: NextSceneResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Scene", value = result.sceneType)
            FateDiceRow(result.fateDice)
        }
    }
}

@Composable
fun NextSceneWithFollowUpDisplay(
    result: NextSceneWithFollowUpResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Scene", value = result.sceneType)
            FateDiceRow(result.fateDice)
            if (result.focusResult != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Focus: ${result.focusResult.focus}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
fun FocusDisplay(
    result: FocusResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Focus", value = result.focus)
    }
}

@Composable
fun DiscoverMeaningDisplay(
    result: DiscoverMeaningResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.phrase,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Adjective", value = "${result.adjective} (${result.adjectiveRoll})")
            DetailRow(label = "Noun", value = "${result.noun} (${result.nounRoll})")
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Random Event Displays
// ═══════════════════════════════════════════════════════════════

@Composable
fun RandomEventDisplay(
    result: RandomEventResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.eventPhrase,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Focus", value = "${result.focus} (${result.focusRoll})")
            DetailRow(label = "Modifier", value = "${result.modifier} (${result.modifierRoll})")
            DetailRow(label = "Idea", value = "${result.idea} (${result.ideaRoll})")
        }
    }
}

@Composable
fun RandomEventFocusDisplay(
    result: RandomEventFocusResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Focus", value = result.focus)
    }
}

@Composable
fun IdeaDisplay(
    result: IdeaResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = result.phrase,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            DetailRow(label = result.ideaCategory, value = result.idea)
        }
    }
}

@Composable
fun SingleTableDisplay(
    result: SingleTableResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Table", value = result.tableName)
            DetailRow(label = "Roll", value = "${result.roll}")
        }
    }
}
