package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

data class PlayerInfoResponse(
    val name: String = "",
    val surname: String = "",
    val img: String = "",
    val dorsal: Int = 0,
    val stats: DocumentReference? = null
) {
    fun toDomain(): PlayerInformation {
        return PlayerInformation(
            name = name,
            img = img,
            dorsal = dorsal,
            stats = stats,
            selected = false,
            surname = surname
        )
    }
}