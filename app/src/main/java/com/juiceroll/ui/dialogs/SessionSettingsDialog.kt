package com.juiceroll.ui.dialogs

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.juiceroll.data.session.Session
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.JuiceOrange
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Success
import com.juiceroll.ui.theme.Surface

private const val DEFAULT_MAX_ROLLS = 200

/**
 * Dialog for managing session-specific settings.
 *
 * Allows toggling a max roll limit, selecting a preset value,
 * or typing a custom amount. Shows current usage stats.
 */
@Composable
fun SessionSettingsDialog(
    session: Session,
    onUpdate: (maxRollsPerSession: Int?, clearMax: Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    var useCustomMaxRolls by remember { mutableStateOf(session.maxRollsPerSession != null) }
    var maxRollsText by remember {
        mutableStateOf(
            (session.maxRollsPerSession ?: DEFAULT_MAX_ROLLS).toString()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Session Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = Parchment,
                    )
                    Text(
                        text = session.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = ParchmentDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                // History Settings section
                SectionHeaderWithIcon(
                    icon = Icons.Filled.History,
                    title = "History Settings",
                    color = Gold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Gold.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Gold.copy(alpha = 0.2f)),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Toggle row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Max Rolls in History",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Parchment,
                                )
                                Text(
                                    text = "Limit how many rolls are stored",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentDark,
                                )
                            }
                            Switch(
                                checked = useCustomMaxRolls,
                                onCheckedChange = { enabled ->
                                    useCustomMaxRolls = enabled
                                    if (enabled) {
                                        maxRollsText = DEFAULT_MAX_ROLLS.toString()
                                    }
                                },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    checkedThumbColor = Gold,
                                    checkedTrackColor = Gold.copy(alpha = 0.3f),
                                ),
                            )
                        }

                        if (useCustomMaxRolls) {
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = maxRollsText,
                                onValueChange = { maxRollsText = it.filter { c -> c.isDigit() } },
                                label = { Text("Maximum Rolls") },
                                placeholder = { Text("e.g., 1000") },
                                suffix = { Text("rolls", color = ParchmentDark) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(),
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Quick presets
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                listOf("500", "1000", "2000", "5000").forEach { preset ->
                                    val isSelected = maxRollsText == preset
                                    Surface(
                                        onClick = { maxRollsText = preset },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) Gold.copy(alpha = 0.2f) else Color.Transparent,
                                        border = BorderStroke(
                                            1.dp,
                                            if (isSelected) Gold.copy(alpha = 0.5f) else Gold.copy(alpha = 0.3f),
                                        ),
                                        tonalElevation = 0.dp,
                                    ) {
                                        Text(
                                            text = preset,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Gold else Gold.copy(alpha = 0.8f),
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Info.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, Info.copy(alpha = 0.2f)),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Info,
                                        contentDescription = null,
                                        tint = Info,
                                        modifier = Modifier.size(14.dp),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Unlimited history (no limit)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Info,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current Usage section
                SectionHeaderWithIcon(
                    icon = Icons.Filled.Casino,
                    title = "Current Usage",
                    color = Gold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1A1613),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        DetailRow(
                            icon = Icons.Filled.Casino,
                            label = "Rolls in History",
                            value = "${session.rollCount}",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(
                            icon = Icons.Filled.Storage,
                            label = "Current Limit",
                            value = session.maxRollsPerSession?.toString() ?: "Unlimited",
                        )
                        if (session.maxRollsPerSession != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            UsageBar(
                                current = session.rollCount,
                                max = session.maxRollsPerSession!!,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = ParchmentDark)
                }
                Spacer(modifier = Modifier.width(8.dp))
                ElevatedButton(
                    onClick = {
                        val maxRolls = if (useCustomMaxRolls) {
                            maxRollsText.toIntOrNull()?.let { if (it > 0) it else null }
                        } else null
                        onUpdate(maxRolls, !useCustomMaxRolls)
                        onDismiss()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.elevatedButtonColors(
                        containerColor = Gold.copy(alpha = 0.15f),
                        contentColor = Gold,
                    ),
                ) {
                    Text("Save")
                }
            }
        },
        containerColor = Surface,
        titleContentColor = Parchment,
        textContentColor = Parchment.copy(alpha = 0.9f),
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun UsageBar(
    current: Int,
    max: Int,
) {
    val percentage = if (max > 0) (current.toFloat() / max).coerceIn(0f, 1f) else 0f
    val barColor = when {
        percentage > 0.9f -> Danger
        percentage > 0.7f -> JuiceOrange
        else -> Success
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Usage",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
            Text(
                text = "${(percentage * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = barColor,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = barColor,
            trackColor = ParchmentDark.copy(alpha = 0.15f),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}
