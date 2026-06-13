package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Star
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
import com.juiceroll.generator.world.ObjectTreasureGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.CategoryWorld
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
 * Dialog for Treasure generation.
 *
 * Provides item creation procedure, category selection (Trinket, Treasure,
 * Document, Accessory, Weapon, Armor), and full item generation with properties.
 */
@Composable
fun TreasureDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    treasureGenerator: ObjectTreasureGenerator = remember { ObjectTreasureGenerator() },
) {
    var includeColor by remember { mutableStateOf(false) }
    val accentColor = Gold

    OracleDialog(
        title = "Treasure",
        icon = Icons.Filled.Diamond,
        accentColor = accentColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Procedure ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = accentColor.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Item Creation Procedure:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "1. Roll 4d6 on Object/Treasure table\n"
                            + "2. Roll two properties (1d10+1d6 each)\n"
                            + "3. Optionally roll color for appearance/elemental",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Color toggle ──
        Surface(
            onClick = { includeColor = !includeColor },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Mystic.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.2f)),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = if (includeColor) Mystic else Color.Transparent,
                    border = BorderStroke(
                        1.dp,
                        if (includeColor) Mystic else Mystic.copy(alpha = 0.6f),
                    ),
                ) {
                    Text(
                        text = " ",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(width = 12.dp, height = 12.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Include Color (appearance/elemental)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (includeColor) FontWeight.Bold else FontWeight.Normal,
                    color = if (includeColor) Mystic else ParchmentDark,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ── Create Full Item ──
        Surface(
            onClick = {
                onRoll(treasureGenerator.generateFullItem(
                    skew = "none", includeColor = includeColor,
                ))
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = accentColor.copy(alpha = 0.2f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Full Item",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                )
            }
        }
        Text(
            text = "4d6 + 2 Properties${if (includeColor) " + Color" else ""}",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            color = ParchmentDark.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // ROLL 4D6
        // ══════════════════════════════════════════════════════
        Text(
            text = "Roll 4d6",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = accentColor,
        )
        Spacer(modifier = Modifier.height(6.dp))
        TreasureOption(
            title = "Random Treasure (4d6)",
            subtitle = "Category + Properties",
            onClick = {
                onRoll(treasureGenerator.generate(skew = "none"))
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // BY CATEGORY
        // ══════════════════════════════════════════════════════
        Text(
            text = "By Category (3d6)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = CategoryWorld,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Pick a specific category and roll 3d6 for properties:",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Category grid - 2 columns
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                number = "1", title = "Trinket",
                properties = "Quality + Material + Type",
                color = Sepia,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateTrinket())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            CategoryCard(
                number = "2", title = "Treasure",
                properties = "Quality + Container + Contents",
                color = Gold,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateTreasure())
                    onDismiss()
                },
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                number = "3", title = "Document",
                properties = "Type + Content + Subject",
                color = ParchmentDark,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateDocument())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            CategoryCard(
                number = "4", title = "Accessory",
                properties = "Quality + Material + Type",
                color = Mystic,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateAccessory())
                    onDismiss()
                },
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CategoryCard(
                number = "5", title = "Weapon",
                properties = "Quality + Material + Type",
                color = Danger,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateWeapon())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            CategoryCard(
                number = "6", title = "Armor",
                properties = "Quality + Material + Type",
                color = Info,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(treasureGenerator.generateArmor())
                    onDismiss()
                },
            )
        }

    }
}

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun TreasureOption(
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

@Composable
private fun CategoryCard(
    number: String,
    title: String,
    properties: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = color.copy(alpha = 0.2f),
            ) {
                Text(
                    text = number,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
                Text(
                    text = properties,
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
            }
        }
    }
}

