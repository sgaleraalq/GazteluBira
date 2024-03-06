package com.sgalera.gaztelubira.domain.model.matches

data class Match(
    val local: String,
    val visitor: String,
    val localGoals: Int,
    val visitorGoals: Int,
    val scorers: List<String>,
    val assitants: List<String>,
    val starters: Starters,
    val bench: List<String>
)

