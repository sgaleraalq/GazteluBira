package com.sgalera.gaztelubira.domain.usecases.matches

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.repository.MatchesRepository
import javax.inject.Inject

class GetTeamUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {
    suspend operator fun invoke(teamReference: DocumentReference?) = matchesRepository.getTeam(teamReference)
}
