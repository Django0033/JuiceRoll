package com.juiceroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juiceroll.data.session.SessionRepository
import com.juiceroll.data.session.SessionRepositoryImpl
import com.juiceroll.data.session.SessionSummary
import com.juiceroll.domain.model.RollResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionUiState(
    val rollHistory: List<RollResult> = emptyList(),
    val sessions: List<SessionSummary> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: SessionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Run legacy migration once on startup
            if (repository is SessionRepositoryImpl) {
                repository.migrateLegacyHistory()
            }

            // Observe roll history
            repository.rollHistory.collect { history ->
                _uiState.value = _uiState.value.copy(rollHistory = history)
            }
        }

        viewModelScope.launch {
            repository.sessions.collect { sessions ->
                _uiState.value = _uiState.value.copy(sessions = sessions)
            }
        }
    }

    fun saveRoll(roll: RollResult) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.saveRoll(roll)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
