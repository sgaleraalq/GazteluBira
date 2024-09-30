package com.sgalera.gaztelubira.ui.player_compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.PlayerModel
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

    init {
        viewModelScope.launch {
            _playersList.value = withContext(Dispatchers.IO){
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
            }
            if (_playersList.value.isNotEmpty()) {
                _uiState.value = PlayerComparisonUiState.Success
            }
        }
    }
}


sealed class PlayerComparisonUiState{
    data object Success : PlayerComparisonUiState()
    data object Loading : PlayerComparisonUiState()
    data object Error : PlayerComparisonUiState()
}