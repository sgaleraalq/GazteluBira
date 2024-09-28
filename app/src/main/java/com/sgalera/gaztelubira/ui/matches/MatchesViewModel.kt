package com.sgalera.gaztelubira.ui.matches

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.usecases.matches.GetMatchesUseCase
import com.sgalera.gaztelubira.domain.usecases.matches.GetTeamUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getMatchesUseCase: GetMatchesUseCase,
    private val getTeamUseCase: GetTeamUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private val _matchesList = MutableStateFlow<List<MatchModel>>(emptyList())
    val matchesList: StateFlow<List<MatchModel>> = _matchesList

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    init {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result =
                withContext(Dispatchers.IO) { getMatchesUseCase(sharedPreferences.credentials.year.toString()) }
            if (result != null) {
                result.forEach {
                    val homeTeam = async { getTeamUseCase(it.homeTeam) }.await()
                    val awayTeam = async { getTeamUseCase(it.awayTeam) }.await()
                    it.localTeam = homeTeam
                    it.visitorTeam = awayTeam
                }
                _matchesList.value = result
                _uiState.value = UIState.Success

            } else {
                _uiState.value = UIState.Error
            }
        }
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