package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.domain.usecases.GetPlayersStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val playersProvider: PlayersProvider,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase
): ViewModel() {

    private var _state = MutableStateFlow<StatsState>(StatsState.Loading)
    val state: StateFlow<StatsState> = _state

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    init {
        _uiState.value = StatsUiState.Loading
        viewModelScope.launch {
            _state.value = StatsState.Loading
            val result = playersProvider.getAllStats()
            if (result != null) {
                _state.value = StatsState.Success(result)
            } else {
                _state.value = StatsState.Error("Ha ocurrido un error, intentelo más tarde")
            }
        }
    }

    fun getPlayersStats(year: Int) {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            val result = withContext(Dispatchers.IO){
                getPlayersStatsUseCase(year.toString())
            }
            if (result != null) {
                _uiState.value = StatsUiState.Success(result.sortedByDescending { it.percentage })
            } else {
                _uiState.value = StatsUiState.Error
            }
        }
    }
}


sealed class StatsUiState {
    data object Loading: StatsUiState()
    data object Error: StatsUiState()
    data class Success(val playersStats: List<PlayerStatsModel>): StatsUiState()
}