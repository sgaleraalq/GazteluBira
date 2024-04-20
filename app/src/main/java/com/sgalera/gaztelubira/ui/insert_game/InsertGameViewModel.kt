package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(private val matchesProvider: MatchesProvider) :
    ViewModel() {
    private var _state = MutableStateFlow(arrayListOf<String>())
    val state: StateFlow<ArrayList<String>> = _state

    private var _stateInsertGame = MutableStateFlow(InsertGameInfoState.Loading)
    val stateInsertGame: StateFlow<InsertGameInfoState> = _stateInsertGame

    init {
        viewModelScope.launch {
            val playerNames = arrayListOf(
                "Pedro",
                "Jon",
                "Asier",
                "Manu",
                "Xabi",
                "Oso",
                "Diego",
                "Mikel",
                "Gorka",
                "Arrondo",
                "Bryant",
                "Dani",
                "Nando",
                "Haaland",
                "David",
                "Aaron",
                "Roson",
                "Mugueta",
                "Fran",
                "Iker",
                "Larra",
                "Unai",
                "Madariaga",
                "Emilio"
            )
            _state.value = playerNames
        }
    }

    suspend fun postGame(
        homeTeam: String,
        homeGoals: Int,
        awayTeam: String,
        awayGoals: Int,
        match: String,
        journey: Int,
        id: Int
    ) {
        _stateInsertGame.value = InsertGameInfoState.Loading
        val jornada = if (match == "liga") { "Jornada $journey" } else { "Copa" }
        val gameData = createGameData(homeTeam, homeGoals, awayTeam, awayGoals, match, jornada, id)
        val result = withContext(Dispatchers.IO){ matchesProvider.postGame(gameData) }
        if (result != null) {
            _stateInsertGame.value = InsertGameInfoState.Success
        } else {
            _stateInsertGame.value = InsertGameInfoState.Error("Ha ocurrido un error, intentelo más tarde")
        }
    }

    private fun createGameData(
        homeTeam: String,
        homeGoals: Int,
        awayTeam: String,
        awayGoals: Int,
        match: String,
        jornada: String,
        id: Int
    ): MatchInfo {
        return MatchInfo(
            homeTeam = homeTeam,
            homeGoals = homeGoals,
            awayTeam = awayTeam,
            awayGoals = awayGoals,
            match = match,
            journey = jornada,
            id = id
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
}