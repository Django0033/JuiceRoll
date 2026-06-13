package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.challenge.ChallengeGenerator
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.dialogs.SimpleOracleDialog
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Challenge generation — skill challenges and DCs.
 *
 * Provides:
 * - Full Challenge (Physical + Mental with difficulty options)
 * - DC Methods (Quick, Random, Balanced, Easy, Hard)
 * - Individual Skills (Physical, Mental)
 * - Examples section
 * - Percentage Chance roll
 */
@Composable
fun ChallengeDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    challengeGenerator: ChallengeGenerator = remember { ChallengeGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }
    val accentColor = Rust

    SimpleOracleDialog(
        title = "Challenge",
        onDismissRequest = onDismiss,
    ) {
        // ── Challenge Procedure ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = accentColor.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Challenge Procedure",
                        style = MaterialTheme.typography.labelMedium,
                        color = Parchment,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1. Roll Physical + Mental challenge with DCs\n"
                            + "2. Create a situation where both make sense\n"
                            + "3. Choose ONE path \u2014 only need to pass one!\n"
                            + "4. Fail = Pay The Price (may lock out other option)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // FULL CHALLENGE
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Full Challenge")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Rolls 1 Physical + 1 Mental with separate DCs for each:",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            ChallengeChip(
                label = "Random DCs",
                hint = "1d10 each",
                color = ParchmentDark,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollFullChallenge()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            ChallengeChip(
                label = "Easy DCs",
                hint = "advantage",
                color = Success,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollFullChallenge(dcSkew = "advantage")
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            ChallengeChip(
                label = "Hard DCs",
                hint = "disadvantage",
                color = Danger,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollFullChallenge(dcSkew = "disadvantage")
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // DC METHODS
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "DC Methods")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "5 ways to generate a DC:",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(6.dp))
        // First row: Quick DC, Random DC
        Row(modifier = Modifier.fillMaxWidth()) {
            DcOption(
                title = "Quick DC",
                subtitle = "2d6+6",
                range = "8-18",
                color = Gold,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollQuickDc()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            DcOption(
                title = "Random DC",
                subtitle = "1d10",
                range = "8-17",
                color = ParchmentDark,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollDc()
                },
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        // Second row: Balanced, Easy, Hard
        Row(modifier = Modifier.fillMaxWidth()) {
            DcOption(
                title = "Balanced DC",
                subtitle = "1d100 bell",
                range = "middle DCs",
                color = Info,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollBalancedDc()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            DcOption(
                title = "Easy DC",
                subtitle = "1d10@+",
                range = "lower DC",
                color = Success,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollDc(skew = "advantage")
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            DcOption(
                title = "Hard DC",
                subtitle = "1d10@\u2212",
                range = "higher DC",
                color = Danger,
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollDc(skew = "disadvantage")
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // INDIVIDUAL SKILLS
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Individual Skills")
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            SkillButton(
                title = "Physical",
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                color = Rust,
                skills = "Medicine, Survival, Athletics...",
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollPhysicalChallenge()
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            SkillButton(
                title = "Mental",
                icon = Icons.Filled.Psychology,
                color = Mystic,
                skills = "Nature, Arcana, Insight...",
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = challengeGenerator.rollMentalChallenge()
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // PERCENTAGE CHANCE
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "% Chance")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Roll a d10 to determine a percentage range:",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedButton(
            onClick = {
                currentResult = challengeGenerator.rollPercentageChance()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Sepia,
            ),
            border = BorderStroke(
                1.dp,
                Sepia.copy(alpha = 0.3f),
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                "Roll % Chance",
                style = MaterialTheme.typography.labelMedium,
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // EXAMPLES
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Examples")
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Sepia.copy(alpha = 0.1f),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                ExampleRow(
                    rolls = "8,2",
                    physical = "Stealth",
                    mental = "Nature",
                    scenario = "Capture an elusive creature",
                )
                Spacer(modifier = Modifier.height(4.dp))
                ExampleRow(
                    rolls = "7,6",
                    physical = "Sleight of Hand",
                    mental = "Language",
                    scenario = "Communicate with natives",
                )
                Spacer(modifier = Modifier.height(4.dp))
                ExampleRow(
                    rolls = "9,7",
                    physical = "Acrobatics",
                    mental = "Religion",
                    scenario = "Display martial arts/tai chi",
                )
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

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ChallengeChip(
    label: String,
    hint: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                textAlign = TextAlign.Center,
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
        }
    }
}

@Composable
private fun DcOption(
    title: String,
    subtitle: String,
    range: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
            Text(
                text = range,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun SkillButton(
    title: String,
    icon: ImageVector,
    color: Color,
    skills: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = skills,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
        }
    }
}

@Composable
private fun ExampleRow(
    rolls: String,
    physical: String,
    mental: String,
    scenario: String,
) {
    Row {
        Surface(
            shape = RoundedCornerShape(3.dp),
            color = Rust.copy(alpha = 0.2f),
        ) {
            Text(
                text = rolls,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Rust,
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$physical or $mental \u2014 $scenario",
            style = MaterialTheme.typography.bodySmall,
            color = Parchment,
        )
    }
}
