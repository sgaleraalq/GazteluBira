package com.sgalera.gaztelubira.ui.insert_game

import androidx.lifecycle.ViewModel
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InsertGameViewModel @Inject constructor(): ViewModel(){

    fun getPlayers(): List<PlayerInfo> {
        return listOf(
            Pedro,
            Jon,
            Asier,
            Manu,
            Xabi,
            Oso,
            Diego,
            Mikel,
            Gorka,
            Arrondo,
            Bryant,
            Dani,
            Nando,
            Haaland,
            David,
            Aaron,
            Roson,
            Mugueta,
            Fran,
            Iker,
            Larra,
            Unai,
            Madariaga,
            Emilio
        )
    }
}