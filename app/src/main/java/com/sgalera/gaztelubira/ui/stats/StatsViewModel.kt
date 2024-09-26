package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.usecases.GetPlayersStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(
    private val playersProvider: PlayersProvider,
    private val getPlayersStatsUseCase: GetPlayersStatsUseCase
): ViewModel() {

    private var _state = MutableStateFlow<StatsState>(StatsState.Loading)
    val state: StateFlow<StatsState> = _state

    init {
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

    }
}
