package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgalera.gaztelubira.domain.model.matches.MatchModel
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.PlayersRepository
import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.*
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
        }
    }

    fun provideTeamList(): List<Pair<String, String>?> {
        return _teamsList.value.filter { it?.teamName != "Gaztelu Bira" }.map { team ->
            team?.let { Pair(it.teamName, it.teamImg) }
        }
    }

    fun providePlayersList(): List<Pair<String, String>?> {
        return _playersList.value.map { player ->
            player.let { Pair(it?.information?.name ?: "", it?.information?.dorsal.toString()) }
        }
    }

    fun setLocalVisitor(team: MatchLocal, teamName: String?, teamImg: String?) {
        when (team){
            HOME -> {

            }
            AWAY -> {

            }
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