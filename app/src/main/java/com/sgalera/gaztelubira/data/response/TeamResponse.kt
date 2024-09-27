package com.sgalera.gaztelubira.data.response

import com.sgalera.gaztelubira.domain.model.TeamModel

data class TeamResponse(
    val name: String = "",
    val img: String = ""
) {
    fun toDomain(): TeamModel {
        return TeamModel(
            teamName = name,
            teamImg = img
        )
    }
}