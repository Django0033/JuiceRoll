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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.character.NpcActionGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.theme.CategoryCharacter
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success

/**
 * Dialog for NPC Action generation.
 *
 * Provides NPC Creation (personality, need, motive),
 * Individual Rolls, Action Tables, and Combat Tables.
 */
@Composable
fun NpcActionDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    npcActionGenerator: NpcActionGenerator = remember { NpcActionGenerator() },
) {
    var disposition by remember { mutableStateOf("active") }
    var context by remember { mutableStateOf("active") }
    var focus by remember { mutableStateOf("active") }
    var objective by remember { mutableStateOf("offensive") }
    var needSkew by remember { mutableStateOf("none") }

    val accentColor = CategoryCharacter

    OracleDialog(
        title = "NPC",
        icon = Icons.Filled.Person,
        accentColor = accentColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Header ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = accentColor.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Disp: d10A/6P  •  Ctx: @+A/-P",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = Parchment,
                )
                Text(
                    text = "WH: \u0394Ctx  •  SH: \u0394Ctx & +/-1",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = ParchmentDark,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // NPC CREATION
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "NPC Creation")
        Spacer(modifier = Modifier.height(4.dp))

        // Need Skew selector
        NeedSkewSelector(
            selected = needSkew,
            onSelect = { needSkew = it },
        )
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Mystic.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.2f)),
        ) {
            Text(
                text = "Complex NPCs (sidekicks, important characters):\n"
                        + "Name + 2 Personalities + Need + Motive + Color + 2 Properties.\n"
                        + "Use @+ for people, @- for monsters.",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        NpcOption(
            title = "\u2B50 Complex NPC (Person)",
            subtitle = "Name + 2 Personalities + Need@+ + Motive + Color + Properties",
            onClick = {
                onRoll(npcActionGenerator.generateComplexNpc(
                    needSkew = "complex", includeName = true, dualPersonality = true,
                ))
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NpcOption(
            title = "\u2B50 Complex NPC (Monster)",
            subtitle = "Name + 2 Personalities + Need@- + Motive + Color + Properties",
            onClick = {
                onRoll(npcActionGenerator.generateComplexNpc(
                    needSkew = "primitive", includeName = true, dualPersonality = true,
                ))
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NpcOption(
            title = "Profile Only (No Name)",
            subtitle = "2 Personalities + Need + Motive + Color + Properties",
            onClick = {
                onRoll(npcActionGenerator.generateProfile(needSkew = needSkew))
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // INDIVIDUAL ROLLS
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Individual Rolls")
        Spacer(modifier = Modifier.height(6.dp))

        NpcOption(
            title = "Personality",
            subtitle = "d10 - Roll 2 for primary/secondary traits",
            onClick = {
                onRoll(npcActionGenerator.rollPersonality())
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NpcOption(
            title = "Dual Personality",
            subtitle = """2d10 - "Primary, yet Secondary"""",
            onClick = {
                onRoll(npcActionGenerator.rollDualPersonality())
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NpcOption(
            title = "Need",
            subtitle = "d10" + when (needSkew) {
                "complex" -> " @+ Complex"
                "primitive" -> " @- Primitive"
                else -> ""
            },
            onClick = {
                onRoll(npcActionGenerator.rollNeed(skew = needSkew))
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NpcOption(
            title = "Motive / Topic",
            subtitle = "d10 - Auto-rolls History/Focus tables",
            onClick = {
                onRoll(npcActionGenerator.rollMotiveWithFollowUp())
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // ACTION TABLE
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Action Table")
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Rust.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Rust.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Disposition (static): Passive=d6, Active=d10\n"
                            + "Context (changeable): Active=@+, Passive=@-",
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Disposition chips
                Row {
                    ToggleChip(
                        label = "Passive (d6)",
                        selected = disposition == "passive",
                        onClick = { disposition = "passive" },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ToggleChip(
                        label = "Active (d10)",
                        selected = disposition == "active",
                        onClick = { disposition = "active" },
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Context chips
                Row {
                    ToggleChip(
                        label = "Passive (@-)",
                        selected = context == "passive",
                        onClick = { context = "passive" },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ToggleChip(
                        label = "Active (@+)",
                        selected = context == "active",
                        onClick = { context = "active" },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        NpcOption(
            title = "Roll Action",
            subtitle = "${if (disposition == "passive") "d6" else "d10"}"
                    + "${if (context == "active") "@+" else "@-"}"
                    + " - $disposition / $context",
            onClick = {
                onRoll(npcActionGenerator.rollAction(
                    disposition = disposition, context = context,
                ))
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ══════════════════════════════════════════════════════
        // COMBAT TABLE
        // ══════════════════════════════════════════════════════
        SectionHeader(title = "Combat Table")
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = Danger.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Danger.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "Focus: Passive=d6 (warnings), Active=d10 (full combat)\n"
                            + "Objective: Defensive=@-, Offensive=@+",
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    ToggleChip(
                        label = "Passive (d6)",
                        selected = focus == "passive",
                        onClick = { focus = "passive" },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ToggleChip(
                        label = "Active (d10)",
                        selected = focus == "active",
                        onClick = { focus = "active" },
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    ToggleChip(
                        label = "Defensive (@-)",
                        selected = objective == "defensive",
                        onClick = { objective = "defensive" },
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ToggleChip(
                        label = "Offensive (@+)",
                        selected = objective == "offensive",
                        onClick = { objective = "offensive" },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        NpcOption(
            title = "Roll Combat",
            subtitle = "${if (focus == "passive") "d6" else "d10"}"
                    + "${if (objective == "offensive") "@+" else "@-"}"
                    + " - $focus / $objective",
            onClick = {
                onRoll(npcActionGenerator.rollCombatAction(
                    focus = focus, objective = objective,
                ))
                onDismiss()
            },
        )

    }
}

// ═══════════════════════════════════════════════════════════════
// Private helper composables
// ═══════════════════════════════════════════════════════════════

@Composable
private fun NpcOption(
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
private fun ToggleChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val chipColor = when {
        label.contains("Passive") || label.contains("Defensive") -> Rust
        else -> Info
    }
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (selected) chipColor.copy(alpha = 0.25f) else chipColor.copy(alpha = 0.08f),
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) chipColor.copy(alpha = 0.6f) else chipColor.copy(alpha = 0.3f),
        ),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) chipColor else Parchment,
        )
    }
}

@Composable
private fun NeedSkewSelector(
    selected: String,
    onSelect: (String) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        color = Info.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Info.copy(alpha = 0.25f)),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Need Skew (for people use @+, for monsters use @-)",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                NeedChip("None", selected == "none") { onSelect("none") }
                Spacer(modifier = Modifier.width(6.dp))
                NeedChip("@- Primitive", selected == "primitive") { onSelect("primitive") }
                Spacer(modifier = Modifier.width(6.dp))
                NeedChip("@+ Complex", selected == "complex") { onSelect("complex") }
            }
        }
    }
}

@Composable
private fun NeedChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = if (selected) Info.copy(alpha = 0.25f) else Info.copy(alpha = 0.06f),
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) Info.copy(alpha = 0.6f) else Info.copy(alpha = 0.25f),
        ),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Info else Parchment,
        )
    }
}
