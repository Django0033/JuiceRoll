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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juiceroll.domain.engine.RollEngine
import com.juiceroll.domain.model.IronswornActionResult
import com.juiceroll.domain.model.IronswornCursedOracleResult
import com.juiceroll.domain.model.IronswornMomentumBurnResult
import com.juiceroll.domain.model.IronswornOdds
import com.juiceroll.domain.model.IronswornOracleResult
import com.juiceroll.domain.model.IronswornOutcome
import com.juiceroll.domain.model.IronswornProgressResult
import com.juiceroll.domain.model.IronswornYesNoResult
import com.juiceroll.domain.model.RollResult
import com.juiceroll.ui.theme.Danger
import com.juiceroll.ui.theme.Gold
import com.juiceroll.ui.theme.Info
import com.juiceroll.ui.theme.Mystic
import com.juiceroll.ui.theme.Parchment
import com.juiceroll.ui.theme.ParchmentDark
import com.juiceroll.ui.theme.Rust
import com.juiceroll.ui.theme.Success
import com.juiceroll.ui.theme.Surface as ThemeSurface
import kotlin.math.abs

// ═══════════════════════════════════════════════════════════════
// Private helpers
// ═══════════════════════════════════════════════════════════════

private data class DicePreset(
    val label: String,
    val count: Int,
    val sides: Int,
    val description: String,
)

private val standardPresets = listOf(
    DicePreset("1d6", 1, 6, "Oracle, simple checks"),
    DicePreset("2d6", 2, 6, "PbtA, reaction rolls"),
    DicePreset("1d10", 1, 10, "Half-tables, icons row"),
    DicePreset("1d100", 1, 100, "Table lookups"),
    DicePreset("1d20", 1, 20, "D&D 5e checks"),
    DicePreset("3d6", 3, 6, "Stat generation"),
)

private val fatePresets = listOf(
    DicePreset("2dF", 2, 0, "Fate Check (Juice)"),
    DicePreset("4dF", 4, 0, "Fate Core/Accelerated"),
    DicePreset("1dF", 1, 0, "Single Fate die"),
)

private val diceSideOptions = listOf(
    4 to "d4",
    6 to "d6",
    8 to "d8",
    10 to "d10",
    12 to "d12",
    20 to "d20",
    100 to "d%",
)

private fun calculateIronswornOutcome(score: Int, challengeDice: List<Int>): IronswornOutcome {
    val beatsFirst = score > challengeDice[0]
    val beatsSecond = score > challengeDice[1]
    return when {
        beatsFirst && beatsSecond -> IronswornOutcome.STRONG_HIT
        beatsFirst || beatsSecond -> IronswornOutcome.WEAK_HIT
        else -> IronswornOutcome.MISS
    }
}

private fun isDoublesOnD100(roll: Int): Boolean {
    if (roll == 100) return true
    if (roll < 11) return false
    return (roll / 10) == (roll % 10)
}

private fun isYesBasedOnOdds(roll: Int, odds: IronswornOdds): Boolean = when (odds) {
    IronswornOdds.ALMOST_CERTAIN -> roll >= 11
    IronswornOdds.LIKELY -> roll >= 26
    IronswornOdds.FIFTY_FIFTY -> roll >= 51
    IronswornOdds.UNLIKELY -> roll >= 76
    IronswornOdds.SMALL_CHANCE -> roll >= 91
}

private fun oddsThreshold(odds: IronswornOdds): Int = when (odds) {
    IronswornOdds.ALMOST_CERTAIN -> 11
    IronswornOdds.LIKELY -> 26
    IronswornOdds.FIFTY_FIFTY -> 51
    IronswornOdds.UNLIKELY -> 76
    IronswornOdds.SMALL_CHANCE -> 91
}

private fun oddsDisplayName(odds: IronswornOdds): String = when (odds) {
    IronswornOdds.ALMOST_CERTAIN -> "Almost Certain"
    IronswornOdds.LIKELY -> "Likely"
    IronswornOdds.FIFTY_FIFTY -> "50/50"
    IronswornOdds.UNLIKELY -> "Unlikely"
    IronswornOdds.SMALL_CHANCE -> "Small Chance"
}

private fun rollSkewedD6(engine: RollEngine, skew: Int): Int {
    if (skew == 0) return engine.rollDie(6)
    val absSkew = abs(skew)
    val rolls = List(absSkew + 1) { engine.rollDie(6) }
    return if (skew > 0) rolls.max() else rolls.min()
}

// ═══════════════════════════════════════════════════════════════
// DiceRollDialog — main entry point
// ═══════════════════════════════════════════════════════════════

/**
 * Custom dialog for rolling dice in 3 modes:
 *
 * **Mode 0 — Standard Dice:** Die size selector (d4-d20+d100),
 * number of dice (1-10), advantage/disadvantage toggle,
 * skew slider (-3 to +3), and modifier control.
 *
 * **Mode 1 — Fate/Fudge Dice:** Number of Fate dice (1-10),
 * symbolic display (− ○ +), total with color coding.
 *
 * **Mode 2 — Ironsworn/Starforged:** Action roll (1d6+stat+adds vs 2d10),
 * progress roll, oracle roll, yes/no oracle, cursed oracle,
 * and momentum burn.
 *
 * State is synced via callbacks for persistence across dialog invocations.
 */
@Composable
fun DiceRollDialog(
    onRoll: (RollResult) -> Unit,
    onDismiss: () -> Unit,
    initialDiceMode: Int = 0,
    initialIronswornRollType: String = "action",
    initialOracleDieType: Int = 100,
    onStateChanged: (mode: Int, ironswornType: String, oracleDie: Int) -> Unit = { _, _, _ -> },
    rollEngine: RollEngine = RollEngine,
) {
    // ── Synced state (from props, synced back via callbacks) ──
    var diceMode by remember { mutableStateOf(initialDiceMode) }
    var ironswornRollType by remember { mutableStateOf(initialIronswornRollType) }
    var oracleDieType by remember { mutableStateOf(initialOracleDieType) }

    LaunchedEffect(initialDiceMode) { diceMode = initialDiceMode }
    LaunchedEffect(initialIronswornRollType) { ironswornRollType = initialIronswornRollType }
    LaunchedEffect(initialOracleDieType) { oracleDieType = initialOracleDieType }

    // ── Internal state ──
    var diceCount by remember { mutableStateOf(2) }
    var diceSides by remember { mutableStateOf(6) }
    var mod by remember { mutableStateOf(0) }
    var skew by remember { mutableStateOf(0) }
    var advantage by remember { mutableStateOf(false) }
    var disadvantage by remember { mutableStateOf(false) }

    var ironswornStat by remember { mutableStateOf(0) }
    var ironswornAdds by remember { mutableStateOf(0) }
    var ironswornProgress by remember { mutableStateOf(5) }
    var yesNoOdds by remember { mutableStateOf(IronswornOdds.LIKELY) }
    var momentum by remember { mutableStateOf(0) }
    var useMomentumBurn by remember { mutableStateOf(false) }

    var currentResult by remember { mutableStateOf<RollResult?>(null) }

    // ── Theme ──
    val ironswornColor = Color(0xFF8595D8)
    val useFateDice = diceMode == 1
    val useIronsworn = diceMode == 2
    val themeColor = when {
        useIronsworn -> ironswornColor
        useFateDice -> Mystic
        else -> Rust
    }

    val scrollState = rememberScrollState()

    // ── Local helper functions ──
    fun notifyChanged() { onStateChanged(diceMode, ironswornRollType, oracleDieType) }

    fun switchMode(mode: Int) {
        diceMode = mode
        if (mode == 0) { diceCount = 2; diceSides = 6 }
        if (mode == 1) { diceCount = 2 }
        notifyChanged()
    }

    fun applyPreset(p: DicePreset) {
        diceCount = p.count
        if (!useFateDice) diceSides = p.sides
        skew = 0; advantage = false; disadvantage = false
    }

    fun changeIronswornType(t: String) { ironswornRollType = t; notifyChanged() }
    fun changeOracleDie(d: Int) { oracleDieType = d; notifyChanged() }

    fun doRoll() {
        currentResult = when {
            useIronsworn -> makeIronswornRoll(rollEngine, ironswornRollType,
                ironswornStat, ironswornAdds, ironswornProgress, yesNoOdds,
                oracleDieType, momentum, useMomentumBurn)
            useFateDice -> {
                val d = rollEngine.rollFateDice(diceCount)
                RollResult.FateRollResult(values = d, total = d.sum() + mod)
            }
            else -> makeStandardRoll(rollEngine, diceCount, diceSides, mod, skew, advantage, disadvantage)
        }
    }

    fun saveAndClose() {
        currentResult?.let { onRoll(it) }
        onDismiss()
    }

    // ═══════════════════════════════════════════════════════════════
    // Layout
    // ═══════════════════════════════════════════════════════════════
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = ThemeSurface,
            tonalElevation = 0.dp,
            border = BorderStroke(1.dp, themeColor.copy(alpha = 0.3f)),
        ) {
            Column {
                // Header
                DiceRollHeader(
                    themeColor = themeColor,
                    useIronsworn = useIronsworn,
                    useFateDice = useFateDice,
                    onClose = onDismiss,
                )

                // Scrollable content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    DiceModeToggle(
                        diceMode = diceMode,
                        onModeSelected = { switchMode(it) },
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick presets or Ironsworn roll type selector
                    SectionHeader(title = if (useIronsworn) "Roll Type" else "Quick Roll")
                    Spacer(modifier = Modifier.height(8.dp))

                    if (useIronsworn) {
                        IronswornRollTypeSelector(
                            selectedType = ironswornRollType,
                            ironswornColor = ironswornColor,
                            onTypeSelected = { changeIronswornType(it) },
                        )
                    } else {
                        QuickPresetsRow(
                            useFateDice = useFateDice,
                            diceCount = diceCount,
                            diceSides = diceSides,
                            themeColor = themeColor,
                            onPreset = { applyPreset(it) },
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    SectionHeader(title = "Custom Configuration")
                    Spacer(modifier = Modifier.height(8.dp))

                    ConfigPanel(
                        useIronsworn = useIronsworn,
                        useFateDice = useFateDice,
                        diceCount = diceCount,
                        diceSides = diceSides,
                        mod = mod,
                        skew = skew,
                        advantage = advantage,
                        disadvantage = disadvantage,
                        ironswornRollType = ironswornRollType,
                        ironswornStat = ironswornStat,
                        ironswornAdds = ironswornAdds,
                        ironswornProgress = ironswornProgress,
                        yesNoOdds = yesNoOdds,
                        oracleDieType = oracleDieType,
                        momentum = momentum,
                        useMomentumBurn = useMomentumBurn,
                        ironswornColor = ironswornColor,
                        themeColor = themeColor,
                        onDiceCountChange = { diceCount = it },
                        onDiceSidesChange = { diceSides = it; skew = 0 },
                        onModChange = { mod = it },
                        onSkewChange = { skew = it },
                        onAdvantageChange = { advantage = it; if (it) disadvantage = false },
                        onDisadvantageChange = { disadvantage = it; if (it) advantage = false },
                        onIronswornStatChange = { ironswornStat = it },
                        onIronswornAddsChange = { ironswornAdds = it },
                        onIronswornProgressChange = { ironswornProgress = it },
                        onYesNoOddsChange = { yesNoOdds = it },
                        onOracleDieTypeChange = { changeOracleDie(it) },
                        onMomentumChange = { momentum = it },
                        onUseMomentumBurnChange = { useMomentumBurn = it },
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    RollPreviewPanel(
                        useIronsworn = useIronsworn,
                        useFateDice = useFateDice,
                        diceCount = diceCount,
                        diceSides = diceSides,
                        mod = mod,
                        skew = skew,
                        advantage = advantage,
                        disadvantage = disadvantage,
                        ironswornRollType = ironswornRollType,
                        ironswornStat = ironswornStat,
                        ironswornAdds = ironswornAdds,
                        ironswornProgress = ironswornProgress,
                        yesNoOdds = yesNoOdds,
                        oracleDieType = oracleDieType,
                        useMomentumBurn = useMomentumBurn,
                        momentum = momentum,
                        themeColor = themeColor,
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    RollResultSection(result = currentResult)

                    if (currentResult != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        ReRollAndSaveRow(
                            onReRoll = { doRoll() },
                            onSave = { saveAndClose() },
                            themeColor = themeColor,
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Bottom action bar
                BottomActionsBar(
                    themeColor = themeColor,
                    onCancel = onDismiss,
                    onRoll = { doRoll() },
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Roll logic (pure functions)
// ═══════════════════════════════════════════════════════════════

private fun makeStandardRoll(
    engine: RollEngine,
    count: Int,
    sides: Int,
    mod: Int,
    skew: Int,
    adv: Boolean,
    dis: Boolean,
): RollResult {
    if (adv) {
        val r = engine.roll(count, sides, advantage = true)
        return r.copy(total = r.total + mod)
    }
    if (dis) {
        val r = engine.roll(count, sides, disadvantage = true)
        return r.copy(total = r.total + mod)
    }
    if (skew != 0 && sides == 6) {
        val values = List(count) { rollSkewedD6(engine, skew) }
        return RollResult.StandardRollResult(values = values, sides = 6, total = values.sum() + mod)
    }
    val values = List(count) { engine.rollDie(sides) }
    return RollResult.StandardRollResult(values = values, sides = sides, total = values.sum() + mod)
}

private fun makeIronswornRoll(
    engine: RollEngine,
    rollType: String,
    stat: Int,
    adds: Int,
    progress: Int,
    odds: IronswornOdds,
    oracleDie: Int,
    momentum: Int,
    useBurn: Boolean,
): RollResult = when (rollType) {
    "action" -> {
        val actionDie = engine.rollDie(6)
        val challengeDice = listOf(engine.rollDie(10), engine.rollDie(10))
        val score = actionDie + stat + adds
        val isMatch = challengeDice[0] == challengeDice[1]
        val outcome = calculateIronswornOutcome(score, challengeDice)

        if (useBurn) {
            val burnedOutcome = calculateIronswornOutcome(momentum, challengeDice)
            val wasUpgraded = when {
                outcome == IronswornOutcome.MISS && burnedOutcome != IronswornOutcome.MISS -> true
                outcome == IronswornOutcome.WEAK_HIT && burnedOutcome == IronswornOutcome.STRONG_HIT -> true
                else -> false
            }
            IronswornMomentumBurnResult(
                actionDie = actionDie,
                challengeDice = challengeDice,
                momentumValue = momentum,
                statBonus = stat,
                adds = adds,
                originalActionScore = score,
                originalOutcome = outcome,
                burnedOutcome = burnedOutcome,
                isMatch = isMatch,
                wasUpgraded = wasUpgraded,
            )
        } else {
            IronswornActionResult(
                actionDie = actionDie,
                challengeDice = challengeDice,
                statBonus = stat,
                adds = adds,
                actionScore = score,
                outcome = outcome,
                isMatch = isMatch,
            )
        }
    }
    "progress" -> {
        val challengeDice = listOf(engine.rollDie(10), engine.rollDie(10))
        val isMatch = challengeDice[0] == challengeDice[1]
        val outcome = calculateIronswornOutcome(progress, challengeDice)
        IronswornProgressResult(
            progressScore = progress,
            challengeDice = challengeDice,
            outcome = outcome,
            isMatch = isMatch,
        )
    }
    "oracle" -> {
        val roll = engine.rollDie(oracleDie)
        IronswornOracleResult(
            oracleRoll = roll,
            dieType = oracleDie,
            isMatch = isDoublesOnD100(roll),
        )
    }
    "yesno" -> {
        val roll = engine.rollDie(100)
        IronswornYesNoResult(
            roll = roll,
            odds = odds,
            isYes = isYesBasedOnOdds(roll, odds),
            isMatch = isDoublesOnD100(roll),
        )
    }
    "cursed" -> {
        val oracleRoll = engine.rollDie(100)
        val cursedDie = engine.rollDie(10)
        IronswornCursedOracleResult(
            oracleRoll = oracleRoll,
            cursedDie = cursedDie,
            isCursed = cursedDie == 10,
            isMatch = isDoublesOnD100(oracleRoll),
        )
    }
    else -> {
        val actionDie = engine.rollDie(6)
        val challengeDice = listOf(engine.rollDie(10), engine.rollDie(10))
        val score = actionDie + stat + adds
        IronswornActionResult(
            actionDie = actionDie,
            challengeDice = challengeDice,
            statBonus = stat,
            adds = adds,
            actionScore = score,
            outcome = calculateIronswornOutcome(score, challengeDice),
            isMatch = challengeDice[0] == challengeDice[1],
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Header composable
// ═══════════════════════════════════════════════════════════════

@Composable
private fun DiceRollHeader(
    themeColor: Color,
    useIronsworn: Boolean,
    useFateDice: Boolean,
    onClose: () -> Unit,
) {
    Surface(color = themeColor.copy(alpha = 0.08f), tonalElevation = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = themeColor.copy(alpha = 0.15f),
                tonalElevation = 0.dp,
            ) {
                Icon(
                    imageVector = when {
                        useIronsworn -> Icons.Filled.Shield
                        useFateDice -> Icons.Filled.AutoAwesome
                        else -> Icons.Filled.Casino
                    },
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.padding(8.dp).size(20.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Dice Roll", style = MaterialTheme.typography.titleLarge, color = Parchment)
                Text(
                    text = when {
                        useIronsworn -> "Ironsworn/Starforged dice"
                        useFateDice -> "Fate dice for oracle checks"
                        else -> "Standard polyhedral dice"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = ParchmentDark,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = ParchmentDark)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Mode toggle (segmented control)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun DiceModeToggle(diceMode: Int, onModeSelected: (Int) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.Black.copy(alpha = 0.25f),
        border = BorderStroke(1.dp, ParchmentDark.copy(alpha = 0.15f)),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            ModeTab("Standard", Icons.Filled.Casino, diceMode == 0, Rust, Modifier.weight(1f)) { onModeSelected(0) }
            ModeTab("Fate", Icons.Filled.AutoAwesome, diceMode == 1, Mystic, Modifier.weight(1f)) { onModeSelected(1) }
            ModeTab("Ironsworn", Icons.Filled.Shield, diceMode == 2, Color(0xFF8595D8), Modifier.weight(1f)) { onModeSelected(2) }
        }
    }
}

@Composable
private fun ModeTab(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) color.copy(alpha = 0.2f) else Color.Transparent,
        border = if (selected) BorderStroke(1.5.dp, color.copy(alpha = 0.5f)) else null,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) color else ParchmentDark, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) color else ParchmentDark)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Quick presets
// ═══════════════════════════════════════════════════════════════

@Composable
private fun QuickPresetsRow(
    useFateDice: Boolean,
    diceCount: Int,
    diceSides: Int,
    themeColor: Color,
    onPreset: (DicePreset) -> Unit,
) {
    val presets = if (useFateDice) fatePresets else standardPresets
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        presets.forEach { p ->
            val sel = if (useFateDice) diceCount == p.count else diceCount == p.count && diceSides == p.sides
            Chip(label = p.label, selected = sel, color = themeColor) { onPreset(p) }
        }
    }
}

@Composable
private fun Chip(label: String, selected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) color.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.25f),
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) color else ParchmentDark.copy(alpha = 0.2f),
        ),
        tonalElevation = 0.dp,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) color else Parchment,
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Ironsworn roll type selector
// ═══════════════════════════════════════════════════════════════

@Composable
private fun IronswornRollTypeSelector(
    selectedType: String,
    ironswornColor: Color,
    onTypeSelected: (String) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf("action" to "Action", "progress" to "Progress", "oracle" to "Oracle",
            "yesno" to "Yes/No", "cursed" to "Cursed").forEach { (value, label) ->
            val sel = selectedType == value
            Surface(
                onClick = { onTypeSelected(value) },
                shape = RoundedCornerShape(8.dp),
                color = if (sel) ironswornColor.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.25f),
                border = BorderStroke(
                    width = if (sel) 1.5.dp else 1.dp,
                    color = if (sel) ironswornColor else ParchmentDark.copy(alpha = 0.2f),
                ),
                tonalElevation = 0.dp,
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                    color = if (sel) ironswornColor else Parchment,
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Config panel (routes to mode-specific panel)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ConfigPanel(
    useIronsworn: Boolean,
    useFateDice: Boolean,
    diceCount: Int,
    diceSides: Int,
    mod: Int,
    skew: Int,
    advantage: Boolean,
    disadvantage: Boolean,
    ironswornRollType: String,
    ironswornStat: Int,
    ironswornAdds: Int,
    ironswornProgress: Int,
    yesNoOdds: IronswornOdds,
    oracleDieType: Int,
    momentum: Int,
    useMomentumBurn: Boolean,
    ironswornColor: Color,
    themeColor: Color,
    onDiceCountChange: (Int) -> Unit,
    onDiceSidesChange: (Int) -> Unit,
    onModChange: (Int) -> Unit,
    onSkewChange: (Int) -> Unit,
    onAdvantageChange: (Boolean) -> Unit,
    onDisadvantageChange: (Boolean) -> Unit,
    onIronswornStatChange: (Int) -> Unit,
    onIronswornAddsChange: (Int) -> Unit,
    onIronswornProgressChange: (Int) -> Unit,
    onYesNoOddsChange: (IronswornOdds) -> Unit,
    onOracleDieTypeChange: (Int) -> Unit,
    onMomentumChange: (Int) -> Unit,
    onUseMomentumBurnChange: (Boolean) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.Black.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, themeColor.copy(alpha = 0.15f)),
    ) {
        Column(Modifier.padding(12.dp)) {
            when {
                useIronsworn -> IronswornConfigPanel(
                    ironswornRollType, ironswornStat, ironswornAdds, ironswornProgress,
                    yesNoOdds, oracleDieType, momentum, useMomentumBurn, ironswornColor,
                    onIronswornStatChange, onIronswornAddsChange, onIronswornProgressChange,
                    onYesNoOddsChange, onOracleDieTypeChange, onMomentumChange, onUseMomentumBurnChange,
                )
                useFateDice -> FateConfigPanel(diceCount, mod, themeColor, onDiceCountChange, onModChange)
                else -> StandardConfigPanel(diceCount, diceSides, mod, skew, advantage, disadvantage,
                    themeColor, onDiceCountChange, onDiceSidesChange, onModChange, onSkewChange,
                    onAdvantageChange, onDisadvantageChange)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Standard dice config
// ═══════════════════════════════════════════════════════════════

@Composable
private fun StandardConfigPanel(
    diceCount: Int, diceSides: Int, mod: Int, skew: Int,
    advantage: Boolean, disadvantage: Boolean,
    themeColor: Color,
    onDiceCountChange: (Int) -> Unit, onDiceSidesChange: (Int) -> Unit,
    onModChange: (Int) -> Unit, onSkewChange: (Int) -> Unit,
    onAdvantageChange: (Boolean) -> Unit, onDisadvantageChange: (Boolean) -> Unit,
) {
    Row {
        NumberControl("Count", diceCount, 1, 10, themeColor, onDiceCountChange, Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        DiceSidesSelector(diceSides, themeColor, onDiceSidesChange, Modifier.weight(1f))
    }
    if (diceSides == 6) { Spacer(Modifier.height(12.dp)); SkewControl(skew, onSkewChange) }
    Spacer(Modifier.height(12.dp))
    AdvantageControl(advantage, disadvantage, onAdvantageChange, onDisadvantageChange)
    Spacer(Modifier.height(12.dp))
    ModifierControl(mod, onModChange)
}

// ═══════════════════════════════════════════════════════════════
// Fate dice config
// ═══════════════════════════════════════════════════════════════

@Composable
private fun FateConfigPanel(
    diceCount: Int, mod: Int, themeColor: Color,
    onDiceCountChange: (Int) -> Unit, onModChange: (Int) -> Unit,
) {
    NumberControl("Fate Dice", diceCount, 1, 10, themeColor, onDiceCountChange)
    Spacer(Modifier.height(10.dp))
    InfoBox("Fate dice have 3 faces: [+] Plus, [ ] Blank, [\u2212] Minus. Juice uses 2dF for Fate Checks.", themeColor)
    Spacer(Modifier.height(12.dp))
    ModifierControl(mod, onModChange)
}

// ═══════════════════════════════════════════════════════════════
// Ironsworn config panels
// ═══════════════════════════════════════════════════════════════

@Composable
private fun IronswornConfigPanel(
    rollType: String, stat: Int, adds: Int, progress: Int,
    odds: IronswornOdds, oracleDie: Int, momentum: Int, useBurn: Boolean,
    color: Color,
    onStatChange: (Int) -> Unit, onAddsChange: (Int) -> Unit,
    onProgressChange: (Int) -> Unit, onOddsChange: (IronswornOdds) -> Unit,
    onOracleDieChange: (Int) -> Unit, onMomentumChange: (Int) -> Unit,
    onUseBurnChange: (Boolean) -> Unit,
) {
    when (rollType) {
        "action" -> IronswornActionConfig(stat, adds, momentum, useBurn, color, onStatChange, onAddsChange, onMomentumChange, onUseBurnChange)
        "progress" -> IronswornProgressConfig(progress, color, onProgressChange)
        "oracle" -> IronswornOracleConfig(oracleDie, color, onOracleDieChange)
        "yesno" -> IronswornYesNoConfig(odds, color, onOddsChange)
        "cursed" -> IronswornCursedConfig(color)
    }
}

@Composable
private fun IronswornActionConfig(
    stat: Int, adds: Int, momentum: Int, useBurn: Boolean, color: Color,
    onStatChange: (Int) -> Unit, onAddsChange: (Int) -> Unit,
    onMomentumChange: (Int) -> Unit, onUseBurnChange: (Boolean) -> Unit,
) {
    Row {
        NumberControl("Stat", stat, 0, 5, color, onStatChange, Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        NumberControl("Adds", adds, 0, 10, color, onAddsChange, Modifier.weight(1f))
    }
    Spacer(Modifier.height(10.dp))
    MomentumBurnControl(useBurn, momentum, color, onUseBurnChange, onMomentumChange)
    Spacer(Modifier.height(10.dp))
    InfoBox("Roll 1d6 + Stat + Adds vs 2d10 challenge dice.\n" +
        "\u2022 Strong Hit: Beat both dice\n\u2022 Weak Hit: Beat one die\n" +
        "\u2022 Miss: Beat neither\n\u2022 Match: Both challenge dice same value", color)
}

@Composable
private fun IronswornProgressConfig(progress: Int, color: Color, onChange: (Int) -> Unit) {
    NumberControl("Progress Score", progress, 0, 10, color, onChange)
    Spacer(Modifier.height(10.dp))
    InfoBox("Compare your progress score (0-10) vs 2d10 challenge dice.\n" +
        "Used for Fulfill Your Vow, Reach a Milestone, etc.\n" +
        "\u2022 Strong Hit: Beat both dice\n\u2022 Weak Hit: Beat one die\n\u2022 Miss: Beat neither", color)
}

@Composable
private fun IronswornOracleConfig(oracleDie: Int, color: Color, onChange: (Int) -> Unit) {
    Text("Die Type", style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
    Spacer(Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(6 to "d6", 20 to "d20", 100 to "d100").forEach { (sides, label) ->
            val sel = oracleDie == sides
            Surface(
                onClick = { onChange(sides) },
                shape = RoundedCornerShape(8.dp),
                color = if (sel) color.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.25f),
                border = BorderStroke(
                    width = if (sel) 1.5.dp else 1.dp,
                    color = if (sel) color else ParchmentDark.copy(alpha = 0.2f),
                ),
                tonalElevation = 0.dp,
            ) {
                Text(label, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                    color = if (sel) color else Parchment)
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    InfoBox("Roll for Ironsworn/Starforged oracle tables.\n" +
        "\u2022 d6: Simple result tables (1-6)\n\u2022 d20: Character/creature oracles\n" +
        "\u2022 d100: Standard percentile oracles", color)
}

@Composable
private fun IronswornYesNoConfig(odds: IronswornOdds, color: Color, onChange: (IronswornOdds) -> Unit) {
    Text("Select Odds", style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
    Spacer(Modifier.height(8.dp))
    val oddsOptions = listOf(
        IronswornOdds.ALMOST_CERTAIN to Success,
        IronswornOdds.LIKELY to Color(0xFF8BC34A),
        IronswornOdds.FIFTY_FIFTY to Gold,
        IronswornOdds.UNLIKELY to Color(0xFFFF6B35),
        IronswornOdds.SMALL_CHANCE to Danger,
    )
    oddsOptions.forEach { (opt, c) ->
        val sel = odds == opt
        Spacer(Modifier.height(6.dp))
        Surface(
            onClick = { onChange(opt) },
            shape = RoundedCornerShape(8.dp),
            color = if (sel) c.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.15f),
            border = BorderStroke(
                width = if (sel) 1.5.dp else 1.dp,
                color = if (sel) c else ParchmentDark.copy(alpha = 0.2f),
            ),
            tonalElevation = 0.dp,
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(oddsDisplayName(opt), modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                    color = if (sel) c else Parchment)
                Surface(shape = RoundedCornerShape(4.dp), color = c.copy(alpha = 0.15f)) {
                    Text("Yes on ${oddsThreshold(opt)}+",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold, color = c)
                }
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    InfoBox("Ask the Oracle a yes/no question.\nRoll d100 and compare against the threshold based on odds.", color)
}

@Composable
private fun IronswornCursedConfig(color: Color) {
    val cursedColor = Color(0xFFca5cff)
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = cursedColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, cursedColor.copy(alpha = 0.3f)),
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.WarningAmber, null, tint = cursedColor, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Sundered Isles", style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold, color = cursedColor)
                Text("Cursed Die adds danger to oracle rolls",
                    style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
            }
        }
    }
    Spacer(Modifier.height(10.dp))
    InfoBox("Roll d100 for the oracle + d10 cursed die.\n" +
        "\u2022 10 on cursed die = Curse triggers!\n" +
        "\u2022 Draw from your curse deck or consult curse table.\n" +
        "\u2022 Used in Sundered Isles for supernatural peril.", cursedColor)
}

// ═══════════════════════════════════════════════════════════════
// Momentum Burn Control
// ═══════════════════════════════════════════════════════════════

@Composable
private fun MomentumBurnControl(
    enabled: Boolean, momentum: Int, color: Color,
    onToggle: (Boolean) -> Unit, onChange: (Int) -> Unit,
) {
    val mc = if (enabled) Gold else ParchmentDark
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (enabled) Gold.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, mc.copy(alpha = 0.3f)),
    ) {
        Column(Modifier.padding(10.dp)) {
            Surface(onClick = { onToggle(!enabled) }, shape = RoundedCornerShape(4.dp),
                color = Color.Transparent, tonalElevation = 0.dp) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (enabled) "\u2611" else "\u2610",
                        style = MaterialTheme.typography.bodyLarge, color = mc)
                    Spacer(Modifier.width(6.dp))
                    Text("Momentum Burn", style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold, color = mc)
                }
            }
            if (enabled) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {
                    CtrlBtn(Icons.Filled.Remove, momentum > -6, mc) { onChange(momentum - 1) }
                    Text("$momentum", modifier = Modifier.padding(horizontal = 12.dp).widthIn(min = 36.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = mc)
                    CtrlBtn(Icons.Filled.Add, momentum < 10, mc) { onChange(momentum + 1) }
                }
                Spacer(Modifier.height(6.dp))
                Text("Burn momentum to replace action score with momentum value.",
                    style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic, color = ParchmentDark)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Shared controls
// ═══════════════════════════════════════════════════════════════

@Composable
private fun NumberControl(
    label: String, value: Int, min: Int, max: Int, color: Color,
    onChange: (Int) -> Unit, modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.25f),
            border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
            tonalElevation = 0.dp,
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                CtrlBtn(Icons.Filled.Remove, value > min, color) { onChange(value - 1) }
                Text("$value", modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = color)
                CtrlBtn(Icons.Filled.Add, value < max, color) { onChange(value + 1) }
            }
        }
    }
}

@Composable
private fun CtrlBtn(icon: ImageVector, enabled: Boolean, color: Color, onClick: () -> Unit) {
    Surface(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(6.dp),
        color = Color.Transparent, tonalElevation = 0.dp) {
        Box(Modifier.padding(8.dp)) {
            Icon(icon, null, tint = if (enabled) color else ParchmentDark.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun DiceSidesSelector(
    selected: Int, color: Color, onChange: (Int) -> Unit, modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text("Die Type", style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.25f),
            border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
            tonalElevation = 0.dp,
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                diceSideOptions.forEach { (sides, label) ->
                    val sel = selected == sides
                    val chipColor = when (sides) {
                        4 -> Info; 6 -> Rust; 8 -> Success; 10 -> Rust
                        12 -> Mystic; 20 -> Gold; else -> Color(0xFFFF6B35)
                    }
                    Surface(onClick = { onChange(sides) }, shape = RoundedCornerShape(6.dp),
                        color = if (sel) chipColor.copy(alpha = 0.2f) else Color.Transparent,
                        tonalElevation = 0.dp) {
                        Text(label, modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            color = if (sel) chipColor else ParchmentDark)
                    }
                }
            }
        }
    }
}

@Composable
private fun SkewControl(skew: Int, onChange: (Int) -> Unit) {
    val (skewLabel, skewColor) = when {
        skew == 0 -> "No skew" to ParchmentDark
        skew > 0 -> "High (+$skew)" to Success
        else -> "Low ($skew)" to Danger
    }
    Column {
        Text("Skew", style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("\u25BC", style = MaterialTheme.typography.bodyMedium, color = Danger.copy(alpha = 0.6f))
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                for (i in -3..3) {
                    val sel = skew == i
                    Surface(onClick = { onChange(i) }, shape = RoundedCornerShape(4.dp),
                        color = if (sel) {
                            when { i > 0 -> Success.copy(alpha = 0.2f); i < 0 -> Danger.copy(alpha = 0.2f); else -> ParchmentDark.copy(alpha = 0.15f) }
                        } else Color.Transparent, tonalElevation = 0.dp) {
                        Text(if (i > 0) "+$i" else "$i",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            color = when { sel && i > 0 -> Success; sel && i < 0 -> Danger; sel -> ParchmentDark; else -> ParchmentDark.copy(alpha = 0.5f) })
                    }
                }
            }
            Text("\u25B2", style = MaterialTheme.typography.bodyMedium, color = Success.copy(alpha = 0.6f))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Surface(shape = RoundedCornerShape(4.dp), color = skewColor.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, skewColor.copy(alpha = 0.3f))) {
                Text(skewLabel, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = skewColor)
            }
        }
    }
}

@Composable
private fun AdvantageControl(
    adv: Boolean, dis: Boolean,
    onAdvChange: (Boolean) -> Unit, onDisChange: (Boolean) -> Unit,
) {
    Row {
        AdvChip("Advantage", adv, Success, Modifier.weight(1f)) { onAdvChange(!adv) }
        Spacer(Modifier.width(8.dp))
        AdvChip("Disadvantage", dis, Danger, Modifier.weight(1f)) { onDisChange(!dis) }
    }
}

@Composable
private fun AdvChip(label: String, selected: Boolean, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick, modifier = modifier, shape = RoundedCornerShape(8.dp),
        color = if (selected) color.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.25f),
        border = BorderStroke(width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) color else ParchmentDark.copy(alpha = 0.2f)),
        tonalElevation = 0.dp,
    ) {
        Box(Modifier.padding(vertical = 10.dp)) {
            Text(label, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) color else ParchmentDark, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ModifierControl(mod: Int, onChange: (Int) -> Unit) {
    val mc = when { mod > 0 -> Success; mod < 0 -> Danger; else -> ParchmentDark }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Modifier", style = MaterialTheme.typography.bodySmall, color = ParchmentDark)
        Spacer(Modifier.weight(1f))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black.copy(alpha = 0.25f),
            border = BorderStroke(1.dp, mc.copy(alpha = 0.3f)),
            tonalElevation = 0.dp,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CtrlBtn(Icons.Filled.Remove, mod > -20, mc) { onChange(mod - 1) }
                Text(if (mod >= 0) "+$mod" else "$mod",
                    modifier = Modifier.padding(horizontal = 4.dp).widthIn(min = 48.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = mc)
                CtrlBtn(Icons.Filled.Add, mod < 20, mc) { onChange(mod + 1) }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Roll preview panel
// ═══════════════════════════════════════════════════════════════

@Composable
private fun RollPreviewPanel(
    useIronsworn: Boolean, useFateDice: Boolean,
    diceCount: Int, diceSides: Int, mod: Int, skew: Int,
    advantage: Boolean, disadvantage: Boolean,
    ironswornRollType: String, ironswornStat: Int, ironswornAdds: Int,
    ironswornProgress: Int, yesNoOdds: IronswornOdds, oracleDieType: Int,
    useMomentumBurn: Boolean, momentum: Int, themeColor: Color,
) {
    val desc = buildString {
        when {
            useIronsworn -> when (ironswornRollType) {
                "action" -> { append("1d6"); val t = ironswornStat + ironswornAdds; if (t > 0) append("+$t"); append(" vs 2d10"); if (useMomentumBurn) append(" (M:$momentum)") }
                "progress" -> append("$ironswornProgress vs 2d10")
                "oracle" -> append("1d$oracleDieType")
                "yesno" -> append("d100 ${oddsDisplayName(yesNoOdds)}")
                "cursed" -> append("d100 + Cursed d10")
            }
            useFateDice -> { append("${diceCount}dF"); if (mod != 0) append(if (mod >= 0) "+$mod" else "$mod") }
            else -> { append("${diceCount}d$diceSides"); if (mod != 0) append(if (mod >= 0) "+$mod" else "$mod") }
        }
    }
    val tags = mutableListOf<Pair<String, Color>>()
    if (!useIronsworn && !useFateDice) {
        if (advantage) tags.add("ADV" to Success)
        if (disadvantage) tags.add("DIS" to Danger)
        if (skew != 0 && diceSides == 6) tags.add("SKEW ${if (skew > 0) "+$skew" else "$skew"}" to if (skew > 0) Success else Danger)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = themeColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, themeColor.copy(alpha = 0.2f)),
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("ROLL PREVIEW", style = MaterialTheme.typography.labelSmall, color = ParchmentDark)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Icon(
                    when { useIronsworn -> Icons.Filled.Shield; useFateDice -> Icons.Filled.AutoAwesome; else -> Icons.Filled.Casino },
                    null, tint = themeColor, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(12.dp))
                Text(desc, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = themeColor)
            }
            if (tags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    tags.forEach { (l, c) -> Tag(l, c) }
                }
            }
        }
    }
}

@Composable
private fun Tag(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(4.dp), color = color.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))) {
        Text(label, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
    }
}

// ═══════════════════════════════════════════════════════════════
// Info box (shared)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun InfoBox(content: String, color: Color = Info) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f)),
    ) {
        Row(Modifier.fillMaxWidth().padding(10.dp)) {
            Text(content, style = MaterialTheme.typography.bodySmall, color = Parchment.copy(alpha = 0.85f))
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Re-roll and Save row (shown after a result)
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ReRollAndSaveRow(onReRoll: () -> Unit, onSave: () -> Unit, themeColor: Color) {
    Row(Modifier.fillMaxWidth()) {
        Surface(
            onClick = onReRoll,
            shape = RoundedCornerShape(8.dp),
            color = themeColor.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, themeColor.copy(alpha = 0.4f)),
            tonalElevation = 0.dp,
        ) {
            Box(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Text("Re-roll", style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold, color = themeColor)
            }
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = Gold.copy(alpha = 0.2f), contentColor = Gold),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Save & Close", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Bottom action bar
// ═══════════════════════════════════════════════════════════════

@Composable
private fun BottomActionsBar(themeColor: Color, onCancel: () -> Unit, onRoll: () -> Unit) {
    Surface(
        color = Color.Black.copy(alpha = 0.2f),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(onClick = onCancel, shape = RoundedCornerShape(8.dp),
                color = Color.Transparent, tonalElevation = 0.dp) {
                Text("Cancel", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge, color = ParchmentDark)
            }
            Spacer(Modifier.weight(1f))
            Surface(
                onClick = onRoll,
                shape = RoundedCornerShape(10.dp),
                color = themeColor,
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Casino, null, tint = Color.Black, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Roll!", style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}
