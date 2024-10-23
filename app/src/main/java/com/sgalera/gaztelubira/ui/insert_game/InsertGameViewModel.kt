package com.sgalera.gaztelubira.ui.insert_game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.core.Constants.PLAYER_NO_IMAGE
import com.sgalera.gaztelubira.core.Constants.SERVER_KEY
import com.sgalera.gaztelubira.domain.manager.SharedPreferences
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import com.sgalera.gaztelubira.domain.usecases.SendNotificationUseCase
import com.sgalera.gaztelubira.domain.usecases.matches.InsertGameUseCase
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.BENCH
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.CLEAN_SHEET
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.GOALS
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.MATCH_LOCAL
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.MATCH_TYPE
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.RESULT
import com.sgalera.gaztelubira.ui.insert_game.InsertGameChecks.STARTERS
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.AWAY
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.HOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val teamsRepository: TeamsRepository,
    private val playersRepository: PlayersRepository,
    private val insertGameUseCase: InsertGameUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {

    private val _match = MutableStateFlow(MatchModel())
    private val _matchStats = MutableStateFlow(MatchStatsModel())

    private val _teamsList = MutableStateFlow<List<TeamModel?>>(emptyList())
    private val _playersList = MutableStateFlow<List<PlayerStatsModel?>>(emptyList())
    private val _playersModelList = MutableStateFlow<List<PlayerModel?>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _expandable = MutableStateFlow<InsertGameExpandable?>(null)
    val expandable: StateFlow<InsertGameExpandable?> = _expandable

    private val _matchType = MutableStateFlow<MatchType?>(null)
    val matchType: StateFlow<MatchType?> = _matchType

    private val _matchLocal = MutableStateFlow<MatchLocal?>(null)
    val matchLocal: StateFlow<MatchLocal?> = _matchLocal

    private val _benchPlayers = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val benchPlayers: StateFlow<List<PlayerModel?>> = _benchPlayers

    private val _scorers = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val scorers: StateFlow<List<PlayerModel?>> = _scorers

    private val _assistants = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val assistants: StateFlow<List<PlayerModel?>> = _assistants

    private val _cleanSheetPlayers = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val cleanSheetPlayers: StateFlow<List<PlayerModel?>> = _cleanSheetPlayers

    private val _penaltiesPlayers = MutableStateFlow<List<PlayerModel?>>(emptyList())
    val penaltiesPlayers: StateFlow<List<PlayerModel?>> = _penaltiesPlayers

    fun onExpandableChanged(expandable: InsertGameExpandable) {
        if (_expandable.value == expandable) _expandable.value = null else _expandable.value = expandable
    }

    fun onMatchTypeSelected(matchType: MatchType) {
        if (_matchType.value == matchType) _matchType.value = null else _matchType.value = matchType
        when (matchType) {
            MatchType.LEAGUE -> {
                _match.value.match = "liga"
                _matchStats.value.match = "liga"
            }

            MatchType.CUP -> {
                _match.value.match = "copa"
                _matchStats.value.match = "copa"
            }
        }
    }

    fun onMatchLocalSelected(matchLocal: MatchLocal) {
        if (_matchLocal.value == matchLocal) _matchLocal.value = null else _matchLocal.value =
            matchLocal
    }

    init {
        viewModelScope.launch {
            _teamsList.value = teamsRepository.teamsList.value
            _playersList.value = playersRepository.playersStats.value
            _playersModelList.value = playersRepository.playersList.value
        }
    }

    fun provideTeamList(): List<Pair<String, String>?> {
        return _teamsList.value.filter { it?.teamName != "Gaztelu Bira" }.map { team ->
            team?.let { Pair(it.teamName, it.teamImg) }
        }
    }

    fun providePlayersList(): List<Pair<String, String>?> {
        return _playersList.value
            .sortedBy { it?.information?.name }
            .filter { it?.information !in _matchStats.value.starters.values }
            .filter { it?.information !in _matchStats.value.bench }
            .map { player -> player.let { Pair(it?.information?.name ?: "", it?.information?.dorsal.toString()) } }
    }

    fun providePlayerListToStats(): List<Pair<String, String>?>{
        return _playersList.value
            .sortedBy { it?.information?.name }
            .filter { it?.information in _matchStats.value.starters.values || it?.information in _matchStats.value.bench }
            .map { player -> player.let { Pair(it?.information?.name ?: "", it?.information?.dorsal.toString()) } }
    }

    fun getPlayerImg(playerName: String?): String {
        return _playersList.value.find { it?.information?.name == playerName }?.information?.img
            ?: PLAYER_NO_IMAGE
    }

    fun setLocalVisitor(team: MatchLocal, teamName: String?) {
        val teamModel = _teamsList.value.find { it?.teamName == teamName }
        val otherTeam = _teamsList.value.find { it?.teamName == "Gaztelu Bira" }
        when (team) {
            HOME -> {
                _match.value.homeTeam = teamModel?.ownReference
                _matchStats.value.homeTeam = teamModel
                _match.value.awayTeam = otherTeam?.ownReference
                _matchStats.value.awayTeam = otherTeam
            }

            AWAY -> {
                _match.value.awayTeam = teamModel?.ownReference
                _matchStats.value.awayTeam = teamModel
                _match.value.homeTeam = otherTeam?.ownReference
                _matchStats.value.homeTeam = otherTeam
            }
        }
        Log.i("InsertGameViewModel", "Home: ${_match.value} - Away: ${_match.value}")
        Log.i("InsertGameViewModel", "Home: ${_matchStats.value.homeTeam} - Away: ${_matchStats.value.awayTeam}")
    }

    fun setPlayerInMatchStats(
        playerPositions: PlayerPositions,
        playerName: String?,
        showMaxBenchPlayersError: () -> Unit
    ) {
        val playerModel = _playersModelList.value.find { it?.name == playerName }
        when (playerPositions) {
            PlayerPositions.GOAL_KEEPER -> {
                _matchStats.value.starters["goal_keeper"] = playerModel
            }

            PlayerPositions.LEFT_BACK -> {
                _matchStats.value.starters["left_back"] = playerModel
            }

            PlayerPositions.RIGHT_BACK -> {
                _matchStats.value.starters["right_back"] = playerModel
            }

            PlayerPositions.LEFT_CENTRE_BACK -> {
                _matchStats.value.starters["left_centre_back"] = playerModel
            }

            PlayerPositions.RIGHT_CENTRE_BACK -> {
                _matchStats.value.starters["right_centre_back"] = playerModel
            }

            PlayerPositions.DEFENSIVE_MID_FIELDER -> {
                _matchStats.value.starters["defensive_mid_fielder"] = playerModel
            }

            PlayerPositions.LEFT_MID_FIELDER -> {
                _matchStats.value.starters["left_mid_fielder"] = playerModel
            }

            PlayerPositions.RIGHT_MID_FIELDER -> {
                _matchStats.value.starters["right_mid_fielder"] = playerModel
            }

            PlayerPositions.LEFT_STRIKER -> {
                _matchStats.value.starters["left_striker"] = playerModel
            }

            PlayerPositions.RIGHT_STRIKER -> {
                _matchStats.value.starters["right_striker"] = playerModel
            }

            PlayerPositions.STRIKER -> {
                _matchStats.value.starters["striker"] = playerModel
            }

            PlayerPositions.BENCH -> {
                if (_matchStats.value.bench.size < 6) {
                    _matchStats.value.bench += playerModel
                    _benchPlayers.value += playerModel
                } else {
                    showMaxBenchPlayersError()
                }
            }
        }
        Log.i("InsertGameViewModel", "Starters: ${_matchStats.value.starters}")
        Log.i("InsertGameViewModel", "Bench: ${_matchStats.value.bench}")
    }

    fun setStat(matchStat: MatchStats?, playerName: String?) {
        val playerModel = _playersModelList.value.find { it?.name == playerName }
        when (matchStat){
            MatchStats.SCORERS -> _scorers.value += playerModel
            MatchStats.ASSISTS -> _assistants.value += playerModel
            MatchStats.CLEAN_SHEET -> _cleanSheetPlayers.value += playerModel
            MatchStats.PENALTIES -> _penaltiesPlayers.value += playerModel
            null -> {}
        }
    }

    fun onBenchPlayerRemoved(playerName: String?) {
        _matchStats.value.bench = _matchStats.value.bench.filter { it?.name != playerName }
        _benchPlayers.value = _benchPlayers.value.filter { it?.name != playerName }
        Log.i("InsertGameViewModel", "Bench: ${_matchStats.value.bench}")
    }

    fun onScorerRemoved(playerName: String) {
        _scorers.value = _scorers.value.filter { it?.name != playerName }
    }

    fun onAssistantsRemoved(playerName: String) {
        _assistants.value = _assistants.value.filter { it?.name != playerName }
    }

    fun onCleanSheetPlayerRemoved(playerName: String) {
        _cleanSheetPlayers.value = _cleanSheetPlayers.value.filter { it?.name != playerName }
    }

    fun onPenaltiesPlayerRemoved(playerName: String) {
        _penaltiesPlayers.value = _penaltiesPlayers.value.filter { it?.name != playerName }
    }

    fun insertGame(
        id: Int,
        journey: Int,
        homeGoals: String,
        awayGoals: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        onMissingField: (InsertGameChecks) -> Unit
    ) {

        if (homeGoals.isBlank() || awayGoals.isBlank()) {
            onMissingField(RESULT)
            return
        }
        _match.value.homeGoals = homeGoals.toInt()
        _match.value.awayGoals = awayGoals.toInt()
        _matchStats.value.homeGoals = homeGoals.toInt()
        _matchStats.value.awayGoals = awayGoals.toInt()

        val matchResult = checkMatchModel { onMissingField(it) }
        if (!matchResult) return

        val matchStatsResult = checkMatchStatsModel { onMissingField(it) }
        if (!matchStatsResult) return

        _matchStats.value.scorers = _scorers.value
        _matchStats.value.assistants = _assistants.value
        _matchStats.value.penalties = _penaltiesPlayers.value
        viewModelScope.launch {
            _isLoading.value = true
//            val result = withContext(Dispatchers.IO){
//                insertGameUseCase(
//                    year = sharedPreferences.credentials.year.toString(),
//                    id = id,
//                    journey = journey,
//                    matchModel = _match.value,
//                    matchStats = _matchStats.value,
//                    playersStats = _playersList.value,
//                    cleanSheet = _cleanSheetPlayers.value
//                )
//            }
            val result = true
            if (result) {
                onSuccess()
            } else {
                onFailure()
            }
            _isLoading.value = false
        }
    }

    fun sendNotification(title: String, message: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                sendNotificationUseCase(SERVER_KEY, title, message)
            }

            if (result.isSuccessful) {
                Log.i("InsertGameViewModel", "Notification sent")
            } else {
                Log.i("InsertGameViewModel", "Notification not sent")
            }
            _isLoading.value = false
        }
    }

    private fun checkMatchModel(onMissingField: (InsertGameChecks) -> Unit): Boolean {
        val match = _match.value

        return if (match.match == null || match.match!!.isBlank()) {
            onMissingField(MATCH_TYPE)
            false
        } else if (match.homeTeam == null || match.awayTeam == null) {
            onMissingField(MATCH_LOCAL)
            false
        } else if (match.homeGoals == -1 || match.awayGoals == -1) {
            onMissingField(RESULT)
            false
        } else {
            true
        }
    }

    private fun checkMatchStatsModel(onMissingField: (InsertGameChecks) -> Unit): Boolean {
        val match = _matchStats.value
        val gazteluBira = if (match.homeTeam?.teamName == "Gaztelu Bira") HOME else AWAY

        return if (match.match == null || match.match!!.isBlank()) {
            onMissingField(MATCH_TYPE)
            false
        } else if (match.homeTeam == null || match.awayTeam == null) {
            onMissingField(MATCH_LOCAL)
            false
        } else if (match.homeGoals == -1 || match.awayGoals == -1) {
            onMissingField(RESULT)
            false
        } else if (match.starters.values.any { it == null }) {
            onMissingField(STARTERS)
            false
        } else if (match.bench.any { it == null }) {
            onMissingField(BENCH)
            false
        } else if (gazteluBira == HOME && match.homeGoals < _scorers.value.size) {
            onMissingField(GOALS)
            false
        } else if (gazteluBira == AWAY && match.awayGoals < _scorers.value.size) {
            onMissingField(GOALS)
            false
        } else if ((gazteluBira == HOME && match.awayGoals == 0) || (gazteluBira == AWAY && match.homeGoals == 0) && _cleanSheetPlayers.value.isEmpty()) {
            onMissingField(CLEAN_SHEET)
            false
        } else {
            true
        }
    }
}

enum class InsertGameExpandable {
    MATCH_TYPE, MATCH_LOCAL, RESULT, STARTERS, BENCH, STATS
}

enum class MatchType {
    LEAGUE, CUP
}

enum class MatchLocal {
    HOME, AWAY
}

enum class PlayerPositions {
    GOAL_KEEPER,
    LEFT_BACK, RIGHT_BACK, LEFT_CENTRE_BACK, RIGHT_CENTRE_BACK,
    DEFENSIVE_MID_FIELDER, LEFT_MID_FIELDER, RIGHT_MID_FIELDER,
    LEFT_STRIKER, RIGHT_STRIKER, STRIKER,
    BENCH
}

enum class MatchStats {
    SCORERS, ASSISTS, CLEAN_SHEET, PENALTIES
}

enum class InsertGameChecks {
    MATCH_TYPE, MATCH_LOCAL, RESULT, STARTERS, BENCH, GOALS, CLEAN_SHEET
}

