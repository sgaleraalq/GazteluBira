package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.manager.PasswordManager
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.usecases.matches.GetTeamsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
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
    private val playersRepository: PlayersRepository,
    private val getPlayersUseCase: GetPlayersUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase,
) : ViewModel() {

    private val _responsiveUI = MutableStateFlow(false)
    val responsiveUI: StateFlow<Boolean> = _responsiveUI

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _playersStats = MutableStateFlow<List<PlayerStatsModel?>>(emptyList())
    val playersStats: StateFlow<List<PlayerStatsModel?>> = _playersStats

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _statSelected = MutableStateFlow(PERCENTAGE)
    val statSelected: StateFlow<StatType> = _statSelected

    private val _headerOpacity = MutableStateFlow(1f)
    val headerOpacity: StateFlow<Float> = _headerOpacity

    init {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            sharedPreferences.getCredentials()

            val playersListResult = withContext(Dispatchers.IO){
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
            }

            val playersStatsResult = withContext(Dispatchers.IO){
                getPlayersStatsUseCase(sharedPreferences.credentials.year.toString())
            }

            withContext(Dispatchers.IO){
                getTeamsUseCase(sharedPreferences.credentials.year.toString())
            }

            if (playersListResult && playersStatsResult) {
                val playersList = playersRepository.playersList.value
                val playersStats = playersRepository.playersStats.value
                initStats(playersList, playersStats)
            }
        }
    }

    fun onResponsiveUIChanged(isResponsive: Boolean) {
        _responsiveUI.value = isResponsive
    }

    private fun changeStatSelected(stat: StatType) {
        _statSelected.value = stat
    }

    private fun initStats(playersList: List<PlayerModel?>, playersStats: List<PlayerStatsModel?>) {
        viewModelScope.launch {
            if (playersStats.isNotEmpty()) {
                _playersStats.value = playersStats.map { playerStat ->
                    val player = playersList.find { player -> player?.ownReference == playerStat?.reference }
                    playerStat?.information = player
                    playerStat
                }.sortedByDescending { it?.percentage }
                _uiState.value = UIState.Success
            } else {
                _uiState.value = UIState.Error
            }
        }
    }

    fun sortPlayersBy(stat: StatType) {
        changeStatSelected(stat)
        val sortedList = _playersStats.value.sortedWith(
            compareByDescending<PlayerStatsModel?> {
                when (stat) {
                    PERCENTAGE -> it?.percentage
                    GOALS -> it?.goals
                    ASSISTS -> it?.assists
                    PENALTIES -> it?.penalties
                    CLEAN_SHEET -> it?.cleanSheet
                    GAMES_PLAYED -> it?.gamesPlayed
                }
            }.thenBy { it?.information?.name }
        )
        _playersStats.value = sortedList
    }


    fun onOpacityChanged(opacity: Float) {
        _headerOpacity.value = opacity
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

enum class StatType {
    PERCENTAGE,
    GOALS,
    ASSISTS,
    PENALTIES,
    CLEAN_SHEET,
    GAMES_PLAYED
}