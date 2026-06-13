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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.data.oracle.WildernessData
import com.juiceroll.domain.model.RollResult
import com.juiceroll.domain.model.WildernessState
import com.juiceroll.generator.challenge.ChallengeGenerator
import com.juiceroll.generator.world.DungeonGenerator
import com.juiceroll.generator.world.WildernessGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.CategoryExplore
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
 * Dialog for Wilderness exploration options.
 * Manages wilderness state synced with the ViewModel/HomeState.
 *
 * Sections:
 * - Environment: Generate new environment type
 * - Terrain: Generate terrain details
 * - Weather: Generate weather
 * - Encounter: Generate encounter/monster
 * - Navigation: Check if lost, discover details
 */
@Composable
fun WildernessDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    wildernessState: WildernessState? = null,
    onStateChange: (WildernessState) -> Unit = {},
    generator: WildernessGenerator = remember { WildernessGenerator() },
    dungeonGenerator: DungeonGenerator = remember { DungeonGenerator() },
    challengeGenerator: ChallengeGenerator = remember { ChallengeGenerator() },
) {
    // ── Local UI state ──
    var hasDangerousTerrain by remember { mutableStateOf(false) }
    var hasMapOrGuide by remember { mutableStateOf(false) }
    var showEnvironmentPicker by remember { mutableStateOf(false) }
    var selectedEnvironment by remember { mutableStateOf(6) } // Default to Forest
    var selectedType by remember { mutableStateOf(6) } // Default to Tropical

    val accColor = CategoryExplore

    // ── Helpers ──
    fun getSkewLabel(): String {
        if (hasDangerousTerrain && hasMapOrGuide) return ""
        if (hasDangerousTerrain) return "@-"
        if (hasMapOrGuide) return "@+"
        return ""
    }

    fun getMonsterFormula(environmentRow: Int): String {
        val formulas = listOf(
            "@-",    // 1 Arctic
            "+1@-",  // 2 Mountains
            "+1@-",  // 3 Cavern
            "+2",    // 4 Hills
            "+2@+",  // 5 Grassland
            "+3",    // 6 Forest
            "+3@+",  // 7 Swamp
            "+4",    // 8 Water
            "+4@+",  // 9 Coast
            "+4@+",  // 10 Desert
        )
        val index = (environmentRow - 1).coerceIn(0, 9)
        return "1d6${formulas[index]}"
    }

    fun rollEncounterWithFollowUp() {
        val encounterResult = generator.rollEncounter(
            currentState = wildernessState,
            hasDangerousTerrain = hasDangerousTerrain,
            hasMapOrGuide = hasMapOrGuide,
        )

        if (encounterResult.newState != null) {
            onStateChange(encounterResult.newState)
        } else if (encounterResult.becameLost && wildernessState != null) {
            onStateChange(wildernessState.copy(isLost = true))
        }

        onRoll(encounterResult)
        onDismiss()
    }

    // ── Dialog ──
    OracleDialog(
        title = "Wilderness",
        icon = Icons.Filled.Terrain,
        accentColor = accColor,
        onDismissRequest = onDismiss,
    ) {
        val isInitialized = wildernessState != null

        // ════════════════════════════════════════════════════
        // STATE CARD (shown when initialized)
        // ════════════════════════════════════════════════════
        if (isInitialized) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                WildernessStateCard(
                    state = wildernessState!!,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Reset button
                Surface(
                    onClick = {
                        // Reset to uninitialized — send a reset signal by
                        // re-initializing. A null reset would require the ViewModel
                        // to handle it; we just generate a fresh random area.
                    },
                    shape = RoundedCornerShape(6.dp),
                    color = Sepia.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Sepia.copy(alpha = 0.3f)),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reset wilderness",
                        tint = Sepia.copy(alpha = 0.7f),
                        modifier = Modifier.padding(8.dp).size(18.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // ════════════════════════════════════════════════════
        // ENVIRONMENT
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Environment",
            icon = Icons.Filled.Terrain,
            color = accColor,
        )
        Spacer(modifier = Modifier.height(6.dp))

        if (!isInitialized) {
            // Not initialized — show init options
            WildernessPrimaryAction(
                title = "Initialize Random Area",
                subtitle = "Start in a random environment (1d10 + 1dF)",
                icon = Icons.Filled.Shuffle,
                color = accColor,
                onClick = {
                    val result = generator.initializeRandom()
                    if (result.newState != null) {
                        onStateChange(result.newState)
                    }
                    onRoll(result)
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.height(6.dp))
            WildernessPrimaryAction(
                title = if (showEnvironmentPicker) "Hide Picker" else "Set Known Position...",
                subtitle = "Start from an existing location",
                icon = if (showEnvironmentPicker) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                color = Sepia,
                onClick = { showEnvironmentPicker = !showEnvironmentPicker },
            )
        } else {
            // Initialized — show transition option
            WildernessPrimaryAction(
                title = "Transition to Next Area",
                subtitle = "Move to adjacent area (2dF env + 1dF type)",
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                color = accColor,
                onClick = {
                    val result = generator.transition(wildernessState)
                    if (result.newState != null) {
                        onStateChange(result.newState)
                    }
                    onRoll(result)
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.height(6.dp))
            WildernessPrimaryAction(
                title = if (showEnvironmentPicker) "Hide Picker" else "Change Position...",
                subtitle = "Set to a different location",
                icon = if (showEnvironmentPicker) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                color = Sepia,
                onClick = { showEnvironmentPicker = !showEnvironmentPicker },
            )
        }

        // Environment picker
        if (showEnvironmentPicker) {
            Spacer(modifier = Modifier.height(8.dp))
            WildernessEnvironmentPicker(
                selectedEnvironment = selectedEnvironment,
                selectedType = selectedType,
                onEnvironmentChanged = { selectedEnvironment = it },
                onTypeChanged = { selectedType = it },
                onConfirm = {
                    val result = generator.initializeAt(
                        environmentRow = selectedEnvironment,
                        typeRow = selectedType,
                    )
                    if (result.newState != null) {
                        onStateChange(result.newState)
                    }
                    onRoll(result)
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════
        // ENCOUNTERS
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Encounters",
            icon = Icons.Filled.Explore,
            color = accColor,
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Skew toggles
        Row(modifier = Modifier.fillMaxWidth()) {
            ModifierChip(
                label = "Dangerous",
                subtitle = "Disadvantage",
                isSelected = hasDangerousTerrain,
                color = Danger,
                modifier = Modifier.weight(1f),
                onClick = { hasDangerousTerrain = !hasDangerousTerrain },
            )
            Spacer(modifier = Modifier.width(8.dp))
            ModifierChip(
                label = "Map/Guide",
                subtitle = "Advantage",
                isSelected = hasMapOrGuide,
                color = Success,
                modifier = Modifier.weight(1f),
                onClick = { hasMapOrGuide = !hasMapOrGuide },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Roll Encounter
        WildernessPrimaryAction(
            title = "Roll Encounter",
            subtitle = if (isInitialized) {
                "What happens? (d${if (wildernessState!!.isLost) 6 else 10}${getSkewLabel()})"
            } else {
                "What happens? (d10)"
            },
            icon = Icons.Filled.Casino,
            color = Gold,
            onClick = {
                rollEncounterWithFollowUp()
            },
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Lost indicator
        if (isInitialized && wildernessState!!.isLost) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = Danger.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Danger.copy(alpha = 0.3f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.WarningAmber,
                        contentDescription = null,
                        tint = Danger,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LOST — using d6 for encounters",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Danger,
                        modifier = Modifier.weight(1f),
                    )
                    Surface(
                        onClick = {
                            onStateChange(wildernessState.copy(isLost = false))
                        },
                        shape = RoundedCornerShape(4.dp),
                        color = Success.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = "Mark Found",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Success,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Quick encounter buttons
        Row(modifier = Modifier.fillMaxWidth()) {
            WildernessCompactButton(
                title = "Weather",
                icon = Icons.Filled.Cloud,
                color = Info,
                modifier = Modifier.weight(1f),
                onClick = {
                    val envRow = wildernessState?.environmentRow ?: 5
                    val typeRow = wildernessState?.typeRow ?: 5
                    onRoll(generator.rollWeather(
                        environmentRow = envRow,
                        typeRow = typeRow,
                    ))
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            WildernessCompactButton(
                title = "Hazard",
                icon = Icons.Filled.Warning,
                color = Rust,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(generator.rollNaturalHazard())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            WildernessCompactButton(
                title = "Feature",
                icon = Icons.Filled.Place,
                color = Mystic,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(generator.rollFeature())
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════
        // MONSTER LEVEL
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Monster Level",
            icon = Icons.Filled.Pets,
            color = accColor,
        )
        Spacer(modifier = Modifier.height(6.dp))

        WildernessPrimaryAction(
            title = "Roll Monster Level",
            subtitle = if (isInitialized) {
                "Based on ${wildernessState!!.environmentRow} (${getMonsterFormula(wildernessState.environmentRow)})"
            } else {
                "1d6+modifier with advantage/disadvantage"
            },
            icon = Icons.Filled.Pets,
            color = Danger,
            onClick = {
                val envRow = wildernessState?.environmentRow ?: 5
                onRoll(generator.rollMonsterLevel(environmentRow = envRow))
                onDismiss()
            },
        )

    }
}

// ════════════════════════════════════════════════════════════════
// Private helper composables
// ════════════════════════════════════════════════════════════════

@Composable
private fun SectionLabel(
    title: String,
    icon: ImageVector,
    color: Color,
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
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = color.copy(alpha = 0.2f),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun WildernessStateCard(
    state: WildernessState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = CategoryExplore.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, CategoryExplore.copy(alpha = 0.4f)),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Place,
                    contentDescription = null,
                    tint = CategoryExplore,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$state.typeRow / $state.environmentRow",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Parchment,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Sepia.copy(alpha = 0.1f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Cloud,
                            contentDescription = null,
                            tint = ParchmentDark,
                            modifier = Modifier.size(10.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "1d6@${state.environmentRow}+${state.typeRow}",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = ParchmentDark,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (state.isLost) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Danger.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Danger.copy(alpha = 0.5f)),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Danger,
                                modifier = Modifier.size(10.dp),
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "LOST",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Danger,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WildernessPrimaryAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
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
private fun WildernessCompactButton(
    title: String,
    icon: ImageVector,
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
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}

@Composable
private fun ModifierChip(
    label: String,
    subtitle: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else Sepia.copy(alpha = 0.08f),
        border = BorderStroke(
            1.dp,
            if (isSelected) color.copy(alpha = 0.5f) else Sepia.copy(alpha = 0.2f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isSelected) color else ParchmentDark,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) color else Parchment,
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
private fun WildernessEnvironmentPicker(
    selectedEnvironment: Int,
    selectedType: Int,
    onEnvironmentChanged: (Int) -> Unit,
    onTypeChanged: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Sepia.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Sepia.copy(alpha = 0.25f)),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // ── Environment Dropdown ──
            val envNames = WildernessData.wildernessEnvironments
            var showEnvMenu by remember { mutableStateOf(false) }
            val envIndex = (selectedEnvironment - 1).coerceIn(0, 9)

            Surface(
                onClick = { showEnvMenu = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = CategoryExplore.copy(alpha = 0.05f),
                border = BorderStroke(1.dp, Sepia.copy(alpha = 0.3f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Environment",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentDark,
                        )
                        Text(
                            text = "${selectedEnvironment}. ${envNames[envIndex]}",
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

            DropdownMenu(
                expanded = showEnvMenu,
                onDismissRequest = { showEnvMenu = false },
            ) {
                envNames.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${index + 1}. ",
                                    style = MaterialTheme.typography.bodySmall,
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
                            }
                        },
                        onClick = {
                            onEnvironmentChanged(index + 1)
                            showEnvMenu = false
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Type Dropdown ──
            val typeNames = WildernessData.wildernessTypes.map { it["name"] as String }
            var showTypeMenu by remember { mutableStateOf(false) }
            val typeIndex = (selectedType - 1).coerceIn(0, 9)

            Surface(
                onClick = { showTypeMenu = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = CategoryExplore.copy(alpha = 0.05f),
                border = BorderStroke(1.dp, Sepia.copy(alpha = 0.3f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Type",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentDark,
                        )
                        Text(
                            text = "${selectedType}. ${typeNames[typeIndex]}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Parchment,
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Select type",
                        tint = Parchment,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            DropdownMenu(
                expanded = showTypeMenu,
                onDismissRequest = { showTypeMenu = false },
            ) {
                typeNames.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${index + 1}. ",
                                    style = MaterialTheme.typography.bodySmall,
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
                            }
                        },
                        onClick = {
                            onTypeChanged(index + 1)
                            showTypeMenu = false
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Preview
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = CategoryExplore.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, CategoryExplore.copy(alpha = 0.4f)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        tint = CategoryExplore,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${typeNames[typeIndex]} ${envNames[envIndex]}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Parchment,
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Confirm button
            Surface(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = CategoryExplore,
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF1C1814),
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Set Position",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1814),
                    )
                }
            }
        }
    }
}
