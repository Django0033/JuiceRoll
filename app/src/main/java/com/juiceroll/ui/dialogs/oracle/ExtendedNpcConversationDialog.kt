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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.character.ExtendedNpcConversationGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.CategoryCharacter
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.InkDark
import com.juiceroll.ui.theme.JuiceOrange
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Success

/**
 * Dialog for Extended NPC Conversation tables.
 * An alternative to the Dialog Grid mini-game for NPC conversations.
 *
 * Provides:
 * - Information (2d100): Type of Information + Topic of Information
 * - Companion Response (1d100): Ordered responses with attitude skew
 * - Dialog Topic (1d100): What NPCs are discussing
 */
@Composable
fun ExtendedNpcConversationDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    conversationGenerator: ExtendedNpcConversationGenerator =
        remember { ExtendedNpcConversationGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }
    var companionSkew by remember { mutableStateOf("none") }

    val npcColor = CategoryCharacter
    val infoColor = Info
    val companionColor = Success
    val topicColor = JuiceOrange
    val opposedColor = Danger
    val favorColor = Success

    fun getSkewLabel(): String = when (companionSkew) {
        "advantage" -> " @+ In Favor"
        "disadvantage" -> " @- Opposed"
        else -> ""
    }

    OracleDialog(
        title = "Extended NPC Conversation",
        icon = Icons.Filled.RecordVoiceOver,
        accentColor = npcColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Header explanation ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = npcColor.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, npcColor.copy(alpha = 0.25f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                    imageVector = Icons.AutoMirrored.Filled.Chat,
                    contentDescription = null,
                    tint = npcColor,
                    modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Plot Knowledge \u2022 Companion Responses \u2022 Dialog Topics",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = npcColor,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Alternative to the Dialog Grid mini-game. "
                            + "NPCs make the world feel alive!",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // INFORMATION SECTION (2d100)
        // ════════════════════════════════════════════════════════════
        NpcSectionHeader(title = "Information", icon = Icons.Filled.Info, color = infoColor)
        NpcInfoBox(
            text = "Roll 2d100 to determine what an NPC is talking about. "
                    + "Could be a response to asking for info, or something overheard.",
            color = infoColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        NpcRollButton(
            title = "Roll Information",
            subtitle = "Type + Topic (2d100)",
            icon = Icons.AutoMirrored.Filled.LibraryBooks,
            color = infoColor,
            onClick = {
                currentResult = conversationGenerator.rollInformation()
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ════════════════════════════════════════════════════════════
        // COMPANION RESPONSE SECTION (1d100)
        // ════════════════════════════════════════════════════════════
        NpcSectionHeader(title = "Companion Response", icon = Icons.Filled.Groups, color = companionColor)
        NpcInfoBox(
            text = "Responses to \"the plan\". Ordered such that bigger numbers "
                    + "are more in favor, smaller numbers are more opposed.",
            color = companionColor,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Skew selection
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = InkDark.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = null,
                        tint = ParchmentDark,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Attitude Bias",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ParchmentDark,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    NpcSkewChip(
                        label = "None",
                        type = "none",
                        icon = Icons.Filled.HorizontalRule,
                        color = ParchmentDark,
                        selected = companionSkew == "none",
                        modifier = Modifier.weight(1f),
                        onClick = { companionSkew = "none" },
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    NpcSkewChip(
                        label = "@- Opposed",
                        type = "disadvantage",
                        icon = Icons.Filled.ThumbDown,
                        color = opposedColor,
                        selected = companionSkew == "disadvantage",
                        modifier = Modifier.weight(1f),
                        onClick = { companionSkew = "disadvantage" },
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    NpcSkewChip(
                        label = "@+ In Favor",
                        type = "advantage",
                        icon = Icons.Filled.ThumbUp,
                        color = favorColor,
                        selected = companionSkew == "advantage",
                        modifier = Modifier.weight(1f),
                        onClick = { companionSkew = "advantage" },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        NpcRollButton(
            title = "Roll Companion Response",
            subtitle = "1d100${getSkewLabel()}",
            icon = Icons.AutoMirrored.Filled.Chat,
            color = companionColor,
            onClick = {
                currentResult = conversationGenerator.rollCompanionResponse(skew = companionSkew)
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ════════════════════════════════════════════════════════════
        // DIALOG TOPIC SECTION (1d100)
        // ════════════════════════════════════════════════════════════
        NpcSectionHeader(title = "Dialog Topic", icon = Icons.Filled.Forum, color = topicColor)
        NpcInfoBox(
            text = "What are NPCs talking about? More topics than the standard table. "
                    + "Also usable for News, letters, books, writing on walls, etc.",
            color = topicColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        NpcRollButton(
            title = "Roll Dialog Topic",
            subtitle = "What NPCs are discussing (1d100)",
            icon = Icons.Filled.Forum,
            color = topicColor,
            onClick = {
                currentResult = conversationGenerator.rollDialogTopic()
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ════════════════════════════════════════════════════════════
        // REFERENCE: RESPONSE FAVOR LEVELS
        // ════════════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = InkDark.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, companionColor.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.SentimentSatisfiedAlt,
                        contentDescription = null,
                        tint = companionColor,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Response Favor Levels",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = companionColor,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                FavorLevelRow(range = "1-20", label = "Strongly Opposed", color = Danger)
                FavorLevelRow(range = "21-40", label = "Hesitant", color = JuiceOrange)
                FavorLevelRow(range = "41-60", label = "Neutral / Questioning", color = ParchmentDark)
                FavorLevelRow(range = "61-80", label = "Cautious Support", color = Info)
                FavorLevelRow(range = "81-100", label = "Strongly In Favor", color = Success)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // TIP: DIALOG GRID
        // ════════════════════════════════════════════════════════════
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Mystic.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.25f)),
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = Mystic,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tip: Use the Dialog Grid (Dialog button) for a more interactive "
                            + "mini-game experience with position tracking.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Parchment.copy(alpha = 0.85f),
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

// ══════════════════════════════════════════════════════════════════
// Private helper composables
// ══════════════════════════════════════════════════════════════════

@Composable
private fun NpcSectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
) {
    Row(
        modifier = Modifier.padding(bottom = 6.dp, top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = color.copy(alpha = 0.15f),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(4.dp).size(14.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}

@Composable
private fun NpcInfoBox(
    text: String,
    color: Color,
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
            color = Parchment.copy(alpha = 0.85f),
        )
    }
}

@Composable
private fun NpcSkewChip(
    label: String,
    type: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) color.copy(alpha = 0.15f) else Color.Transparent,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) color else ParchmentDark.copy(alpha = 0.4f),
        ),
    ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) color else ParchmentDark.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) color else ParchmentDark.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun NpcRollButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
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
            Spacer(modifier = Modifier.width(12.dp))
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
                    fontFamily = FontFamily.Monospace,
                    color = ParchmentDark,
                )
            }
        }
    }
}

@Composable
private fun FavorLevelRow(
    range: String,
    label: String,
    color: Color,
) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = color.copy(alpha = 0.15f),
        ) {
            Text(
                text = range,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = color,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Parchment.copy(alpha = 0.9f),
        )
    }
}
