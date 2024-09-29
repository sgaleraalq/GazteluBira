package com.sgalera.gaztelubira.domain.model

import com.google.firebase.firestore.DocumentReference

data class PlayerModel(
    val completeName: String,
    val dorsal: Int?,
    val img: String,
    val name: String,
    val position: PlayerPosition,
    val stats: DocumentReference?,
    val ownReference: DocumentReference?
)

enum class PlayerPosition {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    FORWARD,
    TECHNICAL_STAFF,
    UNKNOWN
}