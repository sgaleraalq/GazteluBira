package com.sgalera.gaztelubira.domain.model.players

import com.sgalera.gaztelubira.R

sealed class PlayerInfo(
    val name: String,
    val img: Int,
    val dorsal: Int
) {
    data object Pedro: PlayerInfo("Pedro", R.drawable.ic_profile, 1)
    data object Jon: PlayerInfo("Jon", R.drawable.ic_profile, 2)
    data object Asier: PlayerInfo("Asier", R.drawable.ic_profile, 3)
    data object Xabi: PlayerInfo("Xabi", R.drawable.ic_profile, 7)
    data object Oso: PlayerInfo("Oso", R.drawable.ic_profile, 8)
    data object Diego: PlayerInfo("Diego", R.drawable.ic_profile, 9)
    data object Mikel: PlayerInfo("Mikel", R.drawable.ic_profile, 10)
    data object Gorka: PlayerInfo("Gorka", R.drawable.ic_profile, 11)
    data object Arrondo: PlayerInfo("Arrondo", R.drawable.ic_profile, 14)
    data object Dani: PlayerInfo("Dani", R.drawable.ic_profile, 16)
    data object Nando: PlayerInfo("Nando", R.drawable.ic_profile, 18)
    data object Haaland: PlayerInfo("Haaland", R.drawable.ic_profile, 22)
    data object David: PlayerInfo("David", R.drawable.ic_profile, 23)
    data object Aaron: PlayerInfo("Aaron", R.drawable.ic_profile, 24)


    data object Mugueta: PlayerInfo("Mugueta", R.drawable.ic_profile, 0)
    data object Fran: PlayerInfo("Fran", R.drawable.ic_profile, 0)
    data object Iker: PlayerInfo("Iker", R.drawable.ic_profile, 0)
    data object Larra: PlayerInfo("Larra", R.drawable.ic_profile, 0)
    data object Unai: PlayerInfo("Unai", R.drawable.ic_profile, 0)
    data object Manu: PlayerInfo("Manu", R.drawable.ic_profile, 0)
    data object Madariaga: PlayerInfo("Madariaga", R.drawable.ic_profile, 0)
    data object Bryant: PlayerInfo("Bryant", R.drawable.ic_profile, 0)
    data object Roson: PlayerInfo("Roson", R.drawable.ic_profile, 0)
    data object Emilio: PlayerInfo("Emilio", R.drawable.ic_profile, 0)

}