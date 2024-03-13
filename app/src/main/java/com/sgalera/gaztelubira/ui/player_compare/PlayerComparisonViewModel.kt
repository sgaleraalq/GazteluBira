package com.sgalera.gaztelubira.ui.player_compare

import androidx.lifecycle.ViewModel
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(): ViewModel() {
    private var _state = MutableStateFlow<PlayerComparisonState>(PlayerComparisonState.Loading)
    val state: StateFlow<PlayerComparisonState> = _state

    fun getPlayerList(): List<PlayerInfo> {
        return listOf(
            Pedro,
            Jon,
            Asier,
            Manu,
            Xabi,
            Oso,
            Diego,
            Mikel,
            Gorka,
            Arrondo,
            Dani,
            Nando,
            Haaland,
            David,
            Aaron,
            Mugueta,
            Fran,
            Iker,
            Larra,
            Unai,
            Madariaga,
            Bryant,
            Roson,
            Emilio
        )
    }

    fun mapPlayer(playerName: CharSequence): PlayerInfo{
        return when(playerName){
            "Pedro" -> Pedro
            "Jon" -> Jon
            "Asier" -> Asier
            "Manu" -> Manu
            "Xabi" -> Xabi
            "Oso" -> Oso
            "Diego" -> Diego
            "Mikel" -> Mikel
            "Gorka" -> Gorka
            "Arrondo" -> Arrondo
            "Dani" -> Dani
            "Nando" -> Nando
            "Haaland" -> Haaland
            "David" -> David
            "Aaron" -> Aaron
            "Mugueta" -> Mugueta
            "Fran" -> Fran
            "Iker" -> Iker
            "Larra" -> Larra
            "Unai" -> Unai
            "Madariaga" -> Madariaga
            "Bryant" -> Bryant
            "Roson" -> Roson
            else -> Emilio
        }
    }
}
