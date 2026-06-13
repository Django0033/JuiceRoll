package com.juiceroll.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.juiceroll.domain.model.RollResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

interface SessionRepository {
    val rollHistory: Flow<List<RollResult>>
    suspend fun saveRoll(roll: RollResult)
    suspend fun clearHistory()
}

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    private companion object {
        val ROLL_HISTORY_KEY = stringPreferencesKey("roll_history")
    }

    private val json = Json { ignoreUnknownKeys = true }

    override val rollHistory: Flow<List<RollResult>> = dataStore.data.map { prefs ->
        val raw = prefs[ROLL_HISTORY_KEY] ?: return@map emptyList()
        json.decodeFromString<List<RollResult>>(raw)
    }

    override suspend fun saveRoll(roll: RollResult) {
        dataStore.edit { prefs ->
            val current = prefs[ROLL_HISTORY_KEY] ?: "[]"
            val list = json.decodeFromString<List<RollResult>>(current)
            prefs[ROLL_HISTORY_KEY] = json.encodeToString(list + roll)
        }
    }

    override suspend fun clearHistory() {
        dataStore.edit { prefs ->
            prefs.remove(ROLL_HISTORY_KEY)
        }
    }
}
