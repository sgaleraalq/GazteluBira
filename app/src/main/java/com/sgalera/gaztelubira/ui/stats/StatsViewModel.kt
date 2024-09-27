package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.GetPlayersStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerModelUseCase
import com.sgalera.gaztelubira.ui.manager.PasswordManager
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val passwordManager: PasswordManager,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase,
    private val getPlayerModelUseCase: GetPlayerModelUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    fun initAdmin(admin: Boolean) {
        _isAdmin.value = admin
    }

    init {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    getPlayersStatsUseCase(sharedPreferences.credentials.year.toString())
                }

                result?.let { playersList ->
                    var updatedPlayers = playersList.map { player ->
                        async {
                            if (player.reference != null) {
                                val playerModel = getPlayerModelUseCase(player.reference)
                                player.copy(information = playerModel)
                            } else {
                                player
                            }
                        }
                    }.awaitAll()

                    updatedPlayers = updatedPlayers.sortedByDescending { it.percentage }
                    _uiState.value = StatsUiState.Success(updatedPlayers, updatedPlayers.firstOrNull())

                } ?: run {
                    _uiState.value = StatsUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = StatsUiState.Error
            }
        }
    }

    fun sortPlayersBy(stat: StatType, changeButtonColor: (StatType) -> Unit) {
        _uiState.value = when (val currentState = _uiState.value) {
            is StatsUiState.Success -> {
                val sortedList = when (stat) {
                    StatType.PERCENTAGE -> currentState.playersStats.sortedByDescending { it.percentage }
                    StatType.GOALS -> currentState.playersStats.sortedByDescending { it.goals }
                    StatType.ASSISTS -> currentState.playersStats.sortedByDescending { it.assists }
                    StatType.PENALTIES -> currentState.playersStats.sortedByDescending { it.penalties }
                    StatType.CLEAN_SHEET -> currentState.playersStats.sortedByDescending { it.cleanSheet }
                    StatType.GAMES_PLAYED -> currentState.playersStats.sortedByDescending { it.gamesPlayed }
                }
                StatsUiState.Success(sortedList, sortedList.firstOrNull())
            }
            else -> currentState
        }
        changeButtonColor(stat)
    }

    fun adminLogIn(password: String): Boolean {
        val result = passwordManager.checkPassword(password)
        if (result) {
            _isAdmin.value = true
            sharedPreferences.adminLogIn()
        }
        return result
    }

    fun adminLogOut() {
        _isAdmin.value = false
        sharedPreferences.adminLogOut()
    }
}


sealed class StatsUiState {
    data object Loading : StatsUiState()
    data object Error : StatsUiState()
    data class Success(val playersStats: List<PlayerStatsModel>, val champion: PlayerStatsModel?) : StatsUiState()
}

enum class StatType{
    PERCENTAGE,
    GOALS,
    ASSISTS,
    PENALTIES,
    CLEAN_SHEET,
    GAMES_PLAYED
}