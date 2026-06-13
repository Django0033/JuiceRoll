package com.juiceroll.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juiceroll.domain.model.RollResult
import com.juiceroll.ui.display.ResultDisplay
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Surface
import com.juiceroll.ui.theme.CardSurface

// ═══════════════════════════════════════════════════════════════
// Oracle Dialog Wrappers
// ═══════════════════════════════════════════════════════════════

/**
 * Wrapper for oracle dialogs with icon header.
 *
 * Provides a consistent dialog structure with:
 * - Icon in a tinted container + title row
 * - Scrollable content area
 * - Cancel button by default
 * - Reduced horizontal margins for more content space
 */
@Composable
fun OracleDialog(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Surface,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(min = 320.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                // Title row: icon + title + close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor.copy(alpha = 0.2f),
                        tonalElevation = 0.dp,
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.width(22.dp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Parchment,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = ParchmentDark,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    content = content,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel button
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel", color = ParchmentDark)
                }
            }
        }
    }
}

/**
 * Simpler variant of oracle dialog without icon header.
 *
 * Used by dialogs like Challenge, PayThePrice that just need a text title.
 * Has reduced horizontal margins for more content space.
 */
@Composable
fun SimpleOracleDialog(
    title: String,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Surface,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(min = 320.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                // Title row with close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Parchment,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = ParchmentDark,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    content = content,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel button
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel", color = ParchmentDark)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Section Header
// ═══════════════════════════════════════════════════════════════

/**
 * Section header inside dialog content with optional icon and divider.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Gold,
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Gold.copy(alpha = 0.2f),
            thickness = 1.dp,
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Dialog Option Chip
// ═══════════════════════════════════════════════════════════════

/**
 * Clickable option chip for dialog selection controls.
 * Renders as a bordered, tinted chip with centered text.
 */
@Composable
fun DialogOption(
    text: String,
    onClick: () -> Unit,
    accentColor: Color = Gold,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = accentColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.titleSmall,
            color = accentColor,
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Roll Result Section
// ═══════════════════════════════════════════════════════════════

/**
 * Roll result display area inside a dialog.
 * Wraps the existing [ResultDisplay] composable in a card-like container.
 * Shows nothing (empty space) when [result] is null.
 */
@Composable
fun RollResultSection(
    result: RollResult?,
    modifier: Modifier = Modifier,
) {
    if (result != null) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = CardSurface,
            tonalElevation = 0.dp,
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ResultDisplay(result = result)
            }
        }
    } else {
        Spacer(modifier = Modifier.height(0.dp))
    }
}
