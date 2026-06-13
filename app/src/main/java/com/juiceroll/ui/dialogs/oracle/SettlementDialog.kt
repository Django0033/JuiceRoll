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
import androidx.compose.material.icons.filled.LocationCity
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
import com.juiceroll.generator.world.SettlementGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.theme.CategoryWorld
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Settlement generation.
 *
 * Provides village and city generation, individual name/establishment rolls,
 * naming & description tools, and news generation.
 */
@Composable
fun SettlementDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    settlementGenerator: SettlementGenerator = remember { SettlementGenerator() },
) {
    val accentColor = CategoryWorld

    OracleDialog(
        title = "Settlement",
        icon = Icons.Filled.LocationCity,
        accentColor = accentColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = accentColor.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        ) {
            Text(
                text = "Settlements are places to rest, stock up on supplies, "
                        + "collect quests, or chat with NPCs.",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Parchment.copy(alpha = 0.85f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // GENERATE SETTLEMENT
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Generate Settlement")
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SettlementTypeCard(
                title = "Village",
                subtitle = "Smaller, rural",
                mechanics = "1d6@- count\nd6 establishments",
                color = Sepia,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(settlementGenerator.generateVillage())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            SettlementTypeCard(
                title = "City",
                subtitle = "Larger, urban",
                mechanics = "1d6@+ count\nd10 establishments",
                color = Gold,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(settlementGenerator.generateCity())
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ══════════════════════════════════════════════════════
        // INDIVIDUAL ROLLS
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Individual Rolls")
        Spacer(modifier = Modifier.height(6.dp))

            SettlementOption(
                title = "Name (2d10)",
                subtitle = "Also usable for NPC last names",
                onClick = {
                    onRoll(settlementGenerator.generateName())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            SettlementOption(
                title = "Establishment (d6)",
                subtitle = "Village: Stable, Tavern, Inn, Entertainment, General Store, Artisan",
                onClick = {
                    onRoll(settlementGenerator.rollEstablishment(isVillage = true))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            SettlementOption(
                title = "Establishment (d10)",
                subtitle = "City: +Courier, Temple, Guild Hall, Magic Shop",
                onClick = {
                    onRoll(settlementGenerator.rollEstablishment(isVillage = false))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            SettlementOption(
                title = "Artisan (d10)",
                subtitle = "Artist, Baker, Tailor, Tanner, Archer, Blacksmith, Carpenter, Apothecary, Jeweler, Scribe",
                onClick = {
                    onRoll(settlementGenerator.rollEstablishment(isVillage = true))
                    onDismiss()
                },
            )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // NAMING & DESCRIPTION
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Naming & Description")
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Mystic.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.2f)),
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = "Use Color + Object for establishment names "
                            + "(e.g., \"The Crimson Hourglass\").",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = ParchmentDark,
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        SettlementOption(
            title = "Establishment Name",
            subtitle = """Color + Object → "The [Color] [Object]"""",
            onClick = {
                onRoll(settlementGenerator.generateEstablishmentName())
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        SettlementOption(
            title = "Settlement Properties",
            subtitle = "Two properties with intensity (e.g., \"Major Style\" + \"Minimal Weight\")",
            onClick = {
                onRoll(settlementGenerator.generateProperties())
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        SettlementOption(
            title = "Simple NPC",
            subtitle = "Name + Personality + Need + Motive (for establishment owners)",
            onClick = {
                onRoll(settlementGenerator.generateSimpleNpc())
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // NEWS
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "News")
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Rust.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Rust.copy(alpha = 0.2f)),
        ) {
            Text(
                text = "Roll when entering a settlement or on \"Advance Time\" "
                        + "random event. With a Courier, ask for news from other settlements.",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = ParchmentDark,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        SettlementOption(
            title = "News (d10)",
            subtitle = "War, Sickness, Disaster, Crime, Succession, Remote Event, "
                    + "Arrival, Mail, Sale, Celebration",
            onClick = {
                onRoll(settlementGenerator.rollNews())
                onDismiss()
            },
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SettlementTypeCard(
    title: String,
    subtitle: String,
    mechanics: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = color.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mechanics,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = ParchmentDark,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(4.dp),
                color = color.copy(alpha = 0.25f),
            ) {
                Text(
                    text = "Roll",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
        }
    }
}

@Composable
private fun SettlementOption(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
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
