package com.sgalera.gaztelubira.ui.player_compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.PlayerInformationList
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerComparisonViewModel @Inject constructor(
    private val playerComparisonProvider: PlayersProvider
) : ViewModel() {

    private var _state = MutableStateFlow<PlayerComparisonState>(PlayerComparisonState.Loading)
    val state: StateFlow<PlayerComparisonState> = _state

    fun getPlayerStats(playerName: String, id: Int) {
        viewModelScope.launch {
            _state.value = PlayerComparisonState.Loading
            val result = withContext(Dispatchers.IO) { playerComparisonProvider.getPlayerStats(playerName) }
            if (result != null){
                _state.value = PlayerComparisonState.Success(result, id)
            } else {
                _state.value = PlayerComparisonState.Error("Ha ocurrido un error, inténtelo de nuevo más tarde")
            }
        }
    }

    fun getAllPlayerList(): List<PlayerInformation>?{
        return PlayerInformationList.players
    }

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
            Lopez,
            Emilio
        )
    }

    fun mapPlayer(playerName: CharSequence): PlayerInfo {
        return when (playerName) {
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
            "Lopez" -> Lopez
            else -> Emilio
        }
    }
}
