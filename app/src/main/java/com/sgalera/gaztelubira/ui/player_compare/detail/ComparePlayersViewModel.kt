package com.sgalera.gaztelubira.ui.player_compare.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerStatsUseCase
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.model.UIState
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
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _playerOneStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerOneStats: StateFlow<PlayerStatsModel?> = _playerOneStats

    private val _playerTwoStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerTwoStats: StateFlow<PlayerStatsModel?> = _playerTwoStats


    fun getPlayerStats(playerName: String) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val playerOneStats = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerName, sharedPreferences.credentials.year.toString())
            }
            val playerTwoStats = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerName, sharedPreferences.credentials.year.toString())
            }

            if (playerOneStats != null && playerTwoStats != null) {
                _playerOneStats.value = playerOneStats
                _playerTwoStats.value = playerTwoStats
                _uiState.value = UIState.Success
            } else {
                _uiState.value = UIState.Error
            }
        }
    }
}