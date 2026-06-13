package com.juiceroll.ui.dialogs

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juiceroll.data.session.Session
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Surface

/**
 * Dialog for viewing and managing session details.
 *
 * Shows session stats, editable name and notes, and actions
 * for delete, export, and opening session settings.
 */
@Composable
fun SessionDetailsDialog(
    session: Session,
    isCurrentSession: Boolean,
    onDelete: () -> Unit,
    onExport: () -> Unit,
    onUpdate: (name: String, notes: String) -> Unit,
    onShowSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(session.name) }
    var editedNotes by remember { mutableStateOf(session.notes ?: "") }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Session?", color = Parchment) },
            text = {
                Text(
                    text = if (isCurrentSession) {
                        "This is your current session. Deleting it will create a new empty session. " +
                            "Are you sure you want to delete \"${session.name}\" with ${session.rollCount} rolls?"
                    } else {
                        "Are you sure you want to delete \"${session.name}\"? " +
                            "This will permanently remove all ${session.rollCount} rolls."
                    },
                    color = Parchment.copy(alpha = 0.9f),
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete()
                    onDismiss()
                }) {
                    Text("Delete", color = Danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = ParchmentDark)
                }
            },
            containerColor = Surface,
            shape = RoundedCornerShape(16.dp),
        )
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isEditing) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Session Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Text(
                        text = session.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Parchment,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = ParchmentDark,
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                // Session Stats section
                SectionHeaderWithIcon(
                    icon = Icons.Filled.Info,
                    title = "Session Stats",
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
                            label = "Rolls",
                            value = "${session.rollCount}",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(
                            icon = Icons.Filled.Storage,
                            label = "Max Rolls",
                            value = session.maxRollsPerSession?.toString() ?: "Unlimited",
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(
                            icon = Icons.Filled.CalendarToday,
                            label = "Created",
                            value = formatFullDate(session.createdAt),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow(
                            icon = Icons.Filled.AccessTime,
                            label = "Last Played",
                            value = formatFullDate(session.lastAccessedAt),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notes section
                SectionHeaderWithIcon(
                    icon = Icons.AutoMirrored.Filled.Notes,
                    title = "Notes",
                    color = Gold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = editedNotes,
                        onValueChange = { editedNotes = it },
                        placeholder = { Text("Add notes about this session...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E1A16),
                        border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.2f)),
                    ) {
                        Text(
                            text = if (session.notes.isNullOrEmpty()) "No notes yet" else session.notes!!,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (session.notes.isNullOrEmpty()) ParchmentDark.copy(alpha = 0.6f) else Parchment,
                            fontStyle = if (session.notes.isNullOrEmpty()) FontStyle.Italic else FontStyle.Normal,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onExport,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.3f)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export", color = Parchment)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onShowSettings()
                        },
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.3f)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Settings", color = Parchment)
                    }
                }

                if (isCurrentSession) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This is your current session",
                        style = MaterialTheme.typography.bodySmall,
                        color = Info.copy(alpha = 0.8f),
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        },
        confirmButton = {
            if (isEditing) {
                TextButton(onClick = {
                    val trimmedName = editedName.trim().ifEmpty { session.name }
                    val trimmedNotes = editedNotes.trim()
                    onUpdate(trimmedName, trimmedNotes)
                    isEditing = false
                }) {
                    Text("Save", color = Gold)
                }
                TextButton(onClick = {
                    editedName = session.name
                    editedNotes = session.notes ?: ""
                    isEditing = false
                }) {
                    Text("Cancel", color = ParchmentDark)
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { showDeleteConfirm = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = null,
                            tint = Danger,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", color = Danger)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onDismiss) {
                        Text("Close", color = ParchmentDark)
                    }
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
internal fun SectionHeaderWithIcon(
    icon: ImageVector,
    title: String,
    color: androidx.compose.ui.graphics.Color,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
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
internal fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ParchmentDark,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Parchment,
        )
    }
}
