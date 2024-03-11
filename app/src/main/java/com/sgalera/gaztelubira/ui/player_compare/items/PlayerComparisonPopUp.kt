package com.sgalera.gaztelubira.ui.player_compare.items

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.Player
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.ui.player_compare.items.adapter.PopUpAdapter

class PlayerComparisonPopUp(
    private val context: Context,
    private val view: Int,
    private val playerList: List<PlayerInfo>,
    private val playerOne: Player?,
    private val playerTwo: Player?
) {

    private lateinit var popUpAdapter: PopUpAdapter
    fun show() {
        initRecyclerView()
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(view, null)
        val builder = AlertDialog.Builder(context)



        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
    }

    private fun initRecyclerView() {
        popUpAdapter = PopUpAdapter(playerList = playerList, onItemSelected = {
            println(1)
        })
    }
}