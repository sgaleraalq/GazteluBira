package com.sgalera.gaztelubira.ui.matches.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.network.services.MatchesApiService
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.matches.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailMatchViewModel @Inject constructor(private val matchesProvider: MatchesProvider): ViewModel() {
    private var _state = MutableStateFlow<DetailMatchState>(DetailMatchState.Loading)
    val state = _state
    private var hasDataLoaded = false

    fun getMatch(id: Int){
        if(!hasDataLoaded){
            viewModelScope.launch {
                _state.value = DetailMatchState.Loading
                val result = withContext(Dispatchers.IO){ matchesProvider.getMatch(id = id) }
                if (result != null){
                    _state.value = DetailMatchState.Success(result)
                } else{
                    _state.value = DetailMatchState.Error("Ha ocurrido un error, inténtelo de nuevo más tarde")
                }
            }
            hasDataLoaded = true
        }
    }
}