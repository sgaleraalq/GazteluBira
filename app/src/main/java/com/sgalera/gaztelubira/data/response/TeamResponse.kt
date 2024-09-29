package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.TeamModel

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