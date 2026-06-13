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
// NPC Display Composables
// ═══════════════════════════════════════════════════════════════

@Composable
fun NpcActionDisplay(
    result: NpcActionResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Action", value = "${result.result} (${result.roll})")
            result.dieSize?.let { DetailRow(label = "Die", value = "d$it") }
        }
    }
}

@Composable
fun MotiveWithFollowUpDisplay(
    result: MotiveWithFollowUpResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Motive", value = result.motive)
            result.followUp?.let { followUp ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Follow-up: ${followUp.modifier} ${followUp.idea}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
fun SimpleNpcProfileDisplay(
    result: SimpleNpcProfileResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Personality", value = result.personality)
            DetailRow(label = "Need", value = "${result.need} (${result.needRoll})")
            DetailRow(label = "Motive", value = "${result.motive} (${result.motiveRoll})")
        }
    }
}

@Composable
fun NpcProfileDisplay(
    result: NpcProfileResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.primaryPersonality}, yet ${result.secondaryPersonality}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
            DetailRow(label = "Need", value = result.need)
            DetailRow(label = "Motive", value = result.motive)
        }
    }
}

@Composable
fun DualPersonalityDisplay(
    result: DualPersonalityResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            Text(
                text = "${result.primary}, yet ${result.secondary}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
fun ComplexNpcDisplay(
    result: ComplexNpcResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            result.name?.let { name ->
                Text(
                    text = name.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            DetailRow(label = "Personality", value = result.primaryPersonality)
            result.secondaryPersonality?.let { DetailRow(label = "Also", value = it) }
            DetailRow(label = "Need", value = result.need)
            DetailRow(label = "Motive", value = result.motive)
        }
    }
}

@Composable
fun InformationDisplay(
    result: InformationResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        Column {
            DetailRow(label = "Type", value = result.informationType)
            DetailRow(label = "Topic", value = result.topic)
        }
    }
}

@Composable
fun CompanionResponseDisplay(
    result: CompanionResponseResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Response", value = result.response)
    }
}

@Composable
fun DialogTopicDisplay(
    result: DialogTopicResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Topic", value = result.topic)
    }
}

@Composable
fun DialogDisplay(
    result: DialogResult,
    modifier: Modifier = Modifier,
) {
    RollResultCard(result = result, modifier = modifier) {
        DetailRow(label = "Direction", value = "${result.directionRoll} → ${result.direction}")
        DetailRow(label = "Tone", value = result.tone)
        DetailRow(label = "Subject", value = "${result.subjectRoll} → ${result.subject}")
        if (!result.isDoubles) {
            DetailRow(label = "Move", value = result.movementDescription)
        }
        DetailRow(
            label = "Fragment",
            value = if (result.isPast) "${result.newFragment} (Past)" else result.newFragment,
        )
        DetailRow(label = "Description", value = result.fragmentDescription)
        if (result.isDoubles) {
            DetailRow(label = "Doubles", value = "Conversation ends!")
        }
    }
}
