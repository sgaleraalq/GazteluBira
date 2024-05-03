package com.sgalera.gaztelubira.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.network.services.PlayersApiService
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val playersProvider: PlayersProvider): ViewModel(){
    private var _players = MutableStateFlow<TeamInfoState>(TeamInfoState.Loading)
    val players: StateFlow<TeamInfoState> = _players

    init {
        viewModelScope.launch {
            _players.value = TeamInfoState.Loading
            val playerListResult = withContext(Dispatchers.IO){ playersProvider.getAllPlayers() }
            if (playerListResult != null){
                _players.value = TeamInfoState.Success(playerListResult)
            } else {
                _players.value = TeamInfoState.Error("Error fetching players")
            }
        }
    }
}