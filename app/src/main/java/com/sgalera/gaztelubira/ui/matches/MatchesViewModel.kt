package com.sgalera.gaztelubira.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.MatchModel
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
    matchesProvider: MatchesProvider
) : ViewModel() {

    private var _state = MutableStateFlow<MatchInfoState>(MatchInfoState.Loading)
    val state: StateFlow<MatchInfoState> = _state

    private val _matchesList = MutableStateFlow<List<MatchModel>>(emptyList())
    val matchesList: StateFlow<List<MatchModel>> = _matchesList

    init {
        viewModelScope.launch {
            _state.value = MatchInfoState.Loading
            val result = withContext(Dispatchers.IO) { matchesProvider.getMatches() }
            if (result != null) {
                _state.value = MatchInfoState.Success(result)
            } else {
                _state.value = MatchInfoState.Error("Ha ocurrido un error, intentelo más tarde")
            }
        }
    }


    fun init() {

    }
}