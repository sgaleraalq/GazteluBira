package com.sgalera.gaztelubira.data.response.teams

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.teams.TeamModel

data class TeamResponse(
    val name: String = "",
    val img: String = "",
    val ownReference: DocumentReference? = null
) {
    fun toDomain(): TeamModel {
        return TeamModel(
            teamName = name,
            teamImg = img,
            ownReference = ownReference
        )
    }
}