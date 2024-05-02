package com.sgalera.gaztelubira.domain.model.players

import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.R

sealed class PlayerInfo(
    val name: String,
    val img: Int,
    val dorsal: Int,
    var selected: Boolean = false
) {
    data object Pedro: PlayerInfo("Pedro", R.drawable.img_no_profile_picture, 1)
    data object Jon: PlayerInfo("Jon", R.drawable.img_no_profile_picture, 2)
    data object Asier: PlayerInfo("Asier", R.drawable.img_no_profile_picture, 3)
    data object Manu: PlayerInfo("Manu", R.drawable.img_no_profile_picture, 4)
    data object Mugueta: PlayerInfo("Mugueta", R.drawable.img_no_profile_picture, 5)
    data object Xabi: PlayerInfo("Xabi", R.drawable.img_no_profile_picture, 7)
    data object Oso: PlayerInfo("Oso", R.drawable.img_no_profile_picture, 8)
    data object Diego: PlayerInfo("Diego", R.drawable.img_no_profile_picture, 9)
    data object Mikel: PlayerInfo("Mikel", R.drawable.img_no_profile_picture, 10)
    data object Gorka: PlayerInfo("Gorka", R.drawable.img_no_profile_picture, 11)
    data object Arrondo: PlayerInfo("Arrondo", R.drawable.img_no_profile_picture, 14)
    data object Bryant: PlayerInfo("Bryant", R.drawable.img_no_profile_picture, 15)
    data object Dani: PlayerInfo("Dani", R.drawable.img_no_profile_picture, 16)
    data object Nando: PlayerInfo("Nando", R.drawable.img_no_profile_picture, 18)
    data object Aaron: PlayerInfo("Aaron", R.drawable.img_no_profile_picture, 19)
    data object Haaland: PlayerInfo("Haaland", R.drawable.img_no_profile_picture, 22)
    data object David: PlayerInfo("David", R.drawable.img_no_profile_picture, 23)
    data object Emilio: PlayerInfo("Emilio", R.drawable.img_no_profile_picture, 24)
    data object Roson: PlayerInfo("Roson", R.drawable.img_no_profile_picture, 26)
    data object Unai: PlayerInfo("Unai", R.drawable.img_no_profile_picture, 27)
    data object Larra: PlayerInfo("Larra", R.drawable.img_no_profile_picture, 28)


    data object Fran: PlayerInfo("Fran", R.drawable.img_no_profile_picture, 51)
    data object Iker: PlayerInfo("Iker", R.drawable.img_no_profile_picture, 52)
    data object Madariaga: PlayerInfo("Madariaga", R.drawable.img_no_profile_picture, 55)
    data object Lopez: PlayerInfo("Lopez", R.drawable.img_no_profile_picture, 59)
}

data class PlayerInformation(
    val name: String,
    val completeName: String,
    val img: String,
    val dorsal: Int,
    val stats: DocumentReference? = null,
    var selected: Boolean = false
)