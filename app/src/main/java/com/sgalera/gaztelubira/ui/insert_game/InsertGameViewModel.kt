package com.sgalera.gaztelubira.ui.insert_game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.core.Constants.PLAYER_NO_IMAGE
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.AWAY
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.HOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(
    private val teamsRepository: TeamsRepository,
    private val playersRepository: PlayersRepository,
) : ViewModel() {

    private val _match = MutableStateFlow(MatchModel())
    private val _matchStats = MutableStateFlow(MatchStatsModel())

    private val _expandable = MutableStateFlow<InsertGameExpandable?>(null)
    val expandable: StateFlow<InsertGameExpandable?> = _expandable

    private val _matchType = MutableStateFlow<MatchType?>(null)
    val matchType: StateFlow<MatchType?> = _matchType

    private val _matchLocal = MutableStateFlow<MatchLocal?>(null)
    val matchLocal: StateFlow<MatchLocal?> = _matchLocal

    private val _teamsList = MutableStateFlow<List<TeamModel?>>(emptyList())
    private val _playersList = MutableStateFlow<List<PlayerStatsModel?>>(emptyList())
    private val _playersModelList = MutableStateFlow<List<PlayerModel?>>(emptyList())

    fun onExpandableChanged(expandable: InsertGameExpandable) {
        if (_expandable.value == expandable) _expandable.value = null else _expandable.value =
            expandable
    }

    fun onMatchTypeSelected(matchType: MatchType) {
        if (_matchType.value == matchType) _matchType.value = null else _matchType.value = matchType
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
            .sortedBy { it?.information?.dorsal }
            .filter { it?.information !in _matchStats.value.starters.values }
            .filter { it?.information !in _matchStats.value.bench }
            .map { player ->
                player.let { Pair(it?.information?.name ?: "", it?.information?.dorsal.toString()) }
            }
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
        Log.i(
            "InsertGameViewModel",
            "Home: ${_matchStats.value.homeTeam} - Away: ${_matchStats.value.awayTeam}"
        )
    }

    fun setPlayerInMatchStats(playerPositions: PlayerPositions, playerName: String?) {
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
                _matchStats.value.bench += playerModel
            }
        }

        Log.i("InsertGameViewModel", "Starters: ${_matchStats.value.starters}")
        Log.i("InsertGameViewModel", "Bench: ${_matchStats.value.bench}")
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