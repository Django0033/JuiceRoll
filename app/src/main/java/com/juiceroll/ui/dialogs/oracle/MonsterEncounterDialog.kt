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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.data.oracle.MonsterEncounterData
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.flavor.MonsterEncounterGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.theme.CategoryCombat
import com.juiceroll.ui.theme.CategoryExplore
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.JuiceOrange
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Monster Encounter options.
 * Environment-based encounters with difficulty levels.
 */
@Composable
fun MonsterEncounterDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    monsterGenerator: MonsterEncounterGenerator = remember { MonsterEncounterGenerator() },
) {
    var selectedEnvironment by remember { mutableStateOf(6) } // Default to Forest
    var showEnvironmentMenu by remember { mutableStateOf(false) }

    val envIndex = (selectedEnvironment - 1).coerceIn(0, 9)
    val envName = MonsterEncounterData.environmentNames[envIndex]
    val envFormula = monsterGenerator.getEnvironmentFormula(selectedEnvironment)
    val combatColor = CategoryCombat
    val exploreColor = CategoryExplore

    OracleDialog(
        title = "Monster Encounter",
        icon = Icons.Filled.PestControl,
        accentColor = combatColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Environment Section ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = exploreColor.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, exploreColor.copy(alpha = 0.4f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = null,
                        tint = exploreColor,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Environment: $envName ($envFormula)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = exploreColor,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Environment dropdown
                Surface(
                    onClick = { showEnvironmentMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    color = exploreColor.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, exploreColor.copy(alpha = 0.3f)),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Select Environment",
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentDark,
                            )
                            Text(
                                text = "${selectedEnvironment}. $envName ($envFormula)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Parchment,
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Select environment",
                            tint = Parchment,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                // Environment dropdown menu
                DropdownMenu(
                    expanded = showEnvironmentMenu,
                    onDismissRequest = { showEnvironmentMenu = false },
                ) {
                    MonsterEncounterData.environmentNames.forEachIndexed { index, name ->
                        val formula = monsterGenerator.getEnvironmentFormula(index + 1)
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${index + 1}. ",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Monospace,
                                        color = Sepia,
                                    )
                                    Text(
                                        text = name,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = Parchment,
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Gold.copy(alpha = 0.15f),
                                    ) {
                                        Text(
                                            text = formula,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Gold,
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedEnvironment = index + 1
                                showEnvironmentMenu = false
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Full Encounter Button ──
        MonsterPrimaryButton(
            title = "Full Encounter (By Environment)",
            subtitle = "Row ($envFormula) + Difficulty (2d10) + Counts (1d6-1@)",
            icon = Icons.Filled.Groups,
            onClick = {
                onRoll(monsterGenerator.generateFullEncounter(selectedEnvironment))
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // QUICK ROLLS
        // ════════════════════════════════════════════════════════════
        SectionHeader(
            title = "Quick Rolls",
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Deadly explanation
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            color = Sepia.copy(alpha = 0.08f),
        ) {
            Row(
                modifier = Modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.FlashOn,
                    contentDescription = null,
                    tint = Sepia,
                    modifier = Modifier.size(11.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Deadly if: Total CR > Total Level \u00F7 (2 if level > 4, else 4),"
                            + " or any single CR > Level",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Sepia,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            MonsterQuickButton(
                title = "Roll Encounter",
                subtitle = "2d10 for row + difficulty\ndoubles = boss",
                icon = Icons.Filled.Casino,
                color = combatColor,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollEncounter())
                onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            MonsterQuickButton(
                title = "Roll Tracks",
                subtitle = "1d6-1@ with disadvantage",
                icon = Icons.Filled.Pets,
                color = Sepia,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollTracks())
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // BY DIFFICULTY
        // ════════════════════════════════════════════════════════════
        SectionHeader(title = "By Difficulty")
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            MonsterDifficultyChip(
                label = "Easy (1-4)",
                subtitle = "Lower CR monsters",
                color = Success,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollEncounter(forcedDifficulty = "easy"))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            MonsterDifficultyChip(
                label = "Medium (5-8)",
                subtitle = "Standard CR",
                color = JuiceOrange,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollEncounter(forcedDifficulty = "medium"))
                    onDismiss()
                },
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            MonsterDifficultyChip(
                label = "Hard (9-0)",
                subtitle = "Higher CR monsters",
                color = Danger,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollEncounter(forcedDifficulty = "hard"))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            MonsterDifficultyChip(
                label = "Boss",
                subtitle = "Legendary monster",
                color = Mystic,
                icon = Icons.Filled.Star,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollEncounter(forcedDifficulty = "boss"))
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // SPECIAL ROWS
        // ════════════════════════════════════════════════════════════
        SectionHeader(title = "Special Rows")
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            MonsterSpecialRowButton(
                label = "* (Nature/Plants)",
                subtitle = "Blights, hags, plant creatures",
                icon = Icons.Filled.Eco,
                color = exploreColor,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollSpecialRow(humanoid = false))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            MonsterSpecialRowButton(
                label = "** (Humanoids)",
                subtitle = "Bandits, scouts, veterans",
                icon = Icons.Filled.Person,
                color = Rust,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(monsterGenerator.rollSpecialRow(humanoid = true))
                    onDismiss()
                },
            )
        }

    }
}

// ══════════════════════════════════════════════════════════════════
// Private helper composables
// ══════════════════════════════════════════════════════════════════

@Composable
private fun MonsterPrimaryButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
) {
    val color = CategoryCombat
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = color.copy(alpha = 0.2f),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(6.dp).size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
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
private fun MonsterQuickButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
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
            modifier = Modifier.padding(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
            )
        }
    }
}

@Composable
private fun MonsterDifficultyChip(
    label: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
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
private fun MonsterSpecialRowButton(
    label: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
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
            modifier = Modifier.padding(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
        }
    }
}
