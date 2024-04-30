package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.data.response.MatchResponse
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.stats.StatsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
            _state.value = playersProvider.getAllPlayers()
        }
    }

    suspend fun postGame(
        homeTeam: String,
        homeGoals: Int,
        awayTeam: String,
        awayGoals: Int,
        match: String,
        journey: Int,
        id: Int,
        starters: Map<String, String>? = null,
        bench: List<String>? = null,
        scorers: List<String>? = null,
        assistants: List<String>? = null
    ): InsertGameInfoState {
        _stateInsertGame.value = InsertGameInfoState.Loading
        val jornada = if (match == "liga") {
            "Jornada $journey"
        } else {
            "Copa"
        }
        val gameData = createGameData(homeTeam, homeGoals, awayTeam, awayGoals, match, jornada, id)
        val gameStats = starters?.let {
            createGameStats(homeTeam, homeGoals, awayTeam, awayGoals, match, id, starters, bench!!, scorers!!, assistants!!)
        }

        return try {
            val result = matchesProvider.postGame(gameData)
            if (result) {
                _stateInsertGame.value = InsertGameInfoState.Success
                if (gameStats != null) {
                    val statsResult = matchesProvider.postGameStats(gameStats)
                    if (statsResult) {
                        InsertGameInfoState.Success
                    } else {
                        InsertGameInfoState.Error("Error al enviar estadísticas del juego")
                    }
                } else {
                    InsertGameInfoState.Success
                }
            } else {
                _stateInsertGame.value = InsertGameInfoState.Error("Ha ocurrido un error, inténtelo más tarde")
                InsertGameInfoState.Error("Ha ocurrido un error, inténtelo más tarde")
            }
        } catch (e: Exception) {
            _stateInsertGame.value = InsertGameInfoState.Error("Error: ${e.message}")
            InsertGameInfoState.Error("Error: ${e.message}")
        }
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

    fun insertGameStats(players: List<PlayerStats>): Boolean {
        return try {
            runBlocking {
                players.forEach {
                    playersProvider.insertPlayerStats(it)
                }
            }
            true
        } catch (e: Exception) {
            false
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

    private fun createGameStats(
        homeTeam: String,
        homeGoals: Int,
        awayTeam: String,
        awayGoals: Int,
        match: String,
        id: Int,
        starters: Map<String, String>,
        bench: List<String>,
        scorers: List<String>,
        assistants: List<String>
    ): MatchResponse {
        return MatchResponse(
            homeTeam = homeTeam,
            homeGoals = homeGoals,
            awayTeam = awayTeam,
            awayGoals = awayGoals,
            starters = starters,
            match = match,
            id = id,
            bench = bench,
            scorers = scorers,
            assistants = assistants
        )
    }
}