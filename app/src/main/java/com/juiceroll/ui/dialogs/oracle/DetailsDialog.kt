package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.challenge.DetailsGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Ink
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Details — color, property, and history generation.
 *
 * Provides four sections:
 * - Color: roll for a color (d10)
 * - Property: roll for one or two properties with intensity (d10 + d6)
 * - Detail: roll with optional positive/negative skew
 * - History: roll with optional recent/distant skew
 */
@Composable
fun DetailsDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    detailsGenerator: DetailsGenerator = remember { DetailsGenerator() },
) {
    val colorSectionColor = Info
    val propertySectionColor = Gold
    val detailSectionColor = Mystic
    val historySectionColor = Rust

    OracleDialog(
        title = "Details",
        icon = Icons.Filled.AutoFixHigh,
        accentColor = Gold,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Gold.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.2f)),
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = "Add flavor to NPCs, items, settlements, "
                            + "or interpret oracle results.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // COLOR SECTION
        // ══════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = colorSectionColor.copy(alpha = 0.06f),
            border = BorderStroke(1.dp, colorSectionColor.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Palette,
                        contentDescription = null,
                        tint = colorSectionColor,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorSectionColor,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                // Color swatches preview
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    color = Ink.copy(alpha = 0.5f),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        listOf(
                            0xFF3D3029, 0xFF8B6F47, 0xFFC7A34F, 0xFF2E7D32,
                            0xFF1565C0, 0xFFC62828, 0xFF7B1FA2, 0xFF9E9E9E,
                            0xFFFFB300, 0xFFBDBDBD,
                        ).forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(
                                        color = Color(hex),
                                        shape = RoundedCornerShape(3.dp),
                                    ),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Roll button
                OutlinedButton(
                    onClick = {
                        val result = detailsGenerator.rollColor()
                        onRoll(result)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorSectionColor,
                    ),
                    border = BorderStroke(
                        1.dp,
                        colorSectionColor.copy(alpha = 0.3f),
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Colorize,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Roll Color", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "d10",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentDark,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ══════════════════════════════════════════════════════
        // PROPERTY SECTION
        // ══════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.5.dp, propertySectionColor.copy(alpha = 0.4f)),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Highlighted header
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    color = propertySectionColor.copy(alpha = 0.15f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = null,
                            tint = propertySectionColor,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Property",
                            style = MaterialTheme.typography.labelLarge,
                            color = propertySectionColor,
                        )
                    }
                }
                Column(modifier = Modifier.padding(10.dp)) {
                    // Essential badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = propertySectionColor.copy(alpha = 0.3f),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = propertySectionColor,
                                modifier = Modifier.size(10.dp),
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "ESSENTIAL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = propertySectionColor,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Info boxes
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            color = Ink.copy(alpha = 0.3f),
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = Rust.copy(alpha = 0.3f),
                                ) {
                                    Text(
                                        text = "d10",
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Rust,
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Property",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = "Age \u00B7 Size \u00B7 Value \u00B7 Style \u00B7 Power \u00B7 Quality...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentDark,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            color = Ink.copy(alpha = 0.3f),
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = Info.copy(alpha = 0.3f),
                                ) {
                                    Text(
                                        text = "d6",
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Info,
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Intensity",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(
                                    text = "Minimal \u2192 Minor \u2192 Mundane \u2192 Major \u2192 Max",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentDark,
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Roll buttons
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = {
                                val result = detailsGenerator.rollTwoProperties()
                                onRoll(result)
                                onDismiss()
                            },
                            modifier = Modifier.weight(3f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = propertySectionColor,
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                propertySectionColor.copy(alpha = 0.5f),
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    "Property x2",
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Text(
                                    "Recommended",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentDark,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = {
                                val result = detailsGenerator.rollProperty()
                                onRoll(result)
                                onDismiss()
                            },
                            modifier = Modifier.weight(2f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = propertySectionColor,
                            ),
                            border = BorderStroke(
                                1.dp,
                                propertySectionColor.copy(alpha = 0.3f),
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LooksOne,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text("x1", style = MaterialTheme.typography.labelMedium)
                                Text(
                                    "Single",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentDark,
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ══════════════════════════════════════════════════════
        // DETAIL SECTION
        // ══════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = detailSectionColor.copy(alpha = 0.06f),
            border = BorderStroke(1.dp, detailSectionColor.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoFixHigh,
                        contentDescription = null,
                        tint = detailSectionColor,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Detail",
                        style = MaterialTheme.typography.labelLarge,
                        color = detailSectionColor,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Oracle threw a curveball? Ground meaning to a thread, character, or emotion.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        val result = detailsGenerator.rollDetailWithFollowUp()
                        onRoll(result)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = detailSectionColor,
                    ),
                    border = BorderStroke(
                        1.dp,
                        detailSectionColor.copy(alpha = 0.3f),
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        "Roll Detail",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Skew buttons row
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = {
                            val result = detailsGenerator.rollDetailWithFollowUp(skew = "advantage")
                            onRoll(result)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Success,
                        ),
                        border = BorderStroke(
                            1.dp,
                            Success.copy(alpha = 0.35f),
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Positive",
                                style = MaterialTheme.typography.labelMedium,
                                color = Success,
                            )
                            Text(
                                "Favorable",
                                style = MaterialTheme.typography.bodySmall,
                                color = Success.copy(alpha = 0.9f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            val result = detailsGenerator.rollDetailWithFollowUp(skew = "disadvantage")
                            onRoll(result)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Danger,
                        ),
                        border = BorderStroke(
                            1.dp,
                            Danger.copy(alpha = 0.35f),
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Negative",
                                style = MaterialTheme.typography.labelMedium,
                                color = Danger,
                            )
                            Text(
                                "Unfavorable",
                                style = MaterialTheme.typography.bodySmall,
                                color = Danger.copy(alpha = 0.9f),
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ══════════════════════════════════════════════════════
        // HISTORY SECTION
        // ══════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = historySectionColor.copy(alpha = 0.06f),
            border = BorderStroke(1.dp, historySectionColor.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = historySectionColor,
                        modifier = Modifier.size(15.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.labelLarge,
                        color = historySectionColor,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tie elements to the past: backstory, past scenes, previous actions, or threads.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        val result = detailsGenerator.rollHistory()
                        onRoll(result)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = historySectionColor,
                    ),
                    border = BorderStroke(
                        1.dp,
                        historySectionColor.copy(alpha = 0.3f),
                    ),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        "Roll History",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Skew buttons row
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = {
                            val result = detailsGenerator.rollHistory(skew = "advantage")
                            onRoll(result)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Info,
                        ),
                        border = BorderStroke(
                            1.dp,
                            Info.copy(alpha = 0.35f),
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Recent",
                                style = MaterialTheme.typography.labelMedium,
                                color = Info,
                            )
                            Text(
                                "Present",
                                style = MaterialTheme.typography.bodySmall,
                                color = Info.copy(alpha = 0.9f),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            val result = detailsGenerator.rollHistory(skew = "disadvantage")
                            onRoll(result)
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Sepia,
                        ),
                        border = BorderStroke(
                            1.dp,
                            Sepia.copy(alpha = 0.35f),
                        ),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Distant",
                                style = MaterialTheme.typography.labelMedium,
                                color = Sepia,
                            )
                            Text(
                                "Past",
                                style = MaterialTheme.typography.bodySmall,
                                color = Sepia.copy(alpha = 0.9f),
                            )
                        }
                    }
                }
            }
        }

    }
}
