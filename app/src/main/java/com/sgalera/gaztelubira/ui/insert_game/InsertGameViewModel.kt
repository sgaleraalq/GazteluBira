package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.ui.stats.StatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(
    private val matchesProvider: MatchesProvider,
    private val playersProvider: PlayersProvider
) :
    ViewModel() {
    private var _state = MutableStateFlow(arrayListOf<String>())
    val state: StateFlow<ArrayList<String>> = _state

    private var _stateInsertGame =
        MutableStateFlow<InsertGameInfoState>(InsertGameInfoState.Loading)
    val stateInsertGame: StateFlow<InsertGameInfoState> = _stateInsertGame

    private var _allPlayersState = MutableStateFlow<StatsState>(StatsState.Loading)
    val allPlayersState: StateFlow<StatsState> = _allPlayersState

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
                "Lopez",
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
    ): InsertGameInfoState {
        _stateInsertGame.value = InsertGameInfoState.Loading
        val jornada = if (match == "liga") {
            "Jornada $journey"
        } else {
            "Copa"
        }
        val gameData = createGameData(homeTeam, homeGoals, awayTeam, awayGoals, match, jornada, id)

        return try {
            val result = matchesProvider.postGame(gameData)
            _stateInsertGame.value =
                if (result) InsertGameInfoState.Success else InsertGameInfoState.Error("Ha ocurrido un error, inténtelo más tarde")
            InsertGameInfoState.Success
        } catch (e: Exception) {
            _stateInsertGame.value = InsertGameInfoState.Error("Error: ${e.message}")
            InsertGameInfoState.Error("Error: ${e.message}")
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

    suspend fun getAllPlayerInfo() {
        viewModelScope.launch {
            _allPlayersState.value = StatsState.Loading
            val result = playersProvider.getAllStats()
            if (result != null) {
                _allPlayersState.value = StatsState.Success(result)
            } else {
                _allPlayersState.value =
                    StatsState.Error("Ha ocurrido un error, intentelo más tarde")
            }
        }
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
            "Lopez" -> Lopez
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
            Lopez,
            Emilio
        )
    }
}