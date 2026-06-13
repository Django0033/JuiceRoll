package com.juiceroll.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.sp
import com.juiceroll.data.session.Session
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Surface
import com.juiceroll.ui.theme.Success
import java.util.Calendar

/**
 * Bottom sheet for selecting or managing sessions.
 *
 * Shows a list of sessions with the ability to:
 * - Tap to switch active session
 * - Create a new session
 * - View details, settings, or delete a session
 * - Import a session from clipboard format
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionSelectorSheet(
    sessions: List<Session>,
    currentSession: Session?,
    onSelectSession: (Session) -> Unit,
    onNewSession: () -> Unit,
    onShowDetails: (Session) -> Unit,
    onShowSettings: (Session) -> Unit,
    onDeleteSession: (Session) -> Unit,
    onImportSession: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp),
        ) {
            // Handle bar
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Surface(
                    shape = RoundedCornerShape(2.dp),
                    color = ParchmentDark.copy(alpha = 0.4f),
                    modifier = Modifier.size(width = 40.dp, height = 4.dp),
                ) {}
            }

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Sessions",
                    style = MaterialTheme.typography.titleLarge,
                    color = Parchment,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = onImportSession) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Import", color = ParchmentDark)
                }
            }

            HorizontalDivider(
                color = ParchmentDark.copy(alpha = 0.15f),
                thickness = 1.dp,
            )

            // New Session button
            Surface(
                onClick = {
                    onDismiss()
                    onNewSession()
                },
                shape = RoundedCornerShape(0.dp),
                color = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Success.copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Success,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "New Session",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Parchment,
                        )
                        Text(
                            text = "Start a fresh adventure",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentDark,
                        )
                    }
                }
            }

            HorizontalDivider(
                color = ParchmentDark.copy(alpha = 0.15f),
                thickness = 1.dp,
            )

            // Session list
            sessions.forEach { session ->
                val isSelected = session.id == currentSession?.id
                SessionListItem(
                    session = session,
                    isSelected = isSelected,
                    onSelect = {
                        onDismiss()
                        if (!isSelected) onSelectSession(session)
                    },
                    onDetails = {
                        onDismiss()
                        onShowDetails(session)
                    },
                    onSettings = {
                        onDismiss()
                        onShowSettings(session)
                    },
                    onDelete = {
                        onDismiss()
                        onDeleteSession(session)
                    },
                )
            }

            // Footer with session count
            if (sessions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${sessions.size} session${if (sessions.size == 1) "" else "s"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@Composable
private fun SessionListItem(
    session: Session,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDetails: () -> Unit,
    onSettings: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(0.dp),
        color = if (isSelected) Gold.copy(alpha = 0.06f) else Color.Transparent,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = if (isSelected) Gold.copy(alpha = 0.3f) else ParchmentDark.copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Gold,
                            modifier = Modifier.size(20.dp),
                        )
                    } else {
                        Text(
                            text = if (session.name.isNotEmpty()) session.name.first().uppercase() else "?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Parchment,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Name and subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = Parchment,
                )
                Text(
                    text = "${session.rollCount} rolls \u2022 ${formatRelativeDate(session.lastAccessedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onSettings) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = ParchmentDark,
                        modifier = Modifier.size(20.dp),
                    )
                }
                IconButton(onClick = onDetails) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Details",
                        tint = ParchmentDark,
                        modifier = Modifier.size(20.dp),
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Danger.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }

    HorizontalDivider(
        color = ParchmentDark.copy(alpha = 0.1f),
        thickness = 0.5.dp,
        modifier = Modifier.padding(start = 70.dp),
    )
}

/**
 * Format a timestamp as a relative time string (e.g. "Just now", "5m ago", "3d ago").
 */
internal fun formatRelativeDate(timestampMillis: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestampMillis
    return when {
        diff < 60_000L -> "Just now"
        diff < 3_600_000L -> "${diff / 60_000L}m ago"
        diff < 86_400_000L -> "${diff / 3_600_000L}h ago"
        diff < 604_800_000L -> "${diff / 86_400_000L}d ago"
        else -> {
            val cal = Calendar.getInstance().apply { timeInMillis = timestampMillis }
            "${cal.get(Calendar.MONTH) + 1}/${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.YEAR)}"
        }
    }
}

/**
 * Format a timestamp as a full date string (e.g. "Jan 15, 2025 at 10:30 AM").
 */
internal fun formatFullDate(timestampMillis: Long): String {
    val cal = Calendar.getInstance().apply { timeInMillis = timestampMillis }
    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val hour = cal.get(Calendar.HOUR).let { if (it == 0) 12 else it }
    val minute = cal.get(Calendar.MINUTE).toString().padStart(2, '0')
    val amPm = if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
    return "${months[cal.get(Calendar.MONTH)]} ${cal.get(Calendar.DAY_OF_MONTH)}, " +
        "${cal.get(Calendar.YEAR)} at $hour:$minute $amPm"
}
