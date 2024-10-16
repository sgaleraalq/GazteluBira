package com.sgalera.gaztelubira.ui.player_compare.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerStatsUseCase
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ComparePlayersViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val playersRepository: PlayersRepository,
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _playerOneStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerOneStats: StateFlow<PlayerStatsModel?> = _playerOneStats

    private val _playerTwoStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerTwoStats: StateFlow<PlayerStatsModel?> = _playerTwoStats


    private val _playersList = MutableStateFlow<List<PlayerModel?>>(emptyList())

    init {
        _playersList.value = playersRepository.playersList.value
    }

    fun getPlayerStats(playerOneName: String, playerTwoName: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val playerOneStats = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerOneName, sharedPreferences.credentials.year.toString())
            }
            val playerTwoStats = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerTwoName, sharedPreferences.credentials.year.toString())
            }

            if (playerOneStats != null && playerTwoStats != null) {
                mapInformation(playerOneStats, playerTwoStats)
                _uiState.value = UIState.Success
            } else {
                _uiState.value = UIState.Error
            }
        }
    }

    private fun mapInformation(playerOneStats: PlayerStatsModel, playerTwoStats: PlayerStatsModel) {
        val playersList = _playersList.value

        val playerOneInformation = playersList.find { it?.ownReference == playerOneStats.reference }
        val playerTwoInformation = playersList.find { it?.ownReference == playerTwoStats.reference }

        _playerOneStats.value = playerOneStats.copy(information = playerOneInformation)
        _playerTwoStats.value = playerTwoStats.copy(information = playerTwoInformation)
    }

    fun provideMaxStatValue(): Int {
        val maxGoals = maxOf(_playerOneStats.value?.goals ?: 0, _playerTwoStats.value?.goals ?: 0)
        val maxAssists = maxOf(_playerOneStats.value?.assists ?: 0, _playerTwoStats.value?.assists ?: 0)
        val maxPenalties = maxOf(_playerOneStats.value?.penalties ?: 0, _playerTwoStats.value?.penalties ?: 0)
        val maxCleanSheets = maxOf(_playerOneStats.value?.cleanSheet ?: 0, _playerTwoStats.value?.cleanSheet ?: 0)
        val maxGamesPlayed = maxOf(_playerOneStats.value?.gamesPlayed ?: 0, _playerTwoStats.value?.gamesPlayed ?: 0)
        return maxOf(maxGoals, maxAssists, maxPenalties, maxCleanSheets, maxGamesPlayed)
    }
}