package com.sgalera.gaztelubira.domain.model.teams

import com.google.firebase.firestore.DocumentReference

data class TeamModel (
    val teamName: String,
    val teamImg: String,
    val ownReference: DocumentReference?
)