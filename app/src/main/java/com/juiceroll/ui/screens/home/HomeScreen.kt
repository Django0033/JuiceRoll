package com.juiceroll.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juiceroll.domain.model.RollResult
import com.juiceroll.ui.theme.CategoryCharacter
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.components.HistoryContent
import com.juiceroll.ui.components.OracleButtonGrid
import com.juiceroll.ui.components.OracleDialogType
import com.juiceroll.ui.dialogs.DiceRollDialog
import com.juiceroll.ui.dialogs.AboutJuiceDialog
import com.juiceroll.ui.dialogs.OracleDialog
import com.juiceroll.ui.dialogs.RollResultSection
import com.juiceroll.ui.dialogs.SectionHeader
import com.juiceroll.ui.dialogs.SimpleOracleDialog
import com.juiceroll.ui.dialogs.oracle.AbstractIconsDialog
import com.juiceroll.ui.dialogs.oracle.ChallengeDialog
import com.juiceroll.ui.dialogs.oracle.DetailsDialog
import com.juiceroll.ui.dialogs.oracle.DialogGeneratorDialog
import com.juiceroll.ui.dialogs.oracle.DungeonDialog
import com.juiceroll.ui.dialogs.oracle.ExtendedNpcConversationDialog
import com.juiceroll.ui.dialogs.oracle.FateCheckDialog
import com.juiceroll.ui.dialogs.oracle.ImmersionDialog
import com.juiceroll.ui.dialogs.oracle.LocationDialog
import com.juiceroll.ui.dialogs.oracle.MonsterEncounterDialog
import com.juiceroll.ui.dialogs.oracle.NameGeneratorDialog
import com.juiceroll.ui.dialogs.oracle.NextSceneDialog
import com.juiceroll.ui.dialogs.oracle.NpcActionDialog
import com.juiceroll.ui.dialogs.oracle.PayThePriceDialog
import com.juiceroll.ui.dialogs.oracle.SettlementDialog
import com.juiceroll.ui.dialogs.oracle.TreasureDialog
import com.juiceroll.ui.dialogs.oracle.WildernessDialog
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.viewmodel.HomeViewModel

/**
 * Home screen with oracle button grid and roll history.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit = {},
) {
    val rollHistory by viewModel.rollHistory.collectAsState()
    val activeSession by viewModel.activeSession.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var currentDialog by remember { mutableStateOf<OracleDialogType?>(null) }
    var showAbout by remember { mutableStateOf(false) }

    // ── Show About dialog ──
    if (showAbout) {
        AboutJuiceDialog(onDismiss = { showAbout = false })
    }

    // ── Dialog routing ──
    currentDialog?.let { type ->
        OracleDialogHost(
            type = type,
            viewModel = viewModel,
            onDismiss = { currentDialog = null },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "JuiceRoll",
                            style = MaterialTheme.typography.titleMedium,
                            color = Parchment,
                        )
                        if (activeSession != null) {
                            Text(
                                text = activeSession!!.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentDark,
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showAbout = true }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "About",
                            tint = ParchmentDark,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            // Oracle button grid (scrollable with parent)
            OracleButtonGrid(
                onShowDialog = { type -> currentDialog = type },
            )

            HorizontalDivider(
                color = ParchmentDark.copy(alpha = 0.2f),
                thickness = 1.dp,
            )

            // Roll history
            HistoryContent(
                rollHistory = rollHistory,
                onClearHistory = { viewModel.clearHistory() },
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Dialog Host — routes OracleDialogType to the correct dialog
// ═══════════════════════════════════════════════════════════════

@Composable
private fun OracleDialogHost(
    type: OracleDialogType,
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    val onRoll: (RollResult) -> Unit = { result ->
        viewModel.addToHistory(result)
        onDismiss()
    }

    val onDismissAndRoll: (RollResult) -> Unit = { result ->
        viewModel.addToHistory(result)
        onDismiss()
    }

    when (type) {
        OracleDialogType.FATE_CHECK -> FateCheckDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            fateCheckGenerator = viewModel.fateCheck,
        )

        OracleDialogType.DICE_ROLL -> DiceRollDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            initialDiceMode = viewModel.diceDialogMode.value,
            initialIronswornRollType = viewModel.diceDialogIronswornType.value,
            initialOracleDieType = viewModel.diceDialogOracleDie.value,
            onStateChanged = { mode, type, die ->
                viewModel.updateDiceState(mode, type, die)
            },
        )

        OracleDialogType.NEXT_SCENE -> NextSceneDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            nextSceneGenerator = viewModel.nextScene,
            randomEventGenerator = viewModel.randomEvent,
            interruptPlotPointGenerator = viewModel.interruptPlotPoint,
        )

        OracleDialogType.IMMERSION -> ImmersionDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            immersionGenerator = viewModel.immersion,
        )

        OracleDialogType.DETAILS -> DetailsDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            detailsGenerator = viewModel.details,
        )

        OracleDialogType.CHALLENGE -> ChallengeDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            challengeGenerator = viewModel.challenge,
        )

        OracleDialogType.PAY_THE_PRICE -> PayThePriceDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            payThePriceGenerator = viewModel.payThePrice,
        )

        OracleDialogType.NPC_ACTION -> NpcActionDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            npcActionGenerator = viewModel.npcAction,
        )

        OracleDialogType.DIALOG_GENERATOR -> DialogGeneratorDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            dialogGenerator = viewModel.dialog,
        )

        OracleDialogType.EXTENDED_NPC_CONVERSATION -> ExtendedNpcConversationDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            conversationGenerator = viewModel.extendedNpcConversation,
        )

        OracleDialogType.NAME_GENERATOR -> NameGeneratorDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            nameGenerator = viewModel.nameGenerator,
        )

        OracleDialogType.SETTLEMENT -> SettlementDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            settlementGenerator = viewModel.settlement,
        )

        OracleDialogType.TREASURE -> TreasureDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            treasureGenerator = viewModel.objectTreasure,
        )

        OracleDialogType.WILDERNESS -> WildernessDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            generator = viewModel.wilderness,
            dungeonGenerator = viewModel.dungeon,
            wildernessState = viewModel.wildernessState.value,
            onStateChange = { viewModel.updateWildernessState(it) },
        )

        OracleDialogType.MONSTER_ENCOUNTER -> MonsterEncounterDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            monsterGenerator = viewModel.monsterEncounter,
        )

        OracleDialogType.DUNGEON -> DungeonDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            generator = viewModel.dungeon,
            isEntering = viewModel.dungeonPhase.value,
            isTwoPassMode = viewModel.dungeonTwoPassMode.value,
            twoPassHasFirstDoubles = viewModel.twoPassHasFirstDoubles.value,
            onPhaseChange = { viewModel.setDungeonPhase(it) },
            onTwoPassModeChange = { viewModel.setDungeonTwoPassMode(it) },
            onTwoPassFirstDoublesChange = { viewModel.setTwoPassFirstDoubles(it) },
        )

        OracleDialogType.LOCATION -> LocationDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            locationGenerator = viewModel.location,
        )

        OracleDialogType.ABSTRACT_ICONS -> AbstractIconsDialog(
            onRoll = onRoll,
            onDismiss = onDismiss,
            abstractIconsGenerator = viewModel.abstractIcons,
        )

        // ── Quick roll / simple dialog types ──

        OracleDialogType.SCALE -> SimpleOracleDialog(
            title = "Scale",
            onDismissRequest = onDismiss,
        ) {
            ScaleContent(
                viewModel = viewModel,
                onDismiss = onDismiss,
            )
        }

        OracleDialogType.QUEST -> SimpleOracleDialog(
            title = "Quest",
            onDismissRequest = onDismiss,
        ) {
            QuestContent(
                viewModel = viewModel,
                onDismiss = onDismiss,
            )
        }

        OracleDialogType.DISCOVER_MEANING -> SimpleOracleDialog(
            title = "Discover Meaning",
            onDismissRequest = onDismiss,
        ) {
            QuickRollContent(
                title = "Discover Meaning",
                icon = Icons.Filled.Lightbulb,
                accentColor = Gold,
                onRollNow = {
                    viewModel.quickRollDiscoverMeaning()
                    onDismiss()
                },
            )
        }

        OracleDialogType.INTERRUPT_PLOT_POINT -> SimpleOracleDialog(
            title = "Interrupt Plot Point",
            onDismissRequest = onDismiss,
        ) {
            QuickRollContent(
                title = "Interrupt Plot Point",
                icon = Icons.Filled.Bolt,
                accentColor = Mystic,
                onRollNow = {
                    viewModel.quickRollInterruptPlotPoint()
                    onDismiss()
                },
            )
        }

        OracleDialogType.EXPECTATION_CHECK -> SimpleOracleDialog(
            title = "Expectation Check",
            onDismissRequest = onDismiss,
        ) {
            ExpectationCheckContent(
                viewModel = viewModel,
                onDismiss = onDismiss,
            )
        }

        OracleDialogType.RANDOM_EVENT -> SimpleOracleDialog(
            title = "Random Event",
            onDismissRequest = onDismiss,
        ) {
            RandomEventContent(
                viewModel = viewModel,
                onDismiss = onDismiss,
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Inline dialog content for simple generators
// ═══════════════════════════════════════════════════════════════

@Composable
private fun QuickRollContent(
    title: String,
    icon: ImageVector,
    accentColor: androidx.compose.ui.graphics.Color,
    onRollNow: () -> Unit,
) {
    androidx.compose.material3.Button(
        onClick = onRollNow,
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = accentColor.copy(alpha = 0.2f),
            contentColor = accentColor,
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Modifier.padding(start = 8.dp)
        Text("Roll $title")
    }
}

@Composable
private fun ScaleContent(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SectionHeader(title = "Scale Roll")

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Roll on the scale table to determine relative magnitude.",
        style = MaterialTheme.typography.bodySmall,
        color = ParchmentDark,
    )

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            currentResult = viewModel.scale.roll()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = CategoryCharacter.copy(alpha = 0.2f),
            contentColor = CategoryCharacter,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Icon(Icons.Filled.SwapVert, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(if (currentResult == null) "Roll Scale" else "Reroll")
    }

    Spacer(Modifier.height(12.dp))

    RollResultSection(result = currentResult)

    if (currentResult != null) {
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                currentResult?.let { viewModel.addToHistory(it) }
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold.copy(alpha = 0.2f),
                contentColor = Gold,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Save & Close")
        }
    }
}

@Composable
private fun QuestContent(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SectionHeader(title = "Quest Generator")

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Generate a quest with a focus, location, and objective.",
        style = MaterialTheme.typography.bodySmall,
        color = ParchmentDark,
    )

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            currentResult = viewModel.quest.generate()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Rust.copy(alpha = 0.2f),
            contentColor = Rust,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(if (currentResult == null) "Generate Quest" else "Regenerate")
    }

    Spacer(Modifier.height(12.dp))

    RollResultSection(result = currentResult)

    if (currentResult != null) {
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                currentResult?.let { viewModel.addToHistory(it) }
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold.copy(alpha = 0.2f),
                contentColor = Gold,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Save & Close")
        }
    }
}

@Composable
private fun ExpectationCheckContent(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SectionHeader(title = "Expectation Check")

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Check if things happen the way you expect. "
                + "Roll 2d6 — the dice will tell you if expectations are met or subverted.",
        style = MaterialTheme.typography.bodySmall,
        color = ParchmentDark,
    )

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            currentResult = viewModel.expectationCheck.check()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Mystic.copy(alpha = 0.2f),
            contentColor = Mystic,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Icon(Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(if (currentResult == null) "Roll Expectation Check" else "Reroll")
    }

    Spacer(Modifier.height(12.dp))

    RollResultSection(result = currentResult)

    if (currentResult != null) {
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                currentResult?.let { viewModel.addToHistory(it) }
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold.copy(alpha = 0.2f),
                contentColor = Gold,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Save & Close")
        }
    }
}

@Composable
private fun RandomEventContent(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    SectionHeader(title = "Random Event")

    Spacer(Modifier.height(8.dp))

    Text(
        text = "Generate an unexpected event to add twists to your story. "
                + "Rolls for focus, modifier, and idea to create a full event.",
        style = MaterialTheme.typography.bodySmall,
        color = ParchmentDark,
    )

    Spacer(Modifier.height(12.dp))

    Button(
        onClick = {
            currentResult = viewModel.randomEvent.generate()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gold.copy(alpha = 0.2f),
            contentColor = Gold,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Icon(Icons.Filled.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(if (currentResult == null) "Generate Random Event" else "Reroll")
    }

    Spacer(Modifier.height(12.dp))

    RollResultSection(result = currentResult)

    if (currentResult != null) {
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                currentResult?.let { viewModel.addToHistory(it) }
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold.copy(alpha = 0.2f),
                contentColor = Gold,
            ),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Save & Close")
        }
    }
}
