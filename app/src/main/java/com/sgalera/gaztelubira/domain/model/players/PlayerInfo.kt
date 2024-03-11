package com.sgalera.gaztelubira.domain.model.players

import com.sgalera.gaztelubira.R

sealed class PlayerInfo(
    val name: String,
    val img: Int,
    val dorsal: Int
) {
    data object Pedro: PlayerInfo("Pedro", R.drawable.ic_profile, 1)
    data object Jon: PlayerInfo("Jon", R.drawable.img_jon, 2)
    data object Asier: PlayerInfo("Asier", R.drawable.img_asier, 3)
    data object Manu: PlayerInfo("Manu", R.drawable.ic_profile, 5)
    data object Xabi: PlayerInfo("Xabi", R.drawable.img_xabi, 7)
    data object Oso: PlayerInfo("Oso", R.drawable.ic_profile, 8)
    data object Diego: PlayerInfo("Diego", R.drawable.ic_profile, 9)
    data object Mikel: PlayerInfo("Mikel", R.drawable.ic_profile, 10)
    data object Gorka: PlayerInfo("Gorka", R.drawable.ic_profile, 11)
    data object Arrondo: PlayerInfo("Arrondo", R.drawable.img_arrondo, 14)
    data object Dani: PlayerInfo("Dani", R.drawable.img_dani, 16)
    data object Nando: PlayerInfo("Nando", R.drawable.ic_profile, 18)
    data object Haaland: PlayerInfo("Haaland", R.drawable.ic_profile, 22)
    data object David: PlayerInfo("David", R.drawable.img_sadaba, 23)
    data object Aaron: PlayerInfo("Aaron", R.drawable.ic_profile, 24)


    data object Mugueta: PlayerInfo("Mugueta", R.drawable.ic_profile, 50)
    data object Fran: PlayerInfo("Fran", R.drawable.ic_profile, 51)
    data object Iker: PlayerInfo("Iker", R.drawable.ic_profile, 52)
    data object Larra: PlayerInfo("Larra", R.drawable.img_larra, 53)
    data object Unai: PlayerInfo("Unai", R.drawable.ic_profile, 54)
    data object Madariaga: PlayerInfo("Madariaga", R.drawable.ic_profile, 55)
    data object Bryant: PlayerInfo("Bryant", R.drawable.ic_profile, 56)
    data object Roson: PlayerInfo("Roson", R.drawable.ic_profile, 57)
    data object Emilio: PlayerInfo("Emilio", R.drawable.img_emilio, 58)

}