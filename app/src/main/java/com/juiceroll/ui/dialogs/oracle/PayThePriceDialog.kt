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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.juiceroll.generator.challenge.PayThePriceGenerator
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SimpleOracleDialog
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia

/**
 * Dialog for Pay the Price — consequences on failure.
 *
 * Provides standard consequence (1d10) and Major Plot Twist (critical failure).
 */
@Composable
fun PayThePriceDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    payThePriceGenerator: PayThePriceGenerator = remember { PayThePriceGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SimpleOracleDialog(
        title = "Pay the Price",
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Rust.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Rust.copy(alpha = 0.2f)),
        ) {
            Text(
                text = "So you failed a challenge. Time to Pay The Price! "
                        + "Use this to determine the effect of your failure.",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Parchment.copy(alpha = 0.85f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Pay The Price button ──
        Surface(
            onClick = {
                currentResult = payThePriceGenerator.rollConsequence()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Rust,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Pay The Price",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Parchment,
                )
                Text(
                    text = "Standard consequence (1d10)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Standard consequences ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Sepia.copy(alpha = 0.08f),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Possible Outcomes:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(4.dp))
                val outcomes = listOf(
                    "Unintended Effect", "Situation Worsens", "Delayed",
                    "Act Against Intentions", "New Danger", "Community in Danger",
                    "Separated", "Value Lost", "Complication", "Betrayal",
                )
                // Wrap-like layout in 2 columns
                for (i in outcomes.indices step 2) {
                    Row {
                        OutcomeChip(outcomes[i])
                        if (i + 1 < outcomes.size) {
                            Spacer(modifier = Modifier.width(4.dp))
                            OutcomeChip(outcomes[i + 1])
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Major Plot Twist section ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Danger.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Danger.copy(alpha = 0.2f)),
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = "For \"Miss with a Match\" or Critical Fail, "
                            + "use the Major Plot Twist:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            onClick = {
                currentResult = payThePriceGenerator.rollMajorTwist()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Danger.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, Danger.copy(alpha = 0.4f)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Major Plot Twist",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Danger,
                )
                Text(
                    text = "Critical failure consequence (1d10)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Danger.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Danger.copy(alpha = 0.05f),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Possible Twists:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Danger,
                )
                Spacer(modifier = Modifier.height(4.dp))
                val twists = listOf(
                    "Benefits Enemy", "Assumption False", "Dark Secret",
                    "Enemy Allies", "Common Goal", "Diversion",
                    "Secret Alliance", "Someone Returns", "Connected", "Too Late",
                )
                for (i in twists.indices step 2) {
                    Row {
                        TwistChip(twists[i])
                        if (i + 1 < twists.size) {
                            Spacer(modifier = Modifier.width(4.dp))
                            TwistChip(twists[i + 1])
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Result Display ──
        RollResultSection(result = currentResult)

        // ── Save & Close ──
        if (currentResult != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    currentResult?.let { onRoll(it) }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold.copy(alpha = 0.2f),
                    contentColor = Gold,
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Save & Close", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun OutcomeChip(label: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Rust.copy(alpha = 0.15f),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Parchment,
        )
    }
}

@Composable
private fun TwistChip(label: String) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Danger.copy(alpha = 0.15f),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Parchment,
        )
    }
}
