package com.sgalera.gaztelubira.domain.model

data class Match(
    val local: Team,
    val visitor: Team,
    val localGoals: Int,
    val visitorGoals: Int
)

