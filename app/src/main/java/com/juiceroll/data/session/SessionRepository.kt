package com.juiceroll.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.juiceroll.domain.model.RollResult
import com.juiceroll.domain.model.rollResultJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

// ── Interface ──

interface SessionRepository {
    // Legacy single-session history (preserved for backward compat)
    val rollHistory: Flow<List<RollResult>>
    suspend fun saveRoll(roll: RollResult)
    suspend fun clearHistory()

    // Multi-session CRUD
    val sessions: Flow<List<SessionSummary>>
    suspend fun createSession(name: String, notes: String? = null): Session
    suspend fun getSession(id: String): Session?
    suspend fun updateSession(session: Session)
    suspend fun deleteSession(id: String)

    // Active session management
    fun getActiveSessionId(): Flow<String?>
    suspend fun setActiveSession(id: String)
    suspend fun clearActiveSession()

    // Per-session history management
    suspend fun addRollToSession(sessionId: String, roll: RollResult)
    suspend fun getSessionRollHistory(sessionId: String): List<RollResult>

    // Import / Export
    suspend fun exportSession(id: String): String
    suspend fun importSession(json: String): Session?
}

// ── Implementation ──

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SessionRepository {

    private companion object {
        // Legacy key (for migration)
        val ROLL_HISTORY_KEY = stringPreferencesKey("roll_history")

        // New multi-session keys
        val SESSION_INDEX_KEY = stringPreferencesKey("session_index")
        val ACTIVE_SESSION_KEY = stringPreferencesKey("active_session_id")

        // Per-session key prefix: "session_{id}"
        fun sessionKey(id: String): Preferences.Key<String> =
            stringPreferencesKey("session_$id")

        const val MAX_SESSIONS = 50
        const val MAX_ROLLS_PER_SESSION = 500
        const val EXPORT_VERSION = "1.0"
    }

    // ── Sessions flow (metadata-only) ──

    override val sessions: Flow<List<SessionSummary>> = dataStore.data.map { prefs ->
        val raw = prefs[SESSION_INDEX_KEY] ?: return@map emptyList()
        parseSessionIndex(raw)
    }

    // ── Legacy rollHistory flow (reads from active session) ──

    override val rollHistory: Flow<List<RollResult>> = getActiveSessionId().flatMapLatest { activeId ->
        dataStore.data.map { prefs ->
            if (activeId == null) return@map emptyList<RollResult>()
            val raw = prefs[sessionKey(activeId)] ?: return@map emptyList<RollResult>()
            parseSessionFromJson(raw)?.history ?: emptyList()
        }
    }

    // ── Active session management ──

    override fun getActiveSessionId(): Flow<String?> = dataStore.data.map { prefs ->
        prefs[ACTIVE_SESSION_KEY]
    }

    override suspend fun setActiveSession(id: String) {
        dataStore.edit { prefs -> prefs[ACTIVE_SESSION_KEY] = id }
    }

    override suspend fun clearActiveSession() {
        dataStore.edit { prefs -> prefs.remove(ACTIVE_SESSION_KEY) }
    }

    // ── CRUD operations ──

    override suspend fun createSession(name: String, notes: String?): Session {
        val now = System.currentTimeMillis()
        val id = "${now}_${name.hashCode()}"
        val session = Session(
            id = id,
            name = name,
            createdAt = now,
            lastAccessedAt = now,
            notes = notes,
        )
        saveSession(session)
        return session
    }

    override suspend fun getSession(id: String): Session? {
        val raw = dataStore.data.first()[sessionKey(id)] ?: return null
        return parseSessionFromJson(raw)
    }

    override suspend fun updateSession(session: Session) {
        saveSession(session.copy(lastAccessedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteSession(id: String) {
        dataStore.edit { prefs ->
            prefs.remove(sessionKey(id))
            // Remove from index
            val raw = prefs[SESSION_INDEX_KEY] ?: return@edit
            val list = parseSessionIndex(raw).toMutableList()
            list.removeAll { it.id == id }
            prefs[SESSION_INDEX_KEY] = rollResultJson.encodeToString(list)
            // If deleted was active, switch or clear
            val activeId = prefs[ACTIVE_SESSION_KEY]
            if (activeId == id) {
                if (list.isNotEmpty()) prefs[ACTIVE_SESSION_KEY] = list.first().id
                else prefs.remove(ACTIVE_SESSION_KEY)
            }
        }
    }

    // ── Legacy operations — delegate to active session ──

    override suspend fun saveRoll(roll: RollResult) {
        val activeId = dataStore.data.first()[ACTIVE_SESSION_KEY]
        if (activeId != null) addRollToSession(activeId, roll)
    }

    override suspend fun clearHistory() {
        val activeId = dataStore.data.first()[ACTIVE_SESSION_KEY] ?: return
        val session = getSession(activeId) ?: return
        updateSession(session.copy(history = emptyList()))
    }

    // ── Per-session history management ──

    override suspend fun addRollToSession(sessionId: String, roll: RollResult) {
        val session = getSession(sessionId) ?: return
        val newHistory = (listOf(roll) + session.history)
            .take(MAX_ROLLS_PER_SESSION)
        saveSession(session.copy(
            history = newHistory,
            lastAccessedAt = System.currentTimeMillis(),
        ))
    }

    override suspend fun getSessionRollHistory(sessionId: String): List<RollResult> {
        return getSession(sessionId)?.history ?: emptyList()
    }

    // ── Import / Export ──

    override suspend fun exportSession(id: String): String {
        val session = getSession(id) ?: throw IllegalArgumentException("Session not found: $id")
        val exportMap = mapOf(
            "version" to EXPORT_VERSION,
            "exportedAt" to System.currentTimeMillis().toString(),
            "type" to "session",
            "session" to rollResultJson.encodeToString(session),
        )
        return rollResultJson.encodeToString(exportMap)
    }

    override suspend fun importSession(json: String): Session? {
        return try {
            val map = rollResultJson.decodeFromString<Map<String, JsonElement>>(json)
            if (map["version"]?.jsonPrimitive?.content != EXPORT_VERSION) return null
            if (map["type"]?.jsonPrimitive?.content != "session") return null

            val sessionJsonStr = map["session"]?.jsonPrimitive?.content ?: return null
            val imported = parseSessionFromJson(sessionJsonStr) ?: return null

            val now = System.currentTimeMillis()
            val newSession = imported.copy(
                id = "${now}_imported_${imported.name.hashCode()}",
                name = "${imported.name} (imported)",
                createdAt = now,
                lastAccessedAt = now,
            )

            saveSession(newSession)
            newSession
        } catch (e: Exception) {
            null
        }
    }

    // ── Migration helper ──

    /**
     * Migrate legacy roll_history key to a new session.
     * Should be called once on app startup after the DataStore is available.
     */
    suspend fun migrateLegacyHistory() {
        dataStore.edit { prefs ->
            val legacyRaw = prefs[ROLL_HISTORY_KEY] ?: return@edit
            if (legacyRaw == "[]" || legacyRaw.isEmpty()) {
                prefs.remove(ROLL_HISTORY_KEY)
                return@edit
            }
            val legacyRolls = try {
                rollResultJson.decodeFromString<List<RollResult>>(legacyRaw)
            } catch (e: Exception) {
                return@edit
            }
            if (legacyRolls.isEmpty()) return@edit

            val now = System.currentTimeMillis()
            val session = Session(
                id = "${now}_legacy_import",
                name = "Imported (Legacy)",
                createdAt = now,
                lastAccessedAt = now,
                history = legacyRolls.take(MAX_ROLLS_PER_SESSION),
            )
            val jsonStr = rollResultJson.encodeToString(session)
            prefs[sessionKey(session.id)] = jsonStr

            // Update index
            val indexRaw = prefs[SESSION_INDEX_KEY] ?: "[]"
            val list = parseSessionIndex(indexRaw).toMutableList()
            list.add(0, SessionSummary(
                id = session.id,
                name = session.name,
                createdAt = now,
                lastAccessedAt = now,
                rollCount = session.rollCount,
            ))
            prefs[SESSION_INDEX_KEY] = rollResultJson.encodeToString(list)
            prefs[ACTIVE_SESSION_KEY] = session.id

            // Remove legacy key
            prefs.remove(ROLL_HISTORY_KEY)
        }
    }

    // ── Internal helpers ──

    /**
     * Save session: writes full data under its key, updates index, enforces LRU.
     */
    private suspend fun saveSession(session: Session) {
        val jsonStr = rollResultJson.encodeToString(session)

        dataStore.edit { prefs ->
            // 1. Write full session
            prefs[sessionKey(session.id)] = jsonStr

            // 2. Update index with LRU eviction
            val raw = prefs[SESSION_INDEX_KEY] ?: "[]"
            val activeId = prefs[ACTIVE_SESSION_KEY]
            val evictedIds = mutableListOf<String>()
            val list = updateIndexAndEvict(raw, session, activeId, evictedIds)
            prefs[SESSION_INDEX_KEY] = rollResultJson.encodeToString(list)

            // 3. Remove evicted session data
            evictedIds.forEach { prefs.remove(sessionKey(it)) }
        }
    }

    private fun updateIndexAndEvict(
        raw: String,
        session: Session,
        activeId: String?,
        evictedIds: MutableList<String>,
    ): List<SessionSummary> {
        val list = parseSessionIndex(raw).toMutableList()

        // Remove existing entry for this session
        list.removeAll { it.id == session.id }

        // Add updated entry at position 0 (most recently accessed)
        list.add(0, SessionSummary(
            id = session.id,
            name = session.name,
            createdAt = session.createdAt,
            lastAccessedAt = session.lastAccessedAt,
            rollCount = session.rollCount,
        ))

        // LRU eviction: active session is exempt
        val toEvict = mutableListOf<SessionSummary>()
        while (list.size > MAX_SESSIONS) {
            val candidate = list.removeLast()
            if (candidate.id == activeId) {
                // Don't evict active — keep it, move to front and re-check
                list.add(0, candidate)
            } else {
                toEvict.add(candidate)
            }
        }
        toEvict.forEach { evictedIds.add(it.id) }

        return list
    }

    private fun parseSessionIndex(raw: String): List<SessionSummary> {
        return try {
            rollResultJson.decodeFromString<List<SessionSummary>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseSessionFromJson(raw: String): Session? {
        return try {
            rollResultJson.decodeFromString<Session>(raw)
        } catch (e: Exception) {
            null
        }
    }
}
