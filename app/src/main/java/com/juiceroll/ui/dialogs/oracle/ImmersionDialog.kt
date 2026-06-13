package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.flavor.ImmersionGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
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
 * Dialog for Immersion — sensory details and emotional atmosphere.
 *
 * Provides:
 * - Full Immersion: complete sensory + emotional experience
 * - Sensory Detail: sense + detail + where
 * - Emotional Atmosphere: emotion + cause
 */
@Composable
fun ImmersionDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    immersionGenerator: ImmersionGenerator = remember { ImmersionGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    val fullImmersionColor = Gold
    val sensoryColor = Info
    val emotionalColor = Mystic

    OracleDialog(
        title = "Immersion",
        icon = Icons.Filled.SelfImprovement,
        accentColor = JuiceOrange,
        onDismissRequest = onDismiss,
    ) {
        // ── Introduction ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = JuiceOrange.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, JuiceOrange.copy(alpha = 0.2f)),
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = null,
                    tint = JuiceOrange,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "See what they see, feel what they feel. "
                            + "Perfect when you're \"stuck\" \u2014 provides hints about the environment.",
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ════════════════════════════════════════════════════════════
        // FULL IMMERSION
        // ════════════════════════════════════════════════════════════
        ImmersionSection(
            icon = Icons.Filled.AutoAwesome,
            title = "Full Immersion",
            color = fullImmersionColor,
            highlighted = true,
        ) {
            // Complete badge
            Surface(shape = RoundedCornerShape(4.dp), color = fullImmersionColor.copy(alpha = 0.3f)) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = fullImmersionColor,
                        modifier = Modifier.size(10.dp),
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "COMPLETE",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = fullImmersionColor,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Output format quote
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = InkDark.copy(alpha = 0.3f),
                border = BorderStroke(1.dp, fullImmersionColor.copy(alpha = 0.6f)),
            ) {
                Text(
                    text = "\"You [sense] something [detail] [where], "
                            + "and it causes [emotion] because [cause]\"",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Roll button
            ImmersionRollButton(
                label = "Full Immersion",
                subtitle = "5d10 + 1dF \u2192 Complete sensory experience",
                icon = Icons.Filled.AutoAwesome,
                color = fullImmersionColor,
                isPrimary = true,
                onClick = {
                    currentResult = immersionGenerator.generateFullImmersion()
                },
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ════════════════════════════════════════════════════════════
        // SENSORY DETAIL
        // ════════════════════════════════════════════════════════════
        ImmersionSection(
            icon = Icons.Filled.Visibility,
            title = "Sensory Detail",
            color = sensoryColor,
            highlighted = false,
        ) {
            // Reference info
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = InkDark.copy(alpha = 0.3f),
            ) {
                Column(modifier = Modifier.padding(6.dp)) {
                    DiceReference(
                        die = "d10", label = "Sense",
                        values = "See (1-3), Hear (4-6), Smell (7-8), Feel (9-0)",
                        color = sensoryColor,
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    DiceReference(
                        die = "d10", label = "Detail",
                        values = "Based on sense (Broken, Colorful, Shiny...)",
                        color = sensoryColor,
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    DiceReference(
                        die = "d10", label = "Where",
                        values = "Above, Behind, In The Distance, Next To You...",
                        color = sensoryColor,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ImmersionRollButton(
                label = "Sensory Detail",
                subtitle = "3d10 \u2192 \"You [sense] something [detail] [where]\"",
                icon = Icons.Filled.Visibility,
                color = sensoryColor,
                onClick = {
                    currentResult = immersionGenerator.generateSensoryDetail()
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ImmersionSkewButton(
                    label = "Closer",
                    subtitle = "Near you",
                    icon = Icons.Filled.NearMe,
                    color = Success,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        currentResult = immersionGenerator.generateSensoryDetail(skew = "advantage")
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                ImmersionSkewButton(
                    label = "Further",
                    subtitle = "Far away",
                    icon = Icons.Filled.Explore,
                    color = Info,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        currentResult = immersionGenerator.generateSensoryDetail(skew = "disadvantage")
                    },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ImmersionRollButton(
                label = "Distant Senses Only",
                subtitle = "d6 \u2192 See or Hear only (exploration/scouting)",
                icon = Icons.Filled.RemoveRedEye,
                color = sensoryColor.copy(alpha = 0.7f),
                onClick = {
                    currentResult = immersionGenerator.generateSensoryDetail(senseDie = 6)
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ════════════════════════════════════════════════════════════
        // EMOTIONAL ATMOSPHERE
        // ════════════════════════════════════════════════════════════
        ImmersionSection(
            icon = Icons.Filled.Mood,
            title = "Emotional Atmosphere",
            color = emotionalColor,
            highlighted = false,
        ) {
            // Reference info
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                color = InkDark.copy(alpha = 0.3f),
            ) {
                Column(modifier = Modifier.padding(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SmallDieBadge(die = "1dF", color = emotionalColor)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "polarity: (\u2212/blank) negative, (+) positive",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentDark,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Emotions paired as opposites: Despair\u2194Hope, "
                                + "Fear\u2194Courage, Anger\u2194Calm...",
                        style = MaterialTheme.typography.bodySmall,
                        color = ParchmentDark,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Basic 6: Joy, Sadness, Fear, Anger, Disgust, Surprise",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = Parchment.copy(alpha = 0.85f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            ImmersionRollButton(
                label = "Emotional Atmosphere",
                subtitle = "2d10 + 1dF \u2192 \"It causes [emotion] because [cause]\"",
                icon = Icons.Filled.Mood,
                color = emotionalColor,
                onClick = {
                    currentResult = immersionGenerator.generateEmotionalAtmosphere()
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ImmersionSkewButton(
                    label = "Positive",
                    subtitle = "Hopeful",
                    icon = Icons.Filled.SentimentSatisfiedAlt,
                    color = Success,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        currentResult = immersionGenerator.generateEmotionalAtmosphere(skew = "advantage")
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                ImmersionSkewButton(
                    label = "Negative",
                    subtitle = "Darker",
                    icon = Icons.Filled.SentimentDissatisfied,
                    color = Danger,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        currentResult = immersionGenerator.generateEmotionalAtmosphere(skew = "disadvantage")
                    },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ImmersionRollButton(
                label = "Basic Emotions Only",
                subtitle = "d6 \u2192 Joy, Sadness, Fear, Anger, Disgust, Surprise",
                icon = Icons.Filled.Star, // Placeholder since EmojiEmotionsOutlined doesn't exist
                color = emotionalColor.copy(alpha = 0.7f),
                onClick = {
                    currentResult = immersionGenerator.generateEmotionalAtmosphere(emotionDie = 6)
                },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Example ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Mystic.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Mystic.copy(alpha = 0.25f)),
        ) {
            Text(
                text = "\"You see something discarded behind you, "
                        + "and it causes joy because you were warned about it\"",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = Parchment.copy(alpha = 0.85f),
            )
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
private fun ImmersionSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    color: Color,
    highlighted: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = if (highlighted) color.copy(alpha = 0.1f) else Color.Transparent,
        border = BorderStroke(
            width = if (highlighted) 1.5.dp else 1.dp,
            color = color.copy(alpha = if (highlighted) 0.5f else 0.25f),
        ),
    ) {
        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 10.dp)) {
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
                    color = color,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun DiceReference(
    die: String,
    label: String,
    values: String,
    color: Color,
) {
    Row(
        verticalAlignment = Alignment.Top,
    ) {
        SmallDieBadge(die = die, color = color)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label \u2192 ",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = values,
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
    }
}

@Composable
private fun SmallDieBadge(
    die: String,
    color: Color,
) {
    Surface(
        shape = RoundedCornerShape(3.dp),
        color = color.copy(alpha = 0.2f),
    ) {
        Text(
            text = die,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    }
}

@Composable
private fun ImmersionRollButton(
    label: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isPrimary: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (isPrimary) color.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(
            width = if (isPrimary) 2.dp else 1.dp,
            color = color.copy(alpha = if (isPrimary) 0.6f else 0.3f),
        ),
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
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
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
private fun ImmersionSkewButton(
    label: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = color.copy(alpha = 0.9f),
                )
            }
        }
    }
}
