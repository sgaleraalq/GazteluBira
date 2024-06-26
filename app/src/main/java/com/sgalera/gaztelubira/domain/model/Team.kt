package com.sgalera.gaztelubira.domain.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.R

sealed class Team(val name: Int, val img: Int) {
    data object GazteluBira : Team(R.string.gaztelu_bira, R.drawable.img_gaztelu_bira)
    data object ErrorTeam : Team(R.string.error, R.drawable.img_no_football_shield)
}

data class TeamInformation(
    val name: String = "",
    val img: String = "",
    @get:PropertyName("own_reference") @set:PropertyName("own_reference") var reference: DocumentReference? = null
)
