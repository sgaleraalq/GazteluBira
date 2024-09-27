package com.sgalera.gaztelubira.domain.usecases.matches

import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(year: String) = matchesRepository.getMatches(year)
}
