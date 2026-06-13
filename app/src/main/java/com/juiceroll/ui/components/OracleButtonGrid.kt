package com.juiceroll.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juiceroll.ui.theme.CategoryCharacter
import com.juiceroll.ui.theme.CategoryCombat
import com.juiceroll.ui.theme.CategoryExplore
import com.juiceroll.ui.theme.CategoryOracle
import com.juiceroll.ui.theme.CategoryUtility
import com.juiceroll.ui.theme.CategoryWorld
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.JuiceOrange
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success

/**
 * All 24 oracle dialog types.
 *
 * Each corresponds to a generator and/or dialog composable.
 */
enum class OracleDialogType {
    DETAILS,
    IMMERSION,
    FATE_CHECK,
    NEXT_SCENE,
    EXPECTATION_CHECK,
    RANDOM_EVENT,
    DISCOVER_MEANING,
    NPC_ACTION,
    DIALOG_GENERATOR,
    EXTENDED_NPC_CONVERSATION,
    SETTLEMENT,
    TREASURE,
    CHALLENGE,
    PAY_THE_PRICE,
    WILDERNESS,
    MONSTER_ENCOUNTER,
    DUNGEON,
    LOCATION,
    NAME_GENERATOR,
    ABSTRACT_ICONS,
    SCALE,
    QUEST,
    INTERRUPT_PLOT_POINT,
    DICE_ROLL,
}

/**
 * Static grid of 24 oracle buttons, organized in 6 rows of 4.
 *
 * Each button opens its corresponding dialog or triggers a quick roll.
 */
@Composable
fun OracleButtonGrid(
    onShowDialog: (OracleDialogType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        // Row 1: Oracle (Details, Immersion, Fate, Scene)
        GridRow(buttons = listOf(
            ButtonDef("Details", Icons.Filled.Palette, ParchmentDark, OracleDialogType.DETAILS),
            ButtonDef("Immerse", Icons.Filled.Visibility, JuiceOrange, OracleDialogType.IMMERSION),
            ButtonDef("Fate", Icons.AutoMirrored.Filled.HelpOutline, Mystic, OracleDialogType.FATE_CHECK),
            ButtonDef("Scene", Icons.Filled.Theaters, Info, OracleDialogType.NEXT_SCENE),
        ), onShowDialog = onShowDialog)
        Spacer(Modifier.height(4.dp))

        // Row 2: Oracle (Expect, Scale, Interrupt, Meaning)
        GridRow(buttons = listOf(
            ButtonDef("Expect", Icons.Filled.Psychology, Mystic, OracleDialogType.EXPECTATION_CHECK),
            ButtonDef("Scale", Icons.Filled.SwapVert, CategoryCharacter, OracleDialogType.SCALE),
            ButtonDef("Interrupt", Icons.Filled.Bolt, JuiceOrange, OracleDialogType.INTERRUPT_PLOT_POINT),
            ButtonDef("Meaning", Icons.Filled.Lightbulb, Gold, OracleDialogType.DISCOVER_MEANING),
        ), onShowDialog = onShowDialog)
        Spacer(Modifier.height(4.dp))

        // Row 3: Character + Story (Name, Random, Quest, Challenge)
        GridRow(buttons = listOf(
            ButtonDef("Name", Icons.Filled.Badge, CategoryCharacter, OracleDialogType.NAME_GENERATOR),
            ButtonDef("Random", Icons.Filled.Casino, Gold, OracleDialogType.RANDOM_EVENT),
            ButtonDef("Quest", Icons.Filled.Map, Rust, OracleDialogType.QUEST),
            ButtonDef("Challenge", Icons.Filled.FitnessCenter, CategoryCombat, OracleDialogType.CHALLENGE),
        ), onShowDialog = onShowDialog)
        Spacer(Modifier.height(4.dp))

        // Row 4: Story + Exploration (Price, Wilderness, Monster, NPC)
        GridRow(buttons = listOf(
            ButtonDef("Price", Icons.Filled.Warning, Danger, OracleDialogType.PAY_THE_PRICE),
            ButtonDef("Wilderness", Icons.Filled.Forest, CategoryExplore, OracleDialogType.WILDERNESS),
            ButtonDef("Monster", Icons.Filled.PestControl, Danger, OracleDialogType.MONSTER_ENCOUNTER),
            ButtonDef("NPC", Icons.Filled.Person, CategoryCharacter, OracleDialogType.NPC_ACTION),
        ), onShowDialog = onShowDialog)
        Spacer(Modifier.height(4.dp))

        // Row 5: World (Dialog, Settlement, Treasure, Dungeon)
        GridRow(buttons = listOf(
            ButtonDef("Dialog", Icons.AutoMirrored.Filled.Chat, CategoryCharacter, OracleDialogType.DIALOG_GENERATOR),
            ButtonDef("Settlement", Icons.Filled.LocationCity, CategoryWorld, OracleDialogType.SETTLEMENT),
            ButtonDef("Treasure", Icons.Filled.Diamond, Gold, OracleDialogType.TREASURE),
            ButtonDef("Dungeon", Icons.Filled.Castle, CategoryUtility, OracleDialogType.DUNGEON),
        ), onShowDialog = onShowDialog)
        Spacer(Modifier.height(4.dp))

        // Row 6: World + Utility (Location, NPC Talk, Abstract, Dice)
        GridRow(buttons = listOf(
            ButtonDef("Location", Icons.Filled.GridOn, Rust, OracleDialogType.LOCATION),
            ButtonDef("NPC Talk", Icons.Filled.RecordVoiceOver, Mystic, OracleDialogType.EXTENDED_NPC_CONVERSATION),
            ButtonDef("Abstract", Icons.Filled.Image, Success, OracleDialogType.ABSTRACT_ICONS),
            ButtonDef("Dice", Icons.Filled.Casino, CategoryUtility, OracleDialogType.DICE_ROLL),
        ), onShowDialog = onShowDialog)
    }
}

// ── Internal data and composables ──

private data class ButtonDef(
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val type: OracleDialogType,
)

@Composable
private fun GridRow(
    buttons: List<ButtonDef>,
    onShowDialog: (OracleDialogType) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        buttons.forEach { def ->
            OracleButton(
                label = def.label,
                icon = def.icon,
                color = def.color,
                modifier = Modifier.weight(1f),
                onClick = { onShowDialog(def.type) },
            )
        }
    }
}

@Composable
private fun OracleButton(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1,
                fontSize = 11.sp,
            )
        }
    }
}
