package com.sgalera.gaztelubira.domain.model.matches

import com.sgalera.gaztelubira.domain.model.Team

data class Match(
    val local: Team,
    val visitor: Team,
    val localGoals: Int,
    val visitorGoals: Int,
    val scorers: List<String>,
    val assitants: List<String>,
    val starters: Starters,
    val bench: List<String>
)

