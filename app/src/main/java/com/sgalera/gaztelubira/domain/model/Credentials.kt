package com.sgalera.gaztelubira.domain.model

data class Credentials(
    val isAdmin: Boolean = false,
    val player: String = "",
    val year: Int? = null
)