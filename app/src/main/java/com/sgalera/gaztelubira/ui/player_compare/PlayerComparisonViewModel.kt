package com.sgalera.gaztelubira.ui.player_compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(
    private val playerComparisonProvider: PlayersProvider
) : ViewModel() {

    private var _statePlayerOne = MutableStateFlow<PlayerComparisonState>(PlayerComparisonState.Loading)
    val statePlayerOne: StateFlow<PlayerComparisonState> = _statePlayerOne

    private var _statePlayerTwo = MutableStateFlow<PlayerComparisonState>(PlayerComparisonState.Loading)
    val statePlayerTwo: StateFlow<PlayerComparisonState> = _statePlayerTwo

    fun getPlayerStatsPlayerOne(playerReference: DocumentReference): PlayerStats? {
        viewModelScope.launch {
            _statePlayerOne.value = PlayerComparisonState.Loading
            val result = withContext(Dispatchers.IO) { playerComparisonProvider.getPlayerStatsByReference(playerReference) }
            if (result != null){
                _statePlayerOne.value = PlayerComparisonState.Success(result)
            } else {
                _statePlayerOne.value = PlayerComparisonState.Error("Ha ocurrido un error, inténtelo de nuevo más tarde")
            }
        }
        return null
    }

    fun getPlayerStatsPlayerTwo(playerTwoReference: DocumentReference) {
        viewModelScope.launch {
            _statePlayerTwo.value = PlayerComparisonState.Loading
            val result = withContext(Dispatchers.IO) { playerComparisonProvider.getPlayerStatsByReference(playerTwoReference) }
            if (result != null){
                _statePlayerTwo.value = PlayerComparisonState.Success(result)
            } else {
                _statePlayerTwo.value = PlayerComparisonState.Error("Ha ocurrido un error, inténtelo de nuevo más tarde")
            }
        }
    }
}
