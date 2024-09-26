package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.domain.usecases.GetPlayersStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayerModelUseCase
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
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase,
    private val getPlayerModelUseCase: GetPlayerModelUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<StatsState>(StatsState.Loading)
    val state: StateFlow<StatsState> = _state

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState

    fun getPlayersStats(year: Int) {
        viewModelScope.launch {
            _uiState.value = StatsUiState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    getPlayersStatsUseCase(year.toString())
                }

                result?.let { playersList ->
                    val updatedPlayers = playersList.map { player ->
                        async {
                            if (player.reference != null) {
                                val playerModel = getPlayerModelUseCase(player.reference)
                                player.copy(information = playerModel)
                            } else {
                                player
                            }
                        }
                    }.awaitAll()

                    _uiState.value = StatsUiState.Success(
                        updatedPlayers.sortedByDescending { it.percentage }
                    )
                } ?: run {
                    _uiState.value = StatsUiState.Error
                }
            } catch (e: Exception) {
                _uiState.value = StatsUiState.Error
            }
        }
    }
}


sealed class StatsUiState {
    data object Loading : StatsUiState()
    data object Error : StatsUiState()
    data class Success(val playersStats: List<PlayerStatsModel>) : StatsUiState()
}