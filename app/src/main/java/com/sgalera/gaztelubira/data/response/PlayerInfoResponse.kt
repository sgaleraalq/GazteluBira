package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

data class PlayerInfoResponse(
    val name: String = "",
    @get:PropertyName("complete_name") @set:PropertyName("complete_name") var completeName: String = "",
    val img: String = "",
    val dorsal: Int = 0,
    val stats: DocumentReference? = null
) {
    fun toDomain(): PlayerInformation {
        return PlayerInformation(
            name = name,
            completeName = completeName,
            img = img,
            dorsal = dorsal,
            stats = stats,
            selected = false,
        )
    }
}