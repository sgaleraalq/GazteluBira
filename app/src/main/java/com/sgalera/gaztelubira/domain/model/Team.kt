package com.sgalera.gaztelubira.domain.model

import com.sgalera.gaztelubira.R

sealed class Team(val name: Int, val img: Int) {
    data object GazteluBira : Team(R.string.gaztelu_bira, R.drawable.img_gaztelu_bira)
    data object Anaitasuna : Team(R.string.anaitasuna, R.drawable.img_anaitasuna)
    data object Arsenal : Team(R.string.arsenal, R.drawable.img_arsenal)
    data object Aterbea : Team(R.string.aterbea, R.drawable.img_aterbea)
    data object EsicGazteak : Team(R.string.esic_gazteak, R.drawable.img_esic_gazteak)
    data object Esmeraldenos : Team(R.string.esmeraldeños, R.drawable.img_esmeraldenos)
    data object Garre : Team(R.string.garre, R.drawable.img_garre)
    data object Iturrama : Team(R.string.iturrama, R.drawable.img_iturrama)
    data object Izn : Team(R.string.izn, R.drawable.img_izn)
    data object LaUnica : Team(R.string.la_unica, R.drawable.img_la_unica)
    data object PenaSchool : Team(R.string.pena_school, R.drawable.img_pena_school)
    data object SanCristobal : Team(R.string.san_cristobal, R.drawable.img_san_cristobal)
    data object Lezkairu: Team(R.string.lezkairu, R.drawable.img_lezkairu)

    data object Tingla2Legends : Team(R.string.tingla2_legends, R.drawable.img_no_football_shield)
}
