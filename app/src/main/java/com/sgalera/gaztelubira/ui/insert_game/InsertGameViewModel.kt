package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(): ViewModel(){
    private var _state = MutableStateFlow(arrayListOf<String>())
    val state: StateFlow<ArrayList<String>> = _state

    init {
        viewModelScope.launch {
            val playerNames = arrayListOf(
                "Pedro", "Jon", "Asier", "Manu", "Xabi", "Oso", "Diego", "Mikel", "Gorka", "Arrondo",
                "Bryant", "Dani", "Nando", "Haaland", "David", "Aaron", "Roson", "Mugueta", "Fran",
                "Iker", "Larra", "Unai", "Madariaga", "Emilio"
            )
            _state.value = playerNames
        }
    }

    fun getPlayers(): MutableList<PlayerInfo> {
        return mutableListOf(
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
            Bryant,
            Dani,
            Nando,
            Haaland,
            David,
            Aaron,
            Roson,
            Mugueta,
            Fran,
            Iker,
            Larra,
            Unai,
            Madariaga,
            Emilio
        )
    }

    fun convertToPlayerInfo(player: String): PlayerInfo {
        return when (player) {
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
            "Bryant" -> Bryant
            "Dani" -> Dani
            "Nando" -> Nando
            "Haaland" -> Haaland
            "David" -> David
            "Aaron" -> Aaron
            "Roson" -> Roson
            "Mugueta" -> Mugueta
            "Fran" -> Fran
            "Iker" -> Iker
            "Larra" -> Larra
            "Unai" -> Unai
            "Madariaga" -> Madariaga
            "Emilio" -> Emilio
            else -> throw IllegalArgumentException("Player not found")
        }
    }
}