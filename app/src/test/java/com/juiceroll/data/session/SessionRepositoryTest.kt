package com.juiceroll.data.session

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.juiceroll.domain.model.RollResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.io.createTempDir

class SessionRepositoryTest {

    private lateinit var repo: SessionRepositoryImpl
    private lateinit var testFile: File

    @Before
    fun setUp() {
        val tmpDir = createTempDir("juiceroll_test")
        testFile = File(tmpDir, "test.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create {
            testFile
        }
        repo = SessionRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        testFile.delete()
        testFile.parentFile?.delete()
    }

    // ── Helper ──

    private fun makeRoll(value: Int = 1): RollResult =
        RollResult.StandardRollResult(
            values = listOf(value),
            sides = 6,
            total = value,
        )

    // ═══════════════════════════════════════════════════════════════
    // CRUD Lifecycle
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun createAndListSession() = runBlocking {
        val session = repo.createSession("Test Session", notes = "My notes")
        assertNotNull("Session should have an id", session.id)
        assertEquals("Name should match", "Test Session", session.name)
        assertEquals("Notes should match", "My notes", session.notes)
        assertTrue("Created timestamp should be > 0", session.createdAt > 0)
        assertEquals("History should be empty", 0, session.history.size)

        val sessions = repo.sessions.first()
        assertEquals("Should list 1 session", 1, sessions.size)
        assertEquals("Listed session name should match", "Test Session", sessions[0].name)
    }

    @Test
    fun getSessionReturnsFullSession() = runBlocking {
        val created = repo.createSession("Get Test")
        val retrieved = repo.getSession(created.id)
        assertNotNull("Should retrieve session", retrieved)
        assertEquals("ID should match", created.id, retrieved!!.id)
        assertEquals("Name should match", "Get Test", retrieved.name)
    }

    @Test
    fun updateSession() = runBlocking {
        val session = repo.createSession("Original")
        val updated = session.copy(name = "Updated")
        repo.updateSession(updated)

        val retrieved = repo.getSession(session.id)
        assertNotNull("Should retrieve updated", retrieved)
        assertEquals("Name should be updated", "Updated", retrieved!!.name)
        assertTrue("lastAccessedAt should be updated", retrieved.lastAccessedAt >= session.lastAccessedAt)
    }

    @Test
    fun deleteSession() = runBlocking {
        val session = repo.createSession("To Delete")
        assertNotNull("Session should exist before delete", repo.getSession(session.id))

        repo.deleteSession(session.id)
        assertNull("Session should not exist after delete", repo.getSession(session.id))

        val sessions = repo.sessions.first()
        assertTrue("Sessions list should be empty", sessions.isEmpty())
    }

    @Test
    fun fullCrudLifecycle() = runBlocking {
        // Create
        val s1 = repo.createSession("Session 1")
        assertEquals(1, repo.sessions.first().size)

        // Read
        val read = repo.getSession(s1.id)
        assertNotNull(read)

        // Update
        repo.updateSession(s1.copy(name = "Session 1 Updated"))
        assertEquals("Session 1 Updated", repo.getSession(s1.id)!!.name)

        // Delete
        repo.deleteSession(s1.id)
        assertEquals(0, repo.sessions.first().size)
        assertNull(repo.getSession(s1.id))
    }

    // ═══════════════════════════════════════════════════════════════
    // Active Session Management
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun activeSessionFlow() = runBlocking {
        val s1 = repo.createSession("S1")
        val s2 = repo.createSession("S2")

        assertNull("No active session by default", repo.getActiveSessionId().first())

        repo.setActiveSession(s1.id)
        assertEquals("Active should be s1", s1.id, repo.getActiveSessionId().first())

        repo.setActiveSession(s2.id)
        assertEquals("Active should be s2", s2.id, repo.getActiveSessionId().first())

        repo.clearActiveSession()
        assertNull("Active should be cleared", repo.getActiveSessionId().first())
    }

    @Test
    fun deleteActiveSessionFallsBack() = runBlocking {
        val s1 = repo.createSession("S1")
        val s2 = repo.createSession("S2")
        repo.setActiveSession(s1.id)

        repo.deleteSession(s1.id)

        // After deleting active session, it should fall back to next session
        assertNotNull("Active should fallback to another session", repo.getActiveSessionId().first())
        assertEquals("Fallback should be s2", s2.id, repo.getActiveSessionId().first())
    }

    @Test
    fun deleteLastActiveSessionClearsActive() = runBlocking {
        val s1 = repo.createSession("S1")
        repo.setActiveSession(s1.id)
        repo.deleteSession(s1.id)
        assertNull("Active should be cleared when last session deleted", repo.getActiveSessionId().first())
    }

    // ═══════════════════════════════════════════════════════════════
    // 50-Session LRU Eviction
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun fiftySessionEviction() = runBlocking {
        // Create 50 sessions
        val sessions = (1..50).map { repo.createSession("Session $it") }
        assertEquals(50, repo.sessions.first().size)

        // Create 51st - should evict the oldest (Session 1, not active)
        val s51 = repo.createSession("Session 51")
        val listed = repo.sessions.first()
        assertEquals(50, listed.size)

        // Session 1 should be evicted
        assertNull("Session 1 should be evicted", repo.getSession(sessions[0].id))
        // Session 51 should exist
        assertNotNull("Session 51 should exist", repo.getSession(s51.id))
    }

    @Test
    fun activeSessionExemptFromEviction() = runBlocking {
        // Create 50 sessions, make the first one active
        val firstSession = repo.createSession("Active Session")
        repo.setActiveSession(firstSession.id)

        // Create 49 more sessions (total = 50)
        val others = (1..49).map { repo.createSession("Session $it") }
        assertEquals(50, repo.sessions.first().size)

        // Create 51st session - should evict oldest non-active
        val s51 = repo.createSession("Session 51")
        val listed = repo.sessions.first()
        assertEquals(50, listed.size)

        // First session (active) should NOT be evicted
        assertNotNull("Active session should be exempt", repo.getSession(firstSession.id))
        // Session 51 should exist
        assertNotNull("Session 51 should exist", repo.getSession(s51.id))
    }

    // ═══════════════════════════════════════════════════════════════
    // 500-Roll Hard Cap
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun fiveHundredRollCap() = runBlocking {
        val session = repo.createSession("Roll Test")

        // Add 500 rolls
        for (i in 1..500) {
            repo.addRollToSession(session.id, makeRoll(i))
        }

        var loaded = repo.getSession(session.id)!!
        assertEquals(500, loaded.history.size)
        assertEquals("Newest roll should be 500", 500, loaded.history.first().total)

        // Add one more - oldest should be dropped (roll 1)
        repo.addRollToSession(session.id, makeRoll(501))
        loaded = repo.getSession(session.id)!!
        assertEquals(500, loaded.history.size)
        assertEquals("Newest should be 501", 501, loaded.history.first().total)
    }

    @Test
    fun oldestFirstTrim() = runBlocking {
        val session = repo.createSession("Trim Test")

        // Add 500 rolls with sequential values
        for (i in 1..500) {
            repo.addRollToSession(session.id, makeRoll(i))
        }

        // Add 501st - oldest (500th entry) should be dropped
        repo.addRollToSession(session.id, makeRoll(501))

        val loaded = repo.getSession(session.id)!!
        assertEquals(500, loaded.history.size)

        // Newest prepended, so index 0 should be 501
        assertEquals(501, loaded.history[0].total)
    }

    // ═══════════════════════════════════════════════════════════════
    // Legacy rollHistory flow
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun rollHistoryFlowFromActiveSession() = runBlocking {
        val session = repo.createSession("History Test")
        repo.setActiveSession(session.id)

        repo.saveRoll(makeRoll(10))
        repo.saveRoll(makeRoll(20))

        val history = repo.rollHistory.first()
        assertEquals(2, history.size)
        assertEquals(20, history[0].total) // newest first
        assertEquals(10, history[1].total)
    }

    @Test
    fun clearHistoryClearsActiveSession() = runBlocking {
        val session = repo.createSession("Clear Test")
        repo.setActiveSession(session.id)

        repo.saveRoll(makeRoll(1))
        repo.saveRoll(makeRoll(2))
        assertEquals(2, repo.rollHistory.first().size)

        repo.clearHistory()
        assertEquals(0, repo.rollHistory.first().size)
    }

    // ═══════════════════════════════════════════════════════════════
    // getSessionRollHistory
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun getSessionRollHistoryBySessionId() = runBlocking {
        val s1 = repo.createSession("S1")
        val s2 = repo.createSession("S2")

        repo.addRollToSession(s1.id, makeRoll(1))
        repo.addRollToSession(s1.id, makeRoll(2))
        repo.addRollToSession(s2.id, makeRoll(3))

        assertEquals(2, repo.getSessionRollHistory(s1.id).size)
        assertEquals(1, repo.getSessionRollHistory(s2.id).size)
    }

    // ═══════════════════════════════════════════════════════════════
    // Export / Import
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun exportProducesValidJsonEnvelope() = runBlocking {
        val session = repo.createSession("Export Test")
        repo.addRollToSession(session.id, makeRoll(7))
        repo.addRollToSession(session.id, makeRoll(3))

        val exported = repo.exportSession(session.id)
        assertTrue("Export should be non-empty", exported.isNotEmpty())

        val json = Json { ignoreUnknownKeys = true }
        val map = json.decodeFromString<Map<String, String>>(exported)
        assertEquals("Version should be 1.0", "1.0", map["version"])
        assertEquals("Type should be session", "session", map["type"])
        assertTrue("Should contain session field", map.containsKey("session"))
        assertTrue("Should contain exportedAt field", map.containsKey("exportedAt"))
    }

    @Test
    fun importSessionRoundTrip() = runBlocking {
        // Create session with rolls
        val session = repo.createSession("Original Session")
        repo.addRollToSession(session.id, makeRoll(42))
        repo.addRollToSession(session.id, makeRoll(99))

        // Export
        val exported = repo.exportSession(session.id)

        // Import into a fresh repository
        val testDir2 = createTempDir("juiceroll_import")
        val importFile = File(testDir2, "import.preferences_pb")
        try {
            val ds2 = PreferenceDataStoreFactory.create { importFile }
            val repo2 = SessionRepositoryImpl(ds2)

            val imported = repo2.importSession(exported)
            assertNotNull("Import should succeed", imported)
            assertTrue("Imported name should have suffix", imported!!.name.contains("(imported)"))
            assertTrue("Imported ID should be different", imported.id != session.id)
            assertEquals("History count should match", 2, imported.history.size)
            // The original session var was captured before addRollToSession, so re-fetch for comparison
            val updatedSession = repo.getSession(session.id)!!
            assertEquals(
                "Roll values should match",
                updatedSession.history.map { it.total },
                imported.history.map { it.total },
            )
        } finally {
            importFile.delete()
            testDir2.delete()
        }
    }

    @Test
    fun importInvalidJsonReturnsNull() = runBlocking {
        assertNull("Bad version should return null", repo.importSession("""{"version":"0.5","type":"session","session":{}}"""))
        assertNull("Bad type should return null", repo.importSession("""{"version":"1.0","type":"not_session","session":{}}"""))
        assertNull("Invalid should return null", repo.importSession("not json"))
    }

    // ═══════════════════════════════════════════════════════════════
    // Roll History via addRollToSession
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun addRollToSessionPrependsNewest() = runBlocking {
        val session = repo.createSession("Prepend Test")
        repo.addRollToSession(session.id, makeRoll(1))
        repo.addRollToSession(session.id, makeRoll(2))

        val history = repo.getSessionRollHistory(session.id)
        assertEquals(2, history.size)
        // Newest first
        assertEquals(2, history[0].total)
        assertEquals(1, history[1].total)
    }

    @Test
    fun addRollToNonexistentSessionDoesNothing() = runBlocking {
        repo.addRollToSession("nonexistent", makeRoll(1))
        // Should not throw, just no-op
    }

    // ═══════════════════════════════════════════════════════════════
    // Migration
    // ═══════════════════════════════════════════════════════════════

    @Test
    fun migrateWithoutLegacyDoesNothing() = runBlocking {
        // No legacy data should exist
        repo.migrateLegacyHistory()
        assertEquals(0, repo.sessions.first().size)
    }
}
