package com.juiceroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juiceroll.data.session.SessionRepository
import com.juiceroll.domain.model.RollResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SessionUiState(
    val rollHistory: List<RollResult> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.rollHistory.collect { history ->
                _uiState.value = _uiState.value.copy(rollHistory = history)
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
