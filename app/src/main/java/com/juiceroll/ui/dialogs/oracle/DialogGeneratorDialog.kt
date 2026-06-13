package com.juiceroll.ui.dialogs.oracle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.data.oracle.DialogGeneratorData
import com.juiceroll.domain.model.DialogGridState
import com.juiceroll.domain.model.RollResult
import com.juiceroll.generator.character.DialogGenerator
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Sepia
import com.juiceroll.ui.theme.Success

/**
 * Dialog for the NPC Dialog Grid mini-game.
 *
 * A 5x5 grid where you maintain position and navigate via 2d10 rolls.
 * State is managed inside this dialog composable.
 */
@Composable
fun DialogGeneratorDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    dialogGenerator: DialogGenerator = remember { DialogGenerator() },
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }
    var gridState by remember {
        mutableStateOf(DialogGridState(currentRow = 2, currentCol = 2, conversationActive = true))
    }

    val grid = DialogGeneratorData.grid
    val fragmentDescriptions = DialogGeneratorData.fragmentDescriptions
    val dialogColor = Mystic
    val isActive = gridState.conversationActive
    val currentFragment = grid[gridState.currentRow][gridState.currentCol]
    val isPast = gridState.currentRow <= 1

    fun rollDialog() {
        val moveResult = dialogGenerator.roll(gridState)
        gridState = moveResult.newState
        currentResult = moveResult.result
    }

    fun startNewConversation() {
        gridState = DialogGridState(currentRow = 2, currentCol = 2, conversationActive = true)
    }

    fun setPosition(row: Int, col: Int) {
        gridState = dialogGenerator.setPosition(gridState, row, col)
    }

    OracleDialog(
        title = "NPC Dialog Grid",
        icon = Icons.Filled.Forum,
        accentColor = dialogColor,
        onDismissRequest = onDismiss,
    ) {
        // ── Instructions ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = dialogColor.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, dialogColor.copy(alpha = 0.3f)),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "A mini-game to generate NPC conversations.",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = dialogColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u2022 Tap any cell to set your starting position\n"
                            + "\u2022 Roll 2d10: 1st = Direction + Tone, 2nd = Subject\n"
                            + "\u2022 Doubles = Conversation ends\n"
                            + "\u2022 Edges wrap around",
                    style = MaterialTheme.typography.bodySmall,
                    color = Parchment.copy(alpha = 0.85f),
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── The Grid ──
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Row labels
            Text(
                text = "Top 2 rows = Past / Bottom 3 = Present",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentDark,
            )
            Spacer(modifier = Modifier.height(6.dp))
            // 5x5 grid
            for (row in 0..4) {
                Row(horizontalArrangement = Arrangement.Center) {
                    for (col in 0..4) {
                        val isCurrentPos = row == gridState.currentRow && col == gridState.currentCol
                        val isPastRow = row <= 1

                        val cellColor = when {
                            isCurrentPos -> dialogColor.copy(alpha = 0.3f)
                            isPastRow -> Sepia.copy(alpha = 0.15f)
                            else -> Parchment.copy(alpha = 0.1f)
                        }
                        val borderColor = when {
                            isCurrentPos -> dialogColor
                            isPastRow -> Sepia.copy(alpha = 0.4f)
                            else -> Color.Gray.copy(alpha = 0.3f)
                        }

                        Surface(
                            onClick = { setPosition(row, col) },
                            modifier = Modifier
                                .padding(1.dp)
                                .size(width = 54.dp, height = 36.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = cellColor,
                            border = BorderStroke(
                                width = if (isCurrentPos) 2.dp else 1.dp,
                                color = borderColor,
                            ),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = grid[row][col],
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isCurrentPos) FontWeight.Bold else FontWeight.Normal,
                                    fontStyle = if (isPastRow) FontStyle.Italic else FontStyle.Normal,
                                    color = when {
                                        isCurrentPos -> dialogColor
                                        isPastRow -> Parchment.copy(alpha = 0.8f)
                                        else -> Parchment.copy(alpha = 0.7f)
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Current State ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = if (isActive) Success.copy(alpha = 0.1f) else Rust.copy(alpha = 0.1f),
            border = BorderStroke(
                1.dp,
                if (isActive) Success.copy(alpha = 0.4f) else Rust.copy(alpha = 0.4f),
            ),
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = if (isActive) "Conversation Active" else "Conversation Ended",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) Success else Rust,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Current: ",
                        style = MaterialTheme.typography.bodySmall,
                        color = ParchmentDark,
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = dialogColor.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = currentFragment,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontStyle = if (isPast) FontStyle.Italic else FontStyle.Normal,
                            color = dialogColor,
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isPast) Sepia.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.15f),
                    ) {
                        Text(
                            text = if (isPast) "Past" else "Present",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = if (isPast) FontStyle.Italic else FontStyle.Normal,
                            color = if (isPast) Sepia else ParchmentDark,
                        )
                    }
                }
                fragmentDescriptions[currentFragment]?.let { desc ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = ParchmentDark,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Action Buttons ──
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { rollDialog() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = dialogColor,
                    contentColor = Color.Black,
                ),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = if (isActive) "Roll 2d10" else "Roll (New)",
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                onClick = { startNewConversation() },
                shape = RoundedCornerShape(8.dp),
                color = Success.copy(alpha = 0.2f),
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Reset to center",
                        tint = Success,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Direction/Tone Legend ──
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color.Gray.copy(alpha = 0.08f),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "1st Die (Direction + Tone):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ParchmentDark,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    ToneChip("Neutral", "\u2191", "1-2", Info)
                    ToneChip("Defensive", "\u2190", "3-5", Rust)
                    ToneChip("Aggressive", "\u2192", "6-8", Danger)
                    ToneChip("Helpful", "\u2193", "9-0", Success)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "2nd Die (Subject):  1-2: Them  \u2022  3-5: Me  \u2022  6-8: You  \u2022  9-0: Us",
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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

@Composable
private fun ToneChip(
    tone: String,
    direction: String,
    range: String,
    color: Color,
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = direction,
                style = MaterialTheme.typography.bodySmall,
                color = color,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = tone,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = color,
            )
        }
    }
}
