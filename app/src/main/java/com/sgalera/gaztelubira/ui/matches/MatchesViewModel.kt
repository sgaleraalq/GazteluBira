package com.sgalera.gaztelubira.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.usecases.matches.GetMatchesUseCase
import com.sgalera.gaztelubira.domain.usecases.matches.GetTeamsUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamsUseCase: GetTeamsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _matchesList = MutableStateFlow<List<MatchModel>>(emptyList())
    val matchesList: StateFlow<List<MatchModel>> = _matchesList

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _leagueJourney = MutableStateFlow(0)
    val leagueJourney: StateFlow<Int> = _leagueJourney

    private val _id = MutableStateFlow(0)
    val id: StateFlow<Int> = _id

    init {
        viewModelScope.launch {
            val teamsList = withContext(Dispatchers.IO) {
                getTeamsUseCase(sharedPreferences.credentials.year.toString())
            }
            if (teamsList.isNotEmpty()){
                initMatches(teamsList)
            }
        }
    }

    private fun initMatches(teamsList: List<TeamModel>) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = withContext(Dispatchers.IO) { getMatchesUseCase(sharedPreferences.credentials.year.toString()) }
            if (result != null) {
                _matchesList.value = result.map { match ->
                    val homeTeam = teamsList.find { it.ownReference == match.homeTeam }
                    val awayTeam = teamsList.find { it.ownReference == match.awayTeam }
                    match.localTeam = homeTeam
                    match.visitorTeam = awayTeam
                    match
                }.sortedByDescending { it.id }
                _uiState.value = UIState.Success
                _id.value = _matchesList.value.first().id + 1
                setJourneyLeague()
            } else {
                _uiState.value = UIState.Error
            }
        }
    }

    private fun setJourneyLeague() {
        _leagueJourney.value = (_matchesList.value.maxByOrNull { it.journey }?.journey?.plus(1)) ?: 0
    }

    fun checkAdminStatus() {
        _isAdmin.value = sharedPreferences.credentials.isAdmin
    }
}

sealed class UIState {
    data object Loading : UIState()
    data object Success : UIState()
    data object Error : UIState()
}