package com.sgalera.gaztelubira.domain.model

data class Credentials(
    var isAdmin: Boolean = false,
    var player: String = "",
    var year: Int = 2024,
)