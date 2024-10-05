package com.sgalera.gaztelubira.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamListViewModel @Inject constructor(
    private val playersRepository: PlayersRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _playersList = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val playersList: StateFlow<List<PlayerModel?>> = _playersList

    init {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            _playersList.value = playersRepository.playersList.value.sortedBy { it?.dorsal }
            if (_playersList.value.isEmpty()){
                _uiState.value = UIState.Error
            } else {
                _uiState.value = UIState.Success
            }
        }
    }
}