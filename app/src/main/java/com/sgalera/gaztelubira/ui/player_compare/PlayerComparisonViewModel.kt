package com.sgalera.gaztelubira.ui.player_compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.TECHNICAL_STAFF
import com.sgalera.gaztelubira.domain.usecases.players.GetPlayersUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import com.sgalera.gaztelubira.ui.player_compare.PlayerSelection.*
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

    private var _updatedPlayers = MutableStateFlow<Pair<Int?, Int?>>(Pair(null, null))
    val updatedPlayers: StateFlow<Pair<Int?, Int?>> = _updatedPlayers

    private var _selectPlayer = PLAYER_TWO

    init {
        viewModelScope.launch {
            _playersList.value = withContext(Dispatchers.IO){
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
                    .filter { it?.position != TECHNICAL_STAFF }.sortedBy { it?.dorsal }
            }
            if (_playersList.value.isNotEmpty()) {
                _uiState.value = PlayerComparisonUiState.Success
            }
        }
    }

    fun selectPlayer(player: PlayerModel) {
        val firstPlayerIdx = _playersList.value.indexOfFirst { it?.ownReference == player.ownReference }
        val secondPlayerIdx: Int?
        when {
            _playerOne.value?.ownReference == player.ownReference -> {
                _playerOne.value = null
                _updatedPlayers.value = Pair(null, firstPlayerIdx)
            }
            _playerTwo.value?.ownReference == player.ownReference -> {
                _playerTwo.value = null
                _updatedPlayers.value = Pair(null, firstPlayerIdx)
            }
            else -> {
                when {
                    _playerOne.value == null -> {
                        _playerOne.value = player
                        _updatedPlayers.value = Pair(firstPlayerIdx, null)
                    }
                    _playerTwo.value == null -> {
                        _playerTwo.value = player
                        _updatedPlayers.value = Pair(firstPlayerIdx, null)
                    }
                    else -> {
                        if (_selectPlayer == PLAYER_TWO) {
                            secondPlayerIdx = _playersList.value.indexOfFirst { it?.ownReference == _playerOne.value?.ownReference }
                            _playerOne.value = player
                            _selectPlayer = PLAYER_ONE
                        } else {
                            secondPlayerIdx = _playersList.value.indexOfFirst { it?.ownReference == _playerTwo.value?.ownReference }
                            _playerTwo.value = player
                            _selectPlayer = PLAYER_TWO
                        }
                        _updatedPlayers.value = Pair(firstPlayerIdx, secondPlayerIdx)
                    }
                }
            }
        }
        checkUiState()
        updatePlayerSelection()
    }

    private fun checkUiState() {
        if (_playerOne.value != null && _playerTwo.value != null) {
            _uiState.value = PlayerComparisonUiState.ShowButton
        } else {
            _uiState.value = PlayerComparisonUiState.HideButton
        }
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
    data object ShowButton : PlayerComparisonUiState()
    data object HideButton : PlayerComparisonUiState()
}

enum class PlayerSelection{
    PLAYER_ONE,
    PLAYER_TWO
}