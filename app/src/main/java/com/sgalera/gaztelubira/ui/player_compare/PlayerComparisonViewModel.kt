package com.sgalera.gaztelubira.ui.player_compare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerPosition.TECHNICAL_STAFF
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getPlayersUseCase: GetPlayersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerComparisonUiState>(PlayerComparisonUiState.Loading)
    val uiState: StateFlow<PlayerComparisonUiState> = _uiState

    private val _playersList = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val playersList: StateFlow<List<PlayerModel?>> = _playersList

    private val _playerOne = MutableStateFlow<PlayerModel?>(null)
    val playerOne: StateFlow<PlayerModel?> = _playerOne

    private val _playerTwo = MutableStateFlow<PlayerModel?>(null)
    val playerTwo: StateFlow<PlayerModel?> = _playerTwo

    private var _selectPlayer = PlayerSelection.PLAYER_TWO

    init {
        viewModelScope.launch {
            _playersList.value = withContext(Dispatchers.IO){
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
                    .filter { it?.position != TECHNICAL_STAFF }
            }
            if (_playersList.value.isNotEmpty()) {
                _uiState.value = PlayerComparisonUiState.Success
            }
        }
    }

    fun selectPlayer(player: PlayerModel) {
        when {
            // Si el jugador está seleccionado como playerOne, lo deseleccionamos
            _playerOne.value?.ownReference == player.ownReference -> {
                _playerOne.value = null
            }
            // Si el jugador está seleccionado como playerTwo, lo deseleccionamos
            _playerTwo.value?.ownReference == player.ownReference -> {
                _playerTwo.value = null
            }
            // Si ninguno de los dos jugadores está seleccionado
            else -> {
                when {
                    // Si playerOne está vacío, seleccionamos aquí
                    _playerOne.value == null -> _playerOne.value = player
                    // Si playerTwo está vacío, seleccionamos aquí
                    _playerTwo.value == null -> _playerTwo.value = player
                    // Si ambos están seleccionados, deseleccionamos playerOne y seleccionamos el nuevo jugador en playerTwo
                    else -> {
                        _playerOne.value = _playerTwo.value
                        _playerTwo.value = player
                    }
                }
            }
        }

        // Comprobamos si ambos jugadores están deseleccionados y actualizamos la lista
        updatePlayerSelection()
    }

    private fun updatePlayerSelection() {
        _playersList.value = _playersList.value.map { currentPlayer ->
            currentPlayer?.copy(
                selected = currentPlayer.ownReference == _playerOne.value?.ownReference ||
                        currentPlayer.ownReference == _playerTwo.value?.ownReference
            )
        }
    }
}


sealed class PlayerComparisonUiState{
    data object Success : PlayerComparisonUiState()
    data object Loading : PlayerComparisonUiState()
    data object Error : PlayerComparisonUiState()
}

enum class PlayerSelection{
    PLAYER_ONE,
    PLAYER_TWO
}