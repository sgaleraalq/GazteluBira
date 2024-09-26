package com.sgalera.gaztelubira.domain.model

data class Credentials(
    val isAdmin: Boolean,
    val player: String?,
    val year: Int
)