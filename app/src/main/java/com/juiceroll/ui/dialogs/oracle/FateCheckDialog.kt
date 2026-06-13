package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.oracle.FateCheckGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Success

/**
 * Dialog for performing a Fate Check — the core oracle yes/no question.
 *
 * User selects a likelihood, rolls, then saves the result to history.
 * Displays a quick reference table for interpreting fate dice outcomes.
 */
@Composable
fun FateCheckDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    fateCheckGenerator: FateCheckGenerator = remember { FateCheckGenerator() },
) {
    val likelihoods = listOf("Unlikely", "Even Odds", "Likely")
    var selectedLikelihood by remember { mutableStateOf("Even Odds") }
    OracleDialog(
        title = "Fate Check",
        icon = Icons.Filled.AutoAwesome,
        accentColor = Mystic,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Mystic.copy(alpha = 0.1f),
        ) {
            Text(
                text = "Ask a Yes/No question about the world. "
                        + "The dice will answer with intensity and nuance.",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Parchment.copy(alpha = 0.85f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Likelihood selector ──
        Text(
            text = "How likely is it?",
            style = MaterialTheme.typography.labelLarge,
            color = Parchment,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Likelihood chips
        likelihoods.forEach { likelihood ->
            val isSelected = likelihood == selectedLikelihood
            val chipColor = when (likelihood) {
                "Likely" -> Success
                "Unlikely" -> Danger
                else -> Gold
            }
            Surface(
                onClick = { selectedLikelihood = likelihood },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) chipColor.copy(alpha = 0.18f)
                        else chipColor.copy(alpha = 0.06f),
                border = BorderStroke(
                    width = if (isSelected) 1.5.dp else 1.dp,
                    color = if (isSelected) chipColor.copy(alpha = 0.6f)
                            else chipColor.copy(alpha = 0.25f),
                ),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = likelihood,
                        style = MaterialTheme.typography.labelLarge,
                        color = chipColor,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Dice info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "\uD83C\uDFB2",
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "2dF (Primary + Secondary) + 1d6 Intensity",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Roll Button ──
        Button(
            onClick = {
                val result = fateCheckGenerator.check(
                    likelihood = selectedLikelihood,
                )
                onRoll(result)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Mystic.copy(alpha = 0.25f),
                contentColor = Mystic,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "Roll Fate Check",
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ── Quick Reference ──
        HorizontalDivider(
            color = Gold.copy(alpha = 0.2f),
            thickness = 1.dp,
        )
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Quick Reference",
            style = MaterialTheme.typography.labelMedium,
            color = Gold,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                RefRow("++", "Yes And", Success)
                RefRow("+0", "Yes Because*", Gold)
                RefRow("+-", "Yes But", Success)
                RefRow("0+", "Favorable*", Gold)
                RefRow("<0", "Random Event", Mystic)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                RefRow(">0", "Invalid*", Mystic)
                RefRow("0-", "Unfavorable*", Gold)
                RefRow("-+", "No But", Danger)
                RefRow("-0", "No Because*", Gold)
                RefRow("--", "No And", Danger)
            }
        }
    }
}

@Composable
private fun RefRow(symbol: String, label: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = symbol,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Parchment.copy(alpha = 0.7f),
        )
    }
}
