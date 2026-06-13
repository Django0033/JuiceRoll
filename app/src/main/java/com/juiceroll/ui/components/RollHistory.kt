package com.juiceroll.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.model.RollResult
import com.juiceroll.ui.display.ResultDisplay
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Surface as ThemeSurface

/**
 * History section header — static, never rebuilds.
 */
@Composable
fun HistorySectionHeader(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = ParchmentDark.copy(alpha = 0.12f),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
                tint = ParchmentDark.copy(alpha = 0.8f),
                modifier = Modifier.size(12.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Roll History",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark.copy(alpha = 0.8f),
            )
        }
    }
}

/**
 * Shows the roll history with each entry rendered via [ResultDisplay].
 *
 * When history is empty, shows a placeholder message.
 */
@Composable
fun HistoryContent(
    rollHistory: List<RollResult>,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (rollHistory.isEmpty()) {
        EmptyHistory(modifier = modifier)
        return
    }

    Column(modifier = modifier) {
        // Header with clear button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
                tint = ParchmentDark.copy(alpha = 0.8f),
                modifier = Modifier.size(12.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Roll History",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = onClearHistory,
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoStories,
                    contentDescription = "Clear History",
                    tint = ParchmentDark.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        // History list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            items(rollHistory, key = { it.hashCode() }) { result ->
                RollHistoryItem(result = result)
            }
        }
    }
}

/**
 * A single roll history item rendered inside a card.
 */
@Composable
private fun RollHistoryItem(
    result: RollResult,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        color = ParchmentDark.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.1f)),
        tonalElevation = 0.dp,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Type label
            Text(
                text = result.description,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = Gold,
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Dice values
            if (result.diceResults.isNotEmpty()) {
                Row {
                    result.diceResults.forEachIndexed { index, value ->
                        if (index > 0) {
                            Text(
                                text = " + ",
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentDark,
                            )
                        }
                        Text(
                            text = "$value",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Parchment,
                        )
                    }
                    Text(
                        text = " = ${result.total}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ParchmentDark,
                    )
                }
            }

            // Full result display
            ResultDisplay(result = result)
        }
    }
}

/**
 * Empty state placeholder when no rolls have been recorded.
 */
@Composable
private fun EmptyHistory(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.AutoStories,
            contentDescription = null,
            tint = ParchmentDark.copy(alpha = 0.4f),
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No rolls yet",
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = FontStyle.Italic,
            color = ParchmentDark,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Tap an oracle button to begin",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentDark.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
        )
    }
}
