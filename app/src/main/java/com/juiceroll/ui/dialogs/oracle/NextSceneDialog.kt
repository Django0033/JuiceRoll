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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.TableChart
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.oracle.InterruptPlotPointGenerator
import com.juiceroll.generator.oracle.NextSceneGenerator
import com.juiceroll.generator.oracle.RandomEventGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Success

/**
 * Dialog for determining the next scene.
 *
 * At the end of a scene you probably have an idea of what the next scene may look like.
 * This dialog challenges that expectation with random alterations or interruptions.
 */
@Composable
fun NextSceneDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    nextSceneGenerator: NextSceneGenerator = remember { NextSceneGenerator() },
    randomEventGenerator: RandomEventGenerator = remember { RandomEventGenerator() },
    interruptPlotPointGenerator: InterruptPlotPointGenerator = remember { InterruptPlotPointGenerator() },
) {
    var useSimpleMode by remember { mutableStateOf(false) }

    OracleDialog(
        title = "Next Scene",
        icon = Icons.Filled.Movie,
        accentColor = Gold,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Info.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, Info.copy(alpha = 0.2f)),
        ) {
            Text(
                text = "Challenge your expected next scene. Roll 2dF to see if the "
                        + "scene proceeds normally, is altered, or is interrupted.",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = Parchment.copy(alpha = 0.85f),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Dice Outcome Reference ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Gold.copy(alpha = 0.05f),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.2f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Casino,
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "2dF Scene Transitions",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Gold,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutcomeRow(symbols = "+ +", label = "Alter (Add)", color = Success)
                OutcomeRow(symbols = "+ \u2212", label = "Alter (Remove)", color = Info)
                OutcomeRow(symbols = "\u2212 +", label = "Interrupt (+)", color = Info)
                OutcomeRow(symbols = "\u2212 \u2212", label = "Interrupt (\u2212)", color = Danger)
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = ParchmentDark.copy(alpha = 0.15f),
                ) {
                    Text(
                        text = "Any \u25CB = Normal (proceeds as expected)",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        color = ParchmentDark,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Simple Mode Toggle ──
        Surface(
            onClick = { useSimpleMode = !useSimpleMode },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            color = if (useSimpleMode) Mystic.copy(alpha = 0.1f) else ParchmentDark.copy(alpha = 0.06f),
            border = BorderStroke(
                width = 1.dp,
                color = if (useSimpleMode) Mystic.copy(alpha = 0.4f) else ParchmentDark.copy(alpha = 0.2f),
            ),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = if (useSimpleMode) Mystic else ParchmentDark.copy(alpha = 0.15f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (useSimpleMode) Mystic else ParchmentDark,
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
                    text = "Simple Mode: Use Modifier + Idea instead of Focus for Alter",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (useSimpleMode) Mystic else ParchmentDark,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ════════════════════════════════════════════════════════════
        // ROLL SCENE
        // ════════════════════════════════════════════════════════════
        SectionHeader(title = "Roll Scene Transition", icon = Icons.Filled.Movie)
        Spacer(modifier = Modifier.height(4.dp))

        NextSceneOption(
            title = "Quick Roll (2dF)",
            subtitle = "Scene type only, roll follow-up manually",
            icon = Icons.Filled.Bolt,
            iconColor = Gold,
            highlighted = false,
            onClick = {
                onRoll(nextSceneGenerator.determineScene())
                onDismiss()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        NextSceneOption(
            title = "Full Roll (Auto)",
            subtitle = if (useSimpleMode)
                "Auto-rolls Modifier+Idea (Alter) or Plot Point (Interrupt)"
            else
                "Auto-rolls Focus (Alter) or Plot Point (Interrupt)",
            icon = Icons.Filled.AutoAwesome,
            iconColor = Mystic,
            highlighted = true,
            onClick = {
                val sceneResult = nextSceneGenerator.determineScene()
                val sceneType = sceneResult.sceneType
                val result = when {
                    sceneType == "alterAdd" || sceneType == "alterRemove" -> {
                        if (useSimpleMode) {
                            randomEventGenerator.generateIdea()
                        } else {
                            nextSceneGenerator.rollFocus()
                        }
                    }
                    sceneType == "interruptFavorable" || sceneType == "interruptUnfavorable" -> {
                        interruptPlotPointGenerator.generate()
                    }
                    else -> sceneResult
                }
                onRoll(result)
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // FOLLOW-UP TABLES
        // ════════════════════════════════════════════════════════════
        SectionHeader(title = "Follow-up Tables", icon = Icons.Filled.TableChart)
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FollowUpOption(
                title = "Focus",
                subtitle = "d10",
                icon = Icons.Filled.CenterFocusStrong,
                iconColor = Info,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(nextSceneGenerator.rollFocus())
                    onDismiss()
                },
            )
            Spacer(modifier = Modifier.width(6.dp))
            FollowUpOption(
                title = "Mod + Idea",
                subtitle = "2d10",
                icon = Icons.Filled.Lightbulb,
                iconColor = Mystic,
                modifier = Modifier.weight(1f),
                onClick = {
                    onRoll(randomEventGenerator.generateIdea())
                    onDismiss()
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Examples ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = ParchmentDark.copy(alpha = 0.06f),
            border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.15f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = ParchmentDark,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Example: PC rents a room, expects to wake in morning",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ParchmentDark,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                ExampleRow(type = "Normal", typeColor = ParchmentDark, result = "Wake up as expected.")
                ExampleRow(type = "Alter+", typeColor = Success, result = "\"Ally\" \u2192 Friend knocks on door.")
                ExampleRow(type = "Alter\u2212", typeColor = Info, result = "\"Arctic\" \u2192 Hot, stalls closed.")
                ExampleRow(type = "Int (+)", typeColor = Info, result = "\"Reinforcements\" \u2192 Sheriff catches thief.")
                ExampleRow(type = "Int (\u2212)", typeColor = Danger, result = "\"Battle\" \u2192 Assassin visits!")
            }
        }

    }
}

// ══════════════════════════════════════════════════════════════════
// Private helper composables
// ══════════════════════════════════════════════════════════════════

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Gold,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Gold,
        )
    }
}

@Composable
private fun OutcomeRow(
    symbols: String,
    label: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(3.dp),
            color = color.copy(alpha = 0.15f),
        ) {
            Text(
                text = symbols,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = color,
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color,
        )
    }
}

@Composable
private fun NextSceneOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    highlighted: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (highlighted) iconColor.copy(alpha = 0.1f) else Gold.copy(alpha = 0.08f),
        border = BorderStroke(
            width = if (highlighted) 2.dp else 1.dp,
            color = if (highlighted) iconColor.copy(alpha = 0.5f) else Gold.copy(alpha = 0.3f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (highlighted) iconColor else Gold,
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
private fun FollowUpOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = iconColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, iconColor.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = iconColor,
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
private fun ExampleRow(
    type: String,
    typeColor: androidx.compose.ui.graphics.Color,
    result: String,
) {
    Row(
        modifier = Modifier.padding(vertical = 1.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = type,
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = typeColor,
        )
        Text(
            text = "\u2192 ",
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = result,
            style = MaterialTheme.typography.bodySmall,
            color = Parchment.copy(alpha = 0.85f),
        )
    }
}
