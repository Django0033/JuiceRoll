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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.juiceroll.generator.character.NameGenerator
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.dialogs.SimpleOracleDialog
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Name Generator options.
 *
 * Provides Simple Method (3d20) and Pattern Method (1d20 with gender skew)
 * for generating fantasy names from syllable tables.
 */
@Composable
fun NameGeneratorDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    nameGenerator: NameGenerator = remember { NameGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SimpleOracleDialog(
        title = "Name Generator",
        onDismissRequest = onDismiss,
    ) {
        // ── Simple Method ──
        SectionHeader(title = "Simple Method")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Quick random names using 3d20",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(8.dp))

        SimpleNameOption(
            title = "3d20 (Columns 1, 2, 3)",
            subtitle = "Roll on all three columns",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                currentResult = nameGenerator.generate()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        SimpleNameOption(
            title = "3d20 (Column 1 Only)",
            subtitle = "Roll on column 1 three times",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                currentResult = nameGenerator.generateColumn1Only()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ── Pattern Method ──
        SectionHeader(title = "Pattern Method")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Use pattern column for structured names",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(8.dp))

        SimpleNameOption(
            title = "Neutral",
            subtitle = "Roll 1d20 for pattern",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                currentResult = nameGenerator.generatePatternNeutral()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Masculine / Feminine row
        Row(modifier = Modifier.fillMaxWidth()) {
            GenderNameOption(
                title = "Masculine",
                subtitle = "@- (disadvantage)",
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = nameGenerator.generateMasculine()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            GenderNameOption(
                title = "Feminine",
                subtitle = "@+ (advantage)",
                modifier = Modifier.weight(1f),
                onClick = {
                    currentResult = nameGenerator.generateFeminine()
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Examples ──
        SectionHeader(title = "Examples")
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Gold.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "• Simple: Tolimaea, Mayosid, Nenetar",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
                )
                Text(
                    text = "• Masculine: Osuma, Likel, Risan",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
                )
                Text(
                    text = "• Feminine: Nedeli, Eyosi, Kisora",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
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
private fun SimpleNameOption(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Gold.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Parchment,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
            }
        }
    }
}

@Composable
private fun GenderNameOption(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Info.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Info.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Info,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Info.copy(alpha = 0.8f),
            )
        }
    }
}
