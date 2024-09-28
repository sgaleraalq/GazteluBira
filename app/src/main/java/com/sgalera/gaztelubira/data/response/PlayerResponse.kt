package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.PlayerModel

data class PlayerResponse(
    @get:PropertyName("complete_name") @set:PropertyName("complete_name") var completeName: String = "",
    val name: String = "",
    val dorsal: Int = 0,
    val img: String = "",
    val position: String = "",
    val stats: DocumentReference? = null,
    val ownReference: DocumentReference? = null
) {
    fun toDomain(): PlayerModel {
        return PlayerModel(
            completeName = completeName,
            name = name,
            dorsal = dorsal,
            img = img,
            position = position,
            stats = stats,
            ownReference = ownReference
        )
    }
}