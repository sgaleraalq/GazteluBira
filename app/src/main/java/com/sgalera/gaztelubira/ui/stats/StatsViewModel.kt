package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
import com.sgalera.gaztelubira.ui.manager.PasswordManager
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import com.sgalera.gaztelubira.ui.stats.StatType.ASSISTS
import com.sgalera.gaztelubira.ui.stats.StatType.CLEAN_SHEET
import com.sgalera.gaztelubira.ui.stats.StatType.GAMES_PLAYED
import com.sgalera.gaztelubira.ui.stats.StatType.GOALS
import com.sgalera.gaztelubira.ui.stats.StatType.PENALTIES
import com.sgalera.gaztelubira.ui.stats.StatType.PERCENTAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val passwordManager: PasswordManager,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    private val _playersStats = MutableStateFlow<List<PlayerStatsModel>>(emptyList())
    val playersStats: StateFlow<List<PlayerStatsModel>> = _playersStats

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    init {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            sharedPreferences.getCredentials()

            val players = withContext(Dispatchers.IO) {
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
            }

            if (players.isNotEmpty()) {
                initStats(players)
            }
        }
    }

    private fun initStats(players: List<PlayerModel?>) {
        viewModelScope.launch {

            val playersStats = withContext(Dispatchers.IO) {
                getPlayersStatsUseCase(sharedPreferences.credentials.year.toString())
            }

            if (playersStats != null) {
                _playersStats.value = playersStats.map { stat ->
                        val player = players.find { player -> player?.ownReference == stat.reference }
                        stat.information = player
                        stat
                    }.sortedByDescending { it.percentage }
            } else {
                _uiState.value = StatsUiState.Error
            }
        }
    }

    fun sortPlayersBy(stat: StatType, changeButtonColor: (StatType) -> Unit) {
        val sortedList = _playersStats.value.sortedWith(
            compareByDescending<PlayerStatsModel> {
                when (stat) {
                    PERCENTAGE -> it.percentage
                    GOALS -> it.goals
                    ASSISTS -> it.assists
                    PENALTIES -> it.penalties
                    CLEAN_SHEET -> it.cleanSheet
                    GAMES_PLAYED -> it.gamesPlayed
                }
            }.thenBy { it.information?.name }
        )
        _playersStats.value = sortedList
        changeButtonColor(stat)
    }


    fun adminLogIn(password: String): Boolean {
        val result = passwordManager.checkPassword(password)
        if (result) {
            _isAdmin.value = true
            sharedPreferences.manageAdminLogIn(true)
        }
        return result
    }

    fun adminLogOut() {
        _isAdmin.value = false
        sharedPreferences.manageAdminLogIn(false)
    }
}


sealed class StatsUiState {
    data object Loading : StatsUiState()
    data object Error : StatsUiState()
}

enum class StatType {
    PERCENTAGE,
    GOALS,
    ASSISTS,
    PENALTIES,
    CLEAN_SHEET,
    GAMES_PLAYED
}