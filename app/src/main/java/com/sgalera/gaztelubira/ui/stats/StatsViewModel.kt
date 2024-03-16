package com.sgalera.gaztelubira.ui.stats

import androidx.lifecycle.ViewModel
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StatsViewModel @Inject constructor(private val playersProvider: PlayersProvider): ViewModel() {

    suspend fun getStats(): List<PlayerStats>? {
        return playersProvider.getAllStats()
    }
}
