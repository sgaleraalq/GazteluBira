package com.sgalera.gaztelubira.domain.usecases.matches

import com.sgalera.gaztelubira.domain.repository.TeamsRepository
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor(
    private val teamsRepository: TeamsRepository
) {
    suspend operator fun invoke(year: String) = teamsRepository.getTeams(year)
}
