package com.juiceroll.data.session

import com.juiceroll.domain.model.RollResult
import kotlinx.serialization.Serializable

/**
 * Represents a play session with roll history and state.
 * All mutations return a new copy (immutability pattern).
 *
 * Matching the Flutter Session model from example/lib/models/session.dart.
 */
@Serializable
data class Session(
    val id: String,
    val name: String,
    val createdAt: Long,
    val lastAccessedAt: Long,
    val notes: String? = null,

    // ── Stateful presets ──
    val wildernessEnvironmentRow: Int? = null,
    val wildernessTypeRow: Int? = null,
    val wildernessIsLost: Boolean = false,
    val dungeonIsEntering: Boolean = true,
    val dungeonIsTwoPassMode: Boolean = false,
    val twoPassHasFirstDoubles: Boolean = false,

    // ── Dice dialog state ──
    val diceDialogMode: Int = 0,
    val diceDialogIronswornRollType: String = "action",
    val diceDialogOracleDieType: Int = 100,

    // ── Settings ──
    val maxRollsPerSession: Int? = null,

    // ── History ──
    val history: List<RollResult> = emptyList(),
) {
    val rollCount: Int get() = history.size

    fun copyWith(
        id: String? = null,
        name: String? = null,
        createdAt: Long? = null,
        lastAccessedAt: Long? = null,
        notes: String? = null,
        wildernessEnvironmentRow: Any? = unspecified,
        wildernessTypeRow: Any? = unspecified,
        wildernessIsLost: Boolean? = null,
        dungeonIsEntering: Boolean? = null,
        dungeonIsTwoPassMode: Boolean? = null,
        twoPassHasFirstDoubles: Boolean? = null,
        diceDialogMode: Int? = null,
        diceDialogIronswornRollType: String? = null,
        diceDialogOracleDieType: Int? = null,
        maxRollsPerSession: Any? = unspecified,
        clearMaxRollsPerSession: Boolean = false,
        history: List<RollResult>? = null,
    ): Session {
        return Session(
            id = id ?: this.id,
            name = name ?: this.name,
            createdAt = createdAt ?: this.createdAt,
            lastAccessedAt = lastAccessedAt ?: this.lastAccessedAt,
            notes = notes ?: this.notes,
            wildernessEnvironmentRow = if (wildernessEnvironmentRow === unspecified) this.wildernessEnvironmentRow else wildernessEnvironmentRow as Int?,
            wildernessTypeRow = if (wildernessTypeRow === unspecified) this.wildernessTypeRow else wildernessTypeRow as Int?,
            wildernessIsLost = wildernessIsLost ?: this.wildernessIsLost,
            dungeonIsEntering = dungeonIsEntering ?: this.dungeonIsEntering,
            dungeonIsTwoPassMode = dungeonIsTwoPassMode ?: this.dungeonIsTwoPassMode,
            twoPassHasFirstDoubles = twoPassHasFirstDoubles ?: this.twoPassHasFirstDoubles,
            diceDialogMode = diceDialogMode ?: this.diceDialogMode,
            diceDialogIronswornRollType = diceDialogIronswornRollType ?: this.diceDialogIronswornRollType,
            diceDialogOracleDieType = diceDialogOracleDieType ?: this.diceDialogOracleDieType,
            maxRollsPerSession = if (clearMaxRollsPerSession) null
                else if (maxRollsPerSession === unspecified) this.maxRollsPerSession
                else maxRollsPerSession as Int?,
            history = history ?: this.history,
        )
    }

    companion object {
        /** Sentinel object for nullable field diffing in copyWith. */
        private val unspecified = Any()
    }
}

/**
 * Lightweight session metadata for list display (no history).
 */
@Serializable
data class SessionSummary(
    val id: String,
    val name: String,
    val createdAt: Long,
    val lastAccessedAt: Long,
    val rollCount: Int,
)
