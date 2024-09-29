package com.sgalera.gaztelubira.ui.team

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
class TeamListViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getPlayersUseCase: GetPlayersUseCase
): ViewModel(){

    private val _uiState = MutableStateFlow<TeamInfoState>(TeamInfoState.Loading(true))
    val uiState: StateFlow<TeamInfoState> = _uiState

    private val _playersList = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val playersList: StateFlow<List<PlayerModel?>> = _playersList

    init {
        viewModelScope.launch {
            _uiState.value = TeamInfoState.Loading(true)
            _playersList.value = withContext(Dispatchers.IO){
                getPlayersUseCase(sharedPreferences.credentials.year.toString())
            }
            if (_playersList.value.isEmpty()){
                _uiState.value = TeamInfoState.Error
            }
            _uiState.value = TeamInfoState.Loading(false)
        }
    }
}

sealed class TeamInfoState {
    data class Loading(val isLoading: Boolean): TeamInfoState()
    data object Error: TeamInfoState()
}