package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.data.provider.MatchesProvider
import com.sgalera.gaztelubira.data.provider.PlayersProvider
import com.sgalera.gaztelubira.data.provider.TeamsProvider
import com.sgalera.gaztelubira.data.response.MatchResponse
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
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
    private val playersProvider: PlayersProvider,
    private val teamsProvider: TeamsProvider
) :
    ViewModel() {

    private val _stateTeams = MutableStateFlow<InsertGameState>(InsertGameState.Loading)
    val stateTeams: StateFlow<InsertGameState> = _stateTeams

    private val _statePlayers = MutableStateFlow<InsertGameState>(InsertGameState.Loading)
    val statePlayers: StateFlow<InsertGameState> = _statePlayers


    private var _stateInsertGame =
        MutableStateFlow<InsertGameInfoState>(InsertGameInfoState.Loading)
    val stateInsertGame: StateFlow<InsertGameInfoState> = _stateInsertGame

    private var _allPlayersState = MutableStateFlow<StatsState>(StatsState.Loading)
    val allPlayersState: StateFlow<StatsState> = _allPlayersState


    suspend fun fetchTeams(){
        val teams = matchesProvider.getTeams()
        if (teams != null){
            _stateTeams.value = InsertGameState.SuccessTeams(teams)
        } else {
            _stateTeams.value = InsertGameState.Error("Ha ocurrido un error, intentelo más tarde")
        }
    }

    suspend fun getTeamInformation(home: String): TeamInformation? {
        return teamsProvider.getTeamInformation(home)
    }

    suspend fun getPlayersInformation() {
        val players = playersProvider.getAllPlayers()
        if (players != null) {
            _statePlayers.value = InsertGameState.SuccessPlayers(players)
        } else {
            _statePlayers.value = InsertGameState.Error("Ha ocurrido un error, intentelo más tarde")
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
        val gameData = createGameData(null, homeGoals, null, awayGoals, match, jornada, id)
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


    private fun createGameData(
        homeTeam: TeamInformation?,
        homeGoals: Int,
        awayTeam: TeamInformation?,
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
            homeTeam = null,
            homeGoals = homeGoals,
            awayTeam = null,
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