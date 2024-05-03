package com.sgalera.gaztelubira.domain.model.players

import com.google.firebase.firestore.DocumentReference
data class PlayerInformation(
    val name: String,
    val completeName: String,
    val img: String,
    val dorsal: Int,
    val stats: DocumentReference? = null,
    var selected: Boolean = false
)