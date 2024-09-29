package com.sgalera.gaztelubira.ui.matches.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.TeamModel
import com.sgalera.gaztelubira.domain.usecases.matches.GetMatchStatsUseCase
import com.sgalera.gaztelubira.domain.usecases.matches.GetTeamsUseCase
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailMatchViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val getTeamsUseCase: GetTeamsUseCase, // TODO: Change to obtain global object
    private val getMatchStatsUseCase: GetMatchStatsUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<DetailMatchState>(DetailMatchState.Loading)
    val state = _state

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

    private val _matchStats = MutableStateFlow<MatchStatsModel?>(null)
    val matchStats = _matchStats

    private val _teamsList = MutableStateFlow<List<TeamModel>>(emptyList())

    init {
        viewModelScope.launch {
            _teamsList.value = withContext(Dispatchers.IO) {
                getTeamsUseCase(sharedPreferences.credentials.year.toString())
            }
        }
    }

    fun initMatch(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val year = sharedPreferences.credentials.year
            val result = withContext(Dispatchers.IO) { getMatchStatsUseCase(id.toString(), year.toString()) }
            if (result != null) {
                _matchStats.value = result
                Log.i("DetailMatchViewModel", "MatchStats: $result")
            } else {

            }
            _isLoading.value = false
        }
    }
}