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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
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
import com.juiceroll.generator.flavor.LocationGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.CategoryExplore
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Location Grid — a 5x5 bullseye grid for determining
 * direction and distance using 1d100.
 *
 * Two methods:
 * - Compass Method: Direction + Distance from center
 * - Zoom Method: Iterative zooming into regions
 */
@Composable
fun LocationDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    locationGenerator: LocationGenerator = remember { LocationGenerator() },
) {
    val accentColor = Rust

    OracleDialog(
        title = "Location Grid",
        icon = Icons.Filled.Map,
        accentColor = accentColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = accentColor.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f)),
        ) {
            Text(
                text = "A 5\u00D75 bullseye grid. Roll 1d100 to get both "
                        + "a direction and a distance.",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Parchment.copy(alpha = 0.85f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Compass Method ──
        MethodCard(
            title = "Compass Method",
            color = CategoryExplore,
            description = "Imagine your PC at the center. Roll to get:\n"
                    + "\u2022 Direction (N, S, E, W, NE, NW, SE, SW)\n"
                    + "\u2022 Distance (Close or Far based on ring)",
            useFor = "Next town, hex population, travel days, roads",
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ── Zoom Method ──
        MethodCard(
            title = "Zoom Method",
            color = Mystic,
            description = "Use iterative zooming:\n"
                    + "1. Grid overlays world map \u2192 roll to zoom in\n"
                    + "2. Grid overlays region \u2192 roll again\n"
                    + "3. Grid overlays settlement \u2192 roll for building\n"
                    + "4. Keep zooming until you have your answer",
            useFor = "Remote Events, hidden treasure locations",
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Roll Button ──
        Surface(
            onClick = {
                onRoll(locationGenerator.generate())
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = accentColor,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Roll 1d100",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = Color.Black,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Grid Visual ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF1C1814).copy(alpha = 0.5f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "N",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = CategoryExplore,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "W",
                        modifier = Modifier.padding(end = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = CategoryExplore,
                    )
                    Column {
                        for (row in 0..4) {
                            Row {
                                for (col in 0..4) {
                                    val isCenter = row == 2 && col == 2
                                    val isClose =
                                        !isCenter && row in 1..3 && col in 1..3

                                    val cellColor = when {
                                        isCenter -> Gold
                                        isClose -> accentColor
                                        else -> ParchmentDark
                                    }
                                    val symbol = when {
                                        isCenter -> "\u25C9"
                                        isClose -> "\u25CB"
                                        else -> "\u00B7"
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .padding(1.dp)
                                            .size(24.dp),
                                        shape = RoundedCornerShape(3.dp),
                                        color = cellColor.copy(
                                            alpha = if (isCenter) 0.3f else 0.15f,
                                        ),
                                        border = BorderStroke(
                                            width = if (isCenter) 1.5.dp else 0.5.dp,
                                            color = cellColor.copy(alpha = 0.4f),
                                        ),
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = symbol,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = cellColor,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "E",
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = CategoryExplore,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = CategoryExplore,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    LegendItem("\u25C9", "Center", Gold)
                    LegendItem("\u25CB", "Close", accentColor)
                    LegendItem("\u00B7", "Far", ParchmentDark)
                }
            }
        }

    }
}

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun MethodCard(
    title: String,
    color: Color,
    description: String,
    useFor: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Parchment.copy(alpha = 0.9f),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = color.copy(alpha = 0.1f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "\uD83D\uDCA1 $useFor",
                        style = MaterialTheme.typography.bodySmall,
                        color = color.copy(alpha = 0.9f),
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(symbol: String, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            color = ParchmentDark.copy(alpha = 0.7f),
        )
    }
}
