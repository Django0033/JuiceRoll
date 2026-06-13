package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.flavor.AbstractIconsGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Abstract Icons — roll 1d10 + 1d6 for visual inspiration.
 *
 * Based on Juice Oracle Right Extension. These abstract selections were
 * inspired by Rory's Story Cubes. Text-only display (images deferred).
 */
@Composable
fun AbstractIconsDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    abstractIconsGenerator: AbstractIconsGenerator = remember { AbstractIconsGenerator() },
) {
    val accentColor = Success

    OracleDialog(
        title = "Abstract Icons",
        icon = Icons.Filled.Brush,
        accentColor = accentColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Header ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = accentColor.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Visual Inspiration \u2022 Symbol Interpretation",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Roll 1d10 + 1d6 to pick an icon. These abstract images can be "
                            + "used for inspiration instead of words. Inspired by Rory's Story Cubes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Grid Visualization ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF1C1814).copy(alpha = 0.5f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Grid header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Icon Grid",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Mystic,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Mystic.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = "10 \u00D7 6 = 60 icons",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = Mystic,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Mini grid preview (4x4 sample)
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Column labels
                        Row {
                            Spacer(modifier = Modifier.width(16.dp))
                            for (c in 1..4) {
                                CellLabel(text = "$c")
                            }
                        }
                        // Grid rows
                        for (r in 1..4) {
                            Row {
                                RowLabel(text = "$r")
                                for (c in 1..4) {
                                    MiniCell(isHighlighted = r == 2 && c == 3)
                                }
                            }
                        }
                        // Ellipsis row
                        Row {
                            RowLabel(text = "\u22EE")
                            for (c in 1..5) {
                                Box(modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dice indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    DiceLabel("1d10 \u2192 Row", Rust)
                    Spacer(modifier = Modifier.width(8.dp))
                    DiceLabel("1d6 \u2192 Col", Info)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Uses ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF241E1A).copy(alpha = 0.4f),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Uses",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                UseRow("Alternative to word-based meaning tables")
                UseRow("Visual inspiration for scenes or encounters")
                UseRow("Interpret the symbol in your current context")
                UseRow("Use multiple icons for complex situations")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Roll Button ──
        Surface(
            onClick = {
                onRoll(abstractIconsGenerator.generate())
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = accentColor.copy(alpha = 0.9f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Roll 1d10 + 1d6",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = Color(0xFF241E1A),
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Rows: 1-9, 0  \u2022  Columns: 1-6",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = ParchmentDark.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
        )

    }
}

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun CellLabel(text: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .size(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = ParchmentDark,
        )
    }
}

@Composable
private fun RowLabel(text: String) {
    Box(
        modifier = Modifier.size(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = ParchmentDark,
        )
    }
}

@Composable
private fun MiniCell(isHighlighted: Boolean = false) {
    Surface(
        modifier = Modifier
            .padding(1.dp)
            .size(20.dp),
        shape = RoundedCornerShape(3.dp),
        color = if (isHighlighted) Success.copy(alpha = 0.4f)
                else Color(0xFF241E1A).copy(alpha = 0.6f),
        border = BorderStroke(
            width = if (isHighlighted) 1.5.dp else 0.5.dp,
            color = if (isHighlighted) Success else ParchmentDark.copy(alpha = 0.3f),
        ),
    ) {
        if (isHighlighted) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(20.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null,
                    tint = Success,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
private fun DiceLabel(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}

@Composable
private fun UseRow(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Parchment.copy(alpha = 0.9f),
        )
    }
}

