package com.sgalera.gaztelubira.data.response.players

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.DEFENDER
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.FORWARD
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.GOALKEEPER
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.MIDFIELDER
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.TECHNICAL_STAFF
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition.UNKNOWN

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
            position.contains("goalkeeper", ignoreCase = true) || position.contains(
                "goal keeper",
                ignoreCase = true
            ) -> GOALKEEPER

            position.contains("defender", ignoreCase = true) -> DEFENDER
            position.contains("midfielder", ignoreCase = true) -> MIDFIELDER
            position.contains("forward", ignoreCase = true) -> FORWARD
            position.contains("technical staff", ignoreCase = true) -> TECHNICAL_STAFF
            else -> UNKNOWN
        }
    }
}