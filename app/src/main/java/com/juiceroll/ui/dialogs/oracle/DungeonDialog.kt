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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.MotionPhotosPaused
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.world.DungeonGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
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
import com.juiceroll.ui.theme.Surface as ThemeSurface

/**
 * Dialog for Dungeon Generator options.
 * Manages dungeon exploration state synced with the ViewModel/HomeState.
 *
 * Sections:
 * - Entering: Dungeon entrance, name generation, first area
 * - Exploring: Generate next area, passage, condition
 * - Two-Pass Mode: Toggle + dual generation
 * - Encounter: Monster, trap, feature, natural hazard
 * - Navigation: Area history, progress tracking
 */
@Composable
fun DungeonDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    isEntering: Boolean = true,
    onPhaseChange: (Boolean) -> Unit = {},
    isTwoPassMode: Boolean = false,
    onTwoPassModeChange: (Boolean) -> Unit = {},
    twoPassHasFirstDoubles: Boolean = false,
    onTwoPassFirstDoublesChange: (Boolean) -> Unit = {},
    generator: DungeonGenerator = remember { DungeonGenerator() },
) {
    // ── Synced state (initialized from props, synced back via callbacks) ──
    var localIsEntering by remember { mutableStateOf(isEntering) }
    var localIsTwoPassMode by remember { mutableStateOf(isTwoPassMode) }
    var localTwoPassHasFirstDoubles by remember { mutableStateOf(twoPassHasFirstDoubles) }

    // Keep local state in sync when props change (dialog may be re-shown with different values)
    LaunchedEffect(isEntering) { localIsEntering = isEntering }
    LaunchedEffect(isTwoPassMode) { localIsTwoPassMode = isTwoPassMode }
    LaunchedEffect(twoPassHasFirstDoubles) { localTwoPassHasFirstDoubles = twoPassHasFirstDoubles }

    // ── Internal local state ──
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    // Passage/Condition settings
    var useD6ForPassage by remember { mutableStateOf(false) }
    var passageConditionSkew by remember { mutableStateOf(SkewType.NONE) }

    // Encounter settings
    var isLingering by remember { mutableStateOf(false) }
    var encounterSkew by remember { mutableStateOf(SkewType.NONE) }

    // ── Theme colors ──
    val dungeonColor = CategoryExplore
    val phaseEnteringColor = Rust
    val phaseExploringColor = Success
    val encounterColor = Danger
    val trapColor = JuiceOrange

    val scrollState = rememberScrollState()

    // ── Helpers ──
    fun setPhase(entering: Boolean) {
        localIsEntering = entering
        onPhaseChange(entering)
    }

    fun setTwoPassMode(twoPass: Boolean) {
        localIsTwoPassMode = twoPass
        onTwoPassModeChange(twoPass)
    }

    fun setTwoPassFirstDoubles(hasFirstDoubles: Boolean) {
        localTwoPassHasFirstDoubles = hasFirstDoubles
        onTwoPassFirstDoublesChange(hasFirstDoubles)
    }

    fun resetMap() {
        if (localIsTwoPassMode) {
            setTwoPassFirstDoubles(false)
        } else {
            setPhase(true)
        }
    }

    fun useAdvantage(): Boolean {
        return if (localIsTwoPassMode) {
            !localTwoPassHasFirstDoubles
        } else {
            !localIsEntering
        }
    }

    fun computeStatusText(): String {
        return if (localIsTwoPassMode) {
            if (localTwoPassHasFirstDoubles) "1d10@- (after 1st doubles)"
            else "1d10@+ (until 1st doubles)"
        } else {
            if (localIsEntering) "1d10@- Entering (until doubles)"
            else "1d10@+ Exploring (after doubles)"
        }
    }

    fun computeStatusColor(): Color {
        return if (localIsTwoPassMode) {
            if (localTwoPassHasFirstDoubles) phaseEnteringColor else phaseExploringColor
        } else {
            if (localIsEntering) phaseEnteringColor else phaseExploringColor
        }
    }

    // ── Dialog ──
    OracleDialog(
        title = "Dungeon Generator",
        icon = Icons.Filled.Explore,
        accentColor = dungeonColor,
        onDismissRequest = onDismiss,
    ) {
        // ════════════════════════════════════════════════════
        // PHASE INDICATOR
        // ════════════════════════════════════════════════════
        PhaseIndicator(
            statusText = computeStatusText(),
            statusColor = computeStatusColor(),
            isTwoPassMode = localIsTwoPassMode,
            isEntering = localIsEntering,
            twoPassHasFirstDoubles = localTwoPassHasFirstDoubles,
            onSetPhase = { setPhase(it) },
            onResetMap = { resetMap() },
        )

        Spacer(modifier = Modifier.height(10.dp))

        // ════════════════════════════════════════════════════
        // DUNGEON NAME
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Dungeon Name",
            icon = Icons.Filled.Home,
            color = dungeonColor,
        )
        Spacer(modifier = Modifier.height(4.dp))
        ActionButton(
            title = "Generate Name (3d10)",
            subtitle = "[Dungeon] of the [Description] [Subject]",
            color = dungeonColor,
            onClick = {
                currentResult = generator.generateName()
            },
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════
        // MAP GENERATION
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Map Generation",
            icon = Icons.Filled.Map,
            color = dungeonColor,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mode Toggle: One-Pass vs Two-Pass
        Row(modifier = Modifier.fillMaxWidth()) {
            ModeChip(
                label = "One-Pass",
                icon = Icons.Filled.Route,
                isSelected = !localIsTwoPassMode,
                color = dungeonColor,
                modifier = Modifier.weight(1f),
                onClick = { setTwoPassMode(false) },
            )
            Spacer(modifier = Modifier.width(8.dp))
            ModeChip(
                label = "Two-Pass",
                icon = Icons.Filled.Layers,
                isSelected = localIsTwoPassMode,
                color = Mystic,
                modifier = Modifier.weight(1f),
                onClick = { setTwoPassMode(true) },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Mode explanation
        InfoBox(
            text = if (localIsTwoPassMode) {
                "Two-Pass: Pre-generate map, then explore\n" +
                        "• Start 1d10@+ → 1st doubles → 1d10@-\n" +
                        "• 2nd doubles → STOP (remaining = dead ends)\n" +
                        "• Roll encounters during exploration phase"
            } else {
                "One-Pass: Explore as you generate\n" +
                        "• Start 1d10@- → doubles → switch to 1d10@+\n" +
                        "• Roll encounters as you enter each room\n" +
                        "• Mimics \"Skyrim\" style: long way in, shortcut out"
            },
            color = if (localIsTwoPassMode) Mystic else dungeonColor,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Next Area
        ActionButton(
            title = "Next Area",
            subtitle = if (localIsTwoPassMode) {
                "Layout only (${if (useAdvantage()) "1d10@+" else "1d10@-"})"
            } else {
                "Area + Passage if applicable"
            },
            color = dungeonColor,
            onClick = {
                if (localIsTwoPassMode) {
                    val result = generator.generateTwoPassArea(
                        hasFirstDoubles = localTwoPassHasFirstDoubles,
                    )
                    currentResult = result

                    // Handle doubles transitions
                    if (result.isSecondDoubles) {
                        // 2nd DOUBLES - stop map generation
                    } else if (result.isDoubles && !localTwoPassHasFirstDoubles) {
                        setTwoPassFirstDoubles(true)
                    }
                } else {
                    val result = generator.generateNextArea(
                        isEntering = localIsEntering,
                    )
                    currentResult = result

                    // Auto-switch phase if doubles while entering
                    if (result.isDoubles && localIsEntering) {
                        setPhase(false)
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Full Area + Condition
        ActionButton(
            title = "Full Area + Condition",
            subtitle = if (localIsTwoPassMode) {
                "Area + Condition (no encounters)"
            } else {
                "Area + Condition + Passage"
            },
            color = dungeonColor,
            onClick = {
                if (localIsTwoPassMode) {
                    val result = generator.generateTwoPassArea(
                        hasFirstDoubles = localTwoPassHasFirstDoubles,
                    )
                    currentResult = result

                    if (result.isSecondDoubles) {
                        // 2nd DOUBLES — stop map generation
                    } else if (result.isDoubles && !localTwoPassHasFirstDoubles) {
                        setTwoPassFirstDoubles(true)
                    }
                } else {
                    currentResult = generator.generateFullArea(
                        isEntering = localIsEntering,
                        isOccupied = !useD6ForPassage,
                        conditionSkew = when (passageConditionSkew) {
                            SkewType.ADVANTAGE -> "advantage"
                            SkewType.DISADVANTAGE -> "disadvantage"
                            else -> "none"
                        },
                    )
                    // FullDungeonAreaResult does not carry doubles info;
                    // use generateNextArea separately if doubles tracking is needed
                }
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Passage
        ActionButton(
            title = "Passage",
            subtitle = "Manual passage roll (${if (useD6ForPassage) "d6" else "d10"}${getSkewLabel(passageConditionSkew)})",
            color = dungeonColor,
            onClick = {
                currentResult = generator.generatePassage(
                    useD6 = useD6ForPassage,
                    skew = when (passageConditionSkew) {
                        SkewType.ADVANTAGE -> "advantage"
                        SkewType.DISADVANTAGE -> "disadvantage"
                        else -> "none"
                    },
                )
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Condition
        ActionButton(
            title = "Condition",
            subtitle = "Room state (${if (useD6ForPassage) "d6" else "d10"}${getSkewLabel(passageConditionSkew)})",
            color = dungeonColor,
            onClick = {
                currentResult = generator.generateCondition(
                    useD6 = useD6ForPassage,
                    skew = when (passageConditionSkew) {
                        SkewType.ADVANTAGE -> "advantage"
                        SkewType.DISADVANTAGE -> "disadvantage"
                        else -> "none"
                    },
                )
            },
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Passage & Condition Settings
        SettingsPanel(
            title = "Passage/Condition Settings",
            hint = "d6 = Linear/Unoccupied  •  d10 = Branching/Occupied\n" +
                    "@- = Smaller/Worse  •  @+ = Larger/Better",
            color = Mystic,
        ) {
            DieSkewRow(
                useD6 = useD6ForPassage,
                onUseD6Change = { useD6ForPassage = it },
                skew = passageConditionSkew,
                onSkewChange = { passageConditionSkew = it },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════
        // DUNGEON ENCOUNTER
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Dungeon Encounter",
            icon = Icons.Filled.WarningAmber,
            color = encounterColor,
        )
        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = encounterColor.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, encounterColor.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "10m 1d6 (NH: d6); Trap: 10m AP@+ A/L, PP L/T",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = encounterColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "d6 = Lingering 10+ min in unsafe area\n" +
                            "d10 = Entering area first time\n" +
                            "@+ = Better, @- = Worse",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(10.dp))
                DieSkewRow(
                    useD6 = isLingering,
                    onUseD6Change = { isLingering = it },
                    skew = encounterSkew,
                    onSkewChange = { encounterSkew = it },
                    d6Label = "d6 Linger",
                    d10Label = "d10 Entry",
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Encounter Type
        ActionButton(
            title = "Encounter Type",
            subtitle = "What do you find? (${if (isLingering) "d6" else "d10"}${getSkewLabel(encounterSkew)})",
            color = encounterColor,
            onClick = {
                currentResult = generator.rollEncounterType(
                    isLingering = isLingering,
                    skew = when (encounterSkew) {
                        SkewType.ADVANTAGE -> "advantage"
                        SkewType.DISADVANTAGE -> "disadvantage"
                        else -> "none"
                    },
                )
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Full Encounter
        ActionButton(
            title = "Full Encounter",
            subtitle = "Type + Monster/Trap/Feature if applicable",
            color = encounterColor,
            onClick = {
                currentResult = generator.rollFullEncounter(
                    isLingering = isLingering,
                    skew = when (encounterSkew) {
                        SkewType.ADVANTAGE -> "advantage"
                        SkewType.DISADVANTAGE -> "disadvantage"
                        else -> "none"
                    },
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════
        // ENCOUNTER DETAILS
        // ════════════════════════════════════════════════════
        SectionLabel(
            title = "Encounter Details",
            icon = Icons.Filled.PestControl,
            color = trapColor,
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Monster
        ActionButton(
            title = "Monster (2d10)",
            subtitle = "Descriptor + Ability",
            color = trapColor,
            onClick = {
                currentResult = generator.rollMonsterDescription()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Trap
        ActionButton(
            title = "Trap (2d10)",
            subtitle = "Action + Subject",
            color = trapColor,
            onClick = {
                currentResult = generator.rollTrap()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Trap Procedure (Searching)
        ActionButton(
            title = "Trap Procedure (Searching)",
            subtitle = "Trap + DC (10 min, @+): Pass=Avoid, Fail=Locate",
            color = trapColor,
            onClick = {
                currentResult = generator.rollTrapProcedure(
                    isSearching = true,
                    dcSkew = "none",
                )
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Trap Procedure (Passive)
        ActionButton(
            title = "Trap Procedure (Passive)",
            subtitle = "Trap + DC (Passive): Pass=Locate, Fail=Trigger",
            color = trapColor,
            onClick = {
                currentResult = generator.rollTrapProcedure(
                    isSearching = false,
                    dcSkew = "none",
                )
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Feature
        ActionButton(
            title = "Feature (1d10)",
            subtitle = "Library, Mural, Mushrooms, Prison...",
            color = trapColor,
            onClick = {
                currentResult = generator.rollFeature()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Natural Hazard
        ActionButton(
            title = "Natural Hazard",
            subtitle = "d10 on entry, d6 when lingering",
            color = trapColor,
            onClick = {
                currentResult = generator.rollNaturalHazard(isLingering = isLingering)
            },
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Sepia.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(8.dp))

        // Trap Procedure Reference
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = trapColor.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, trapColor.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.MotionPhotosPaused,
                        contentDescription = null,
                        tint = trapColor,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Trap Procedure",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = trapColor,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1. BEFORE encounter: decide to search (10 min) or not\n" +
                            "2. If searching: Active Perception @+ vs DC\n" +
                            "   • Pass = AVOID (completely bypass)\n" +
                            "   • Fail = LOCATE (must disarm/bypass)\n" +
                            "3. If NOT searching: Passive Perception vs DC\n" +
                            "   • Pass = LOCATE (must disarm/bypass)\n" +
                            "   • Fail = TRIGGER (suffer consequences)\n\n" +
                            "Note: Lingering >10 min in non-Safety room = roll\n" +
                            "another encounter (d6). Only 1 action per room is \"free\".",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Encounter Reference
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Sepia.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Sepia.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = Sepia,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Encounter Reference",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Sepia,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1: Monster    6: Known\n" +
                            "2: Nat Hazard 7: Trap\n" +
                            "3: Challenge  8: Feature\n" +
                            "4: Immersion  9: Key\n" +
                            "5: Safety     0: Treasure",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
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

// ════════════════════════════════════════════════════════════════
// Enum
// ════════════════════════════════════════════════════════════════

private enum class SkewType {
    NONE,
    ADVANTAGE,
    DISADVANTAGE,
}

// ════════════════════════════════════════════════════════════════
// Private helper functions
// ════════════════════════════════════════════════════════════════

private fun getSkewLabel(skew: SkewType): String = when (skew) {
    SkewType.ADVANTAGE -> "@+"
    SkewType.DISADVANTAGE -> "@-"
    SkewType.NONE -> ""
}

// ════════════════════════════════════════════════════════════════
// Private helper composables
// ════════════════════════════════════════════════════════════════

@Composable
private fun PhaseIndicator(
    statusText: String,
    statusColor: Color,
    isTwoPassMode: Boolean,
    isEntering: Boolean,
    twoPassHasFirstDoubles: Boolean,
    onSetPhase: (Boolean) -> Unit,
    onResetMap: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = statusColor.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = statusColor.copy(alpha = 0.2f),
            ) {
                Icon(
                    imageVector = Icons.Filled.Explore,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.padding(4.dp).size(14.dp),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = statusText,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = statusColor,
            )

            // Phase toggle (One-Pass only)
            if (!isTwoPassMode) {
                CompactPhaseChip(
                    label = "(@-)",
                    isSelected = isEntering,
                    color = Rust,
                    onClick = { onSetPhase(true) },
                )
                Spacer(modifier = Modifier.width(4.dp))
                CompactPhaseChip(
                    label = "(@+)",
                    isSelected = !isEntering,
                    color = Success,
                    onClick = { onSetPhase(false) },
                )
            }

            // Two-Pass doubles indicators
            if (isTwoPassMode) {
                CompactDoublesIndicator(
                    label = "1st",
                    isActive = twoPassHasFirstDoubles,
                    color = Rust,
                )
                Spacer(modifier = Modifier.width(4.dp))
                CompactDoublesIndicator(
                    label = "2nd",
                    isActive = false,
                    color = Danger,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))

            // Reset button
            Surface(
                onClick = onResetMap,
                shape = RoundedCornerShape(4.dp),
                color = statusColor.copy(alpha = 0.1f),
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Reset",
                    tint = statusColor.copy(alpha = 0.7f),
                    modifier = Modifier.padding(4.dp).size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun CompactPhaseChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = if (isSelected) color.copy(alpha = 0.25f) else Color.Transparent,
        border = BorderStroke(1.dp, if (isSelected) color else color.copy(alpha = 0.4f)),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) color else color.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun CompactDoublesIndicator(
    label: String,
    isActive: Boolean,
    color: Color,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (isActive) color.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (isActive) color else Color.Gray.copy(alpha = 0.3f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isActive) Icons.Filled.Check else Icons.Filled.KeyboardArrowUp,
                contentDescription = null,
                tint = if (isActive) color else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(10.dp),
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) color else Color.Gray.copy(alpha = 0.6f),
            )
        }
    }
}

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
private fun ActionButton(
    title: String,
    subtitle: String,
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
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
private fun InfoBox(
    text: String,
    color: Color = CategoryExplore,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = ParchmentDark,
        )
    }
}

@Composable
private fun ModeChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (isSelected) color else Color.Gray.copy(alpha = 0.4f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) color else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) color else Color.Gray.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun DieChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (isSelected) color else Color.Gray.copy(alpha = 0.4f),
        ),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            color = if (isSelected) color else Color.Gray.copy(alpha = 0.5f),
        )
    }
}

@Composable
private fun SkewChip(
    label: String,
    type: SkewType,
    current: SkewType,
    color: Color,
    onClick: (SkewType) -> Unit,
) {
    val isSelected = current == type
    Surface(
        onClick = { onClick(if (isSelected) SkewType.NONE else type) },
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (isSelected) color else Color.Gray.copy(alpha = 0.4f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontFamily = FontFamily.Monospace,
                color = if (isSelected) color else Color.Gray.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun SettingsPanel(
    title: String,
    hint: String,
    color: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f)),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = ParchmentDark,
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun DieSkewRow(
    useD6: Boolean,
    onUseD6Change: (Boolean) -> Unit,
    skew: SkewType,
    onSkewChange: (SkewType) -> Unit,
    d6Label: String = "d6",
    d10Label: String = "d10",
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        DieChip(
            label = d6Label,
            isSelected = useD6,
            color = Info,
            onClick = { onUseD6Change(true) },
        )
        Spacer(modifier = Modifier.width(8.dp))
        DieChip(
            label = d10Label,
            isSelected = !useD6,
            color = Info,
            onClick = { onUseD6Change(false) },
        )
        Spacer(modifier = Modifier.width(16.dp))
        SkewChip(
            label = "@-",
            type = SkewType.DISADVANTAGE,
            current = skew,
            color = Rust,
            onClick = onSkewChange,
        )
        Spacer(modifier = Modifier.width(8.dp))
        SkewChip(
            label = "@+",
            type = SkewType.ADVANTAGE,
            current = skew,
            color = Success,
            onClick = onSkewChange,
        )
    }
}
