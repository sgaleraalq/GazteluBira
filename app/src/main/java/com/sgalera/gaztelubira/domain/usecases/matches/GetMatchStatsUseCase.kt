package com.sgalera.gaztelubira.domain.usecases.matches

import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import javax.inject.Inject

class GetMatchStatsUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(
        id: String,
        year: String,
        playersRef: List<PlayerModel?>,
        teamsRef: List<TeamModel?>
    ) =
        matchesRepository.getMatchStats(id, year, playersRef, teamsRef)
}
