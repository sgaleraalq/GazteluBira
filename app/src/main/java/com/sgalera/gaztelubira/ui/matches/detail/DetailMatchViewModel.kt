package com.sgalera.gaztelubira.ui.matches.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.MatchStatsModel
import com.sgalera.gaztelubira.domain.usecases.matches.GetMatchStatsUseCase
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
    private val getMatchStatsUseCase: GetMatchStatsUseCase
): ViewModel() {

    private var _state = MutableStateFlow<DetailMatchState>(DetailMatchState.Loading)
    val state = _state

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

    private val _matchStats = MutableStateFlow<MatchStatsModel?>(null)
    val matchStats = _matchStats

    fun getMatch(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val year = sharedPreferences.credentials.year
            val result = withContext(Dispatchers.IO) { getMatchStatsUseCase(id.toString(), year.toString()) }
            if (result != null){
                _matchStats.value = result
            } else {

            }
            _isLoading.value = false
        }
    }
}