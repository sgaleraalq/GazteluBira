package com.sgalera.gaztelubira.data.response

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerPosition
import com.sgalera.gaztelubira.domain.model.PlayerPosition.*

data class PlayerResponse(
    @get:PropertyName("complete_name") @set:PropertyName("complete_name") var completeName: String = "",
    val name: String = "",
    val dorsal: Int? = 0,
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
            position = mapPositions(position),
            stats = stats,
            ownReference = ownReference
        )
    }

    private fun mapPositions(position: String): PlayerPosition {
        return when {
            position.contains("goalkeeper", ignoreCase = true) -> GOALKEEPER
            position.contains("defender", ignoreCase = true) -> DEFENDER
            position.contains("midfielder", ignoreCase = true) -> MIDFIELDER
            position.contains("forward", ignoreCase = true) -> FORWARD
            position.contains("technical staff", ignoreCase = true) -> TECHNICAL_STAFF
            else -> UNKNOWN
        }
    }
}