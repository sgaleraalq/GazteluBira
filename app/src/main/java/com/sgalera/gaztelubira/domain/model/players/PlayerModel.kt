package com.sgalera.gaztelubira.domain.model.players

import com.google.firebase.firestore.DocumentReference

data class PlayerModel(
    val completeName: String,
    val dorsal: Int?,
    val img: String,
    val name: String,
    val position: PlayerPosition,
    val stats: DocumentReference?,
    val ownReference: DocumentReference?,
    var selected: Boolean = false
)

enum class PlayerPosition {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    FORWARD,
    TECHNICAL_STAFF,
    UNKNOWN
}