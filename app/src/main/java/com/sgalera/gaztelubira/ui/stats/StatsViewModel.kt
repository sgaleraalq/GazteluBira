package com.sgalera.gaztelubira.ui.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerModelUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
import com.sgalera.gaztelubira.ui.manager.PasswordManager
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import com.sgalera.gaztelubira.ui.stats.StatType.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val playersRepository: PlayersRepository,
    private val sharedPreferences: SharedPreferences,
    private val passwordManager: PasswordManager,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase,
    private val getPlayerModelUseCase: GetPlayerModelUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    init {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            sharedPreferences.getCredentials()

            val players = withContext(Dispatchers.IO) {
                playersRepository.getPlayers(sharedPreferences.credentials.year.toString())
            }

            if (players.isNotEmpty()) {
                initStats()
                Log.i("StatsViewModel", "Loaded Players: $players")
            }
        }
    }

    private fun initStats() {
//        TODO
    }

    fun sortPlayersBy(stat: StatType, changeButtonColor: (StatType) -> Unit) {
        _uiState.value = when (val currentState = _uiState.value) {
            is StatsUiState.Success -> {
                val sortedList = currentState.playersStats.sortedWith(
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