package com.juiceroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juiceroll.data.session.Session
import com.juiceroll.data.session.SessionRepository
import com.juiceroll.data.session.SessionSummary
import com.juiceroll.domain.model.DialogGridState
import com.juiceroll.domain.model.RollResult
import com.juiceroll.domain.model.WildernessState
import com.juiceroll.generator.PresetRegistry
import kotlinx.coroutines.flow.first
import com.juiceroll.generator.challenge.ChallengeGenerator
import com.juiceroll.generator.challenge.DetailsGenerator
import com.juiceroll.generator.challenge.PayThePriceGenerator
import com.juiceroll.generator.character.DialogGenerator
import com.juiceroll.generator.character.ExtendedNpcConversationGenerator
import com.juiceroll.generator.character.NameGenerator
import com.juiceroll.generator.character.NpcActionGenerator
import com.juiceroll.generator.flavor.AbstractIconsGenerator
import com.juiceroll.generator.flavor.ImmersionGenerator
import com.juiceroll.generator.flavor.LocationGenerator
import com.juiceroll.generator.flavor.MonsterEncounterGenerator
import com.juiceroll.generator.oracle.DiscoverMeaningGenerator
import com.juiceroll.generator.oracle.ExpectationCheckGenerator
import com.juiceroll.generator.oracle.FateCheckGenerator
import com.juiceroll.generator.oracle.InterruptPlotPointGenerator
import com.juiceroll.generator.oracle.NextSceneGenerator
import com.juiceroll.generator.oracle.RandomEventGenerator
import com.juiceroll.generator.oracle.ScaleGenerator
import com.juiceroll.generator.world.DungeonGenerator
import com.juiceroll.generator.world.ObjectTreasureGenerator
import com.juiceroll.generator.world.QuestGenerator
import com.juiceroll.generator.world.SettlementGenerator
import com.juiceroll.generator.world.WildernessGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main ViewModel for the HomeScreen.
 *
 * Owns all generators via PresetRegistry, manages session state,
 * roll history, dungeon/wilderness tracking, and dice dialog state.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val presetRegistry: PresetRegistry,
) : ViewModel() {

    // ── Session state ──

    val sessions: StateFlow<List<com.juiceroll.data.session.SessionSummary>> =
        sessionRepository.sessions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeSession = MutableStateFlow<Session?>(null)
    val activeSession: StateFlow<Session?> = _activeSession.asStateFlow()

    private val _rollHistory = MutableStateFlow<List<RollResult>>(emptyList())
    val rollHistory: StateFlow<List<RollResult>> = _rollHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ── Dice dialog state ──

    private val _diceDialogMode = MutableStateFlow(0)
    val diceDialogMode: StateFlow<Int> = _diceDialogMode.asStateFlow()

    private val _diceDialogIronswornType = MutableStateFlow("action")
    val diceDialogIronswornType: StateFlow<String> = _diceDialogIronswornType.asStateFlow()

    private val _diceDialogOracleDie = MutableStateFlow(100)
    val diceDialogOracleDie: StateFlow<Int> = _diceDialogOracleDie.asStateFlow()

    // ── Dungeon state ──

    private val _dungeonPhase = MutableStateFlow(true) // true = entering, false = exploring
    val dungeonPhase: StateFlow<Boolean> = _dungeonPhase.asStateFlow()

    private val _dungeonTwoPassMode = MutableStateFlow(false)
    val dungeonTwoPassMode: StateFlow<Boolean> = _dungeonTwoPassMode.asStateFlow()

    private val _twoPassHasFirstDoubles = MutableStateFlow(false)
    val twoPassHasFirstDoubles: StateFlow<Boolean> = _twoPassHasFirstDoubles.asStateFlow()

    // ── Wilderness state ──

    private val _wildernessState = MutableStateFlow<WildernessState?>(null)
    val wildernessState: StateFlow<WildernessState?> = _wildernessState.asStateFlow()

    // ── Dialog Grid state ──

    private val _dialogGridState = MutableStateFlow(DialogGridState())
    val dialogGridState: StateFlow<DialogGridState> = _dialogGridState.asStateFlow()

    // ── Generator accessors ──

    val fateCheck: FateCheckGenerator get() = presetRegistry.fateCheck
    val randomEvent: RandomEventGenerator get() = presetRegistry.randomEvent
    val nextScene: NextSceneGenerator get() = presetRegistry.nextScene
    val expectationCheck: ExpectationCheckGenerator get() = presetRegistry.expectationCheck
    val discoverMeaning: DiscoverMeaningGenerator get() = presetRegistry.discoverMeaning
    val interruptPlotPoint: InterruptPlotPointGenerator get() = presetRegistry.interruptPlotPoint
    val scale: ScaleGenerator get() = presetRegistry.scale
    val npcAction: NpcActionGenerator get() = presetRegistry.npcAction
    val dialog: DialogGenerator get() = presetRegistry.dialog
    val extendedNpcConversation: ExtendedNpcConversationGenerator get() = presetRegistry.extendedNpcConversation
    val nameGenerator: NameGenerator get() = presetRegistry.nameGenerator
    val settlement: SettlementGenerator get() = presetRegistry.settlement
    val objectTreasure: ObjectTreasureGenerator get() = presetRegistry.objectTreasure
    val quest: QuestGenerator get() = presetRegistry.quest
    val details: DetailsGenerator get() = presetRegistry.details
    val immersion: ImmersionGenerator get() = presetRegistry.immersion
    val challenge: ChallengeGenerator get() = presetRegistry.challenge
    val payThePrice: PayThePriceGenerator get() = presetRegistry.payThePrice
    val dungeon: DungeonGenerator get() = presetRegistry.dungeon
    val wilderness: WildernessGenerator get() = presetRegistry.wilderness
    val monsterEncounter: MonsterEncounterGenerator get() = presetRegistry.monsterEncounter
    val location: LocationGenerator get() = presetRegistry.location
    val abstractIcons: AbstractIconsGenerator get() = presetRegistry.abstractIcons

    // ── Initialization ──

    init {
        loadActiveSession()
    }

    fun loadActiveSession() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val activeId = sessionRepository.getActiveSessionId().first()
                if (activeId != null) {
                    val session = sessionRepository.getSession(activeId)
                    loadSessionState(session)
                } else {
                    // No active session — create default
                    val session = sessionRepository.createSession("Default")
                    sessionRepository.setActiveSession(session.id)
                    _activeSession.value = session
                    _rollHistory.value = emptyList()
                }
            } catch (_: Exception) {
                // Session may not exist yet
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── History management ──

    fun addToHistory(result: RollResult) {
        viewModelScope.launch {
            try {
                _rollHistory.value = listOf(result) + _rollHistory.value
                _activeSession.value?.let { session ->
                    sessionRepository.addRollToSession(session.id, result)
                }
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Failed to save roll: ${e.message}", e)
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            _rollHistory.value = emptyList()
            _activeSession.value?.let { session ->
                sessionRepository.updateSession(session.copy(history = emptyList()))
            }
        }
    }

    // ── Session management ──

    fun createSession(name: String, notes: String? = null) {
        viewModelScope.launch {
            val session = sessionRepository.createSession(name, notes)
            sessionRepository.setActiveSession(session.id)
            _activeSession.value = session
            _rollHistory.value = emptyList()
            resetDungeonState()
            resetWildernessState()
        }
    }

    fun switchSessionById(sessionId: String) {
        viewModelScope.launch {
            sessionRepository.setActiveSession(sessionId)
            val fullSession = sessionRepository.getSession(sessionId)
            loadSessionState(fullSession)
        }
    }

    private fun loadSessionState(session: Session?) {
        _activeSession.value = session
        _rollHistory.value = session?.history ?: emptyList()

        session?.let { s ->
            _dungeonPhase.value = s.dungeonIsEntering
            _dungeonTwoPassMode.value = s.dungeonIsTwoPassMode
            _twoPassHasFirstDoubles.value = s.twoPassHasFirstDoubles
            _diceDialogMode.value = s.diceDialogMode
            _diceDialogIronswornType.value = s.diceDialogIronswornRollType
            _diceDialogOracleDie.value = s.diceDialogOracleDieType

            if (s.wildernessEnvironmentRow != null) {
                _wildernessState.value = WildernessState(
                    environmentRow = s.wildernessEnvironmentRow,
                    typeRow = s.wildernessTypeRow ?: s.wildernessEnvironmentRow,
                    isLost = s.wildernessIsLost,
                )
            } else {
                _wildernessState.value = null
            }
        }
    }

    fun switchSession(session: SessionSummary) {
        viewModelScope.launch {
            sessionRepository.setActiveSession(session.id)
            val fullSession = sessionRepository.getSession(session.id)
            loadSessionState(fullSession)
        }
    }

    fun deleteSession(id: String) {
        viewModelScope.launch {
            val wasActive = _activeSession.value?.id == id
            sessionRepository.deleteSession(id)
            if (wasActive) {
                loadActiveSession()
            }
        }
    }

    fun updateSession(id: String, name: String? = null, notes: String? = null) {
        viewModelScope.launch {
            val session = sessionRepository.getSession(id) ?: return@launch
            val updated = session.copy(name = name ?: session.name, notes = notes ?: session.notes)
            sessionRepository.updateSession(updated)
            if (_activeSession.value?.id == id) {
                _activeSession.value = updated
            }
        }
    }

    // ── Session state persistence helpers ──

    private suspend fun persistSessionState() {
        _activeSession.value?.let { session ->
            sessionRepository.updateSession(
                session.copy(
                    wildernessEnvironmentRow = _wildernessState.value?.environmentRow,
                    wildernessTypeRow = _wildernessState.value?.typeRow,
                    wildernessIsLost = _wildernessState.value?.isLost ?: false,
                    dungeonIsEntering = _dungeonPhase.value,
                    dungeonIsTwoPassMode = _dungeonTwoPassMode.value,
                    twoPassHasFirstDoubles = _twoPassHasFirstDoubles.value,
                    diceDialogMode = _diceDialogMode.value,
                    diceDialogIronswornRollType = _diceDialogIronswornType.value,
                    diceDialogOracleDieType = _diceDialogOracleDie.value,
                ),
            )
        }
    }

    // ── Dice dialog state management ──

    fun updateDiceMode(mode: Int) {
        _diceDialogMode.value = mode
        viewModelScope.launch { persistSessionState() }
    }

    fun updateDiceIronswornType(type: String) {
        _diceDialogIronswornType.value = type
        viewModelScope.launch { persistSessionState() }
    }

    fun updateDiceOracleDie(die: Int) {
        _diceDialogOracleDie.value = die
        viewModelScope.launch { persistSessionState() }
    }

    fun updateDiceState(mode: Int, ironswornType: String, oracleDie: Int) {
        _diceDialogMode.value = mode
        _diceDialogIronswornType.value = ironswornType
        _diceDialogOracleDie.value = oracleDie
        viewModelScope.launch { persistSessionState() }
    }

    // ── Dungeon state management ──

    fun setDungeonPhase(entering: Boolean) {
        _dungeonPhase.value = entering
        viewModelScope.launch { persistSessionState() }
    }

    fun setDungeonTwoPassMode(twoPass: Boolean) {
        _dungeonTwoPassMode.value = twoPass
        viewModelScope.launch { persistSessionState() }
    }

    fun setTwoPassFirstDoubles(hasFirst: Boolean) {
        _twoPassHasFirstDoubles.value = hasFirst
        viewModelScope.launch { persistSessionState() }
    }

    private fun resetDungeonState() {
        _dungeonPhase.value = true
        _dungeonTwoPassMode.value = false
        _twoPassHasFirstDoubles.value = false
    }

    // ── Wilderness state management ──

    fun updateWildernessState(state: WildernessState?) {
        _wildernessState.value = state
        viewModelScope.launch { persistSessionState() }
    }

    fun resetWildernessState() {
        _wildernessState.value = null
        viewModelScope.launch { persistSessionState() }
    }

    // ── Dialog Grid state management ──

    fun updateDialogGridState(state: DialogGridState) {
        _dialogGridState.value = state
    }

    // ── Quick roll methods ──

    fun quickRollScale() {
        addToHistory(scale.roll())
    }

    fun quickRollInterruptPlotPoint() {
        addToHistory(interruptPlotPoint.generate())
    }

    fun quickRollDiscoverMeaning() {
        addToHistory(discoverMeaning.generate())
    }

    fun quickRollQuest() {
        addToHistory(quest.generate())
    }
}
