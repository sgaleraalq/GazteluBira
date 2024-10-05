package com.sgalera.gaztelubira.ui.team.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerStatsUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerInformationViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getPlayerStatsUseCase: GetPlayerStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerInformationState>(PlayerInformationState.Loading)
    val uiState: StateFlow<PlayerInformationState> = _uiState

    private val _player = MutableStateFlow<PlayerStatsModel?>(null)
    val player: StateFlow<PlayerStatsModel?> = _player

    fun getPlayerStats(playerPath: String){
        viewModelScope.launch {
            _uiState.value = PlayerInformationState.Loading
            val player = withContext(Dispatchers.IO){
                getPlayerStatsUseCase(playerPath, sharedPreferences.credentials.year.toString())
            }
            if (player != null){
                _uiState.value = PlayerInformationState.Success
                _player.value = player
            } else{
                _uiState.value = PlayerInformationState.Error
            }
        }
    }

    fun initManagers() {
        _uiState.value = PlayerInformationState.Success
    }
}


sealed class PlayerInformationState {
    data object Loading : PlayerInformationState()
    data object Error: PlayerInformationState()
    data object Success: PlayerInformationState()
}