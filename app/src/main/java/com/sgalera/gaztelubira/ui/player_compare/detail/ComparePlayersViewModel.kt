package com.sgalera.gaztelubira.ui.player_compare.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerStatsUseCase
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
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

    private val _playerOneStats = MutableStateFlow<PlayerStatsModel?>(null)
    val playerOneStats: StateFlow<PlayerStatsModel?> = _playerOneStats


    fun getPlayerStats(playerName: String) {
        viewModelScope.launch {
            _playerOneStats.value = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerName, sharedPreferences.credentials.year.toString())
            }
        }
    }
}