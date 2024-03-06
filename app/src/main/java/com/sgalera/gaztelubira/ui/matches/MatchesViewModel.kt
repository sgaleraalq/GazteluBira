package com.sgalera.gaztelubira.ui.matches

import androidx.lifecycle.ViewModel
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.Starters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(matchesProvider: MatchesProvider) : ViewModel() {
    private var _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches

    init {
        _matches.value = matchesProvider.getMatches()
    }
}