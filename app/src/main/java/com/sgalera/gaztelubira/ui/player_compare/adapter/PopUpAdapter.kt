package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

class PopUpAdapter(
    private var playerList: List<PlayerInfo> = emptyList(),
    private var selectedPlayers: MutableList<View> = mutableListOf(),
    private val showDoneButton: () -> Unit,
    private val hideDoneButton: () -> Unit
) : RecyclerView.Adapter<PopUpViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        return PopUpViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_popup, parent, false)
        )
    }

    override fun getItemCount() = playerList.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        val player = playerList[position]
        holder.render(player)
        holder.itemView.setOnClickListener {
            dealWithSelection(holder, player)
        }
    }

    private fun dealWithSelection(holder: PopUpViewHolder, player: PlayerInfo) {
        val state = alreadySelected(player)
        if (state){
            if (selectedPlayers.size == 2){
                unSelectPlayer(selectedPlayers[0])
                selectPlayer(holder.itemView)
            } else {
                selectPlayer(holder.itemView)
            }
        } else {
            unSelectPlayer(holder.itemView)
        }

        if (selectedPlayers.size == 2) {
            showDoneButton()
        } else {
            hideDoneButton()
        }
    }


    private fun alreadySelected(player: PlayerInfo): Boolean {
        player.selected = !player.selected
        return player.selected
    }

    private fun selectPlayer(player: View) {
        player.findViewById<ConstraintLayout>(R.id.clPlayerPopUp)
            .setBackgroundResource(R.color.white_80_opacity)
        player.findViewById<TextView>(R.id.tvPlayerName)
            .setTextColor(player.resources.getColor(R.color.black))
        player.findViewById<ImageView>(R.id.ivCheck).visibility = View.VISIBLE
        selectedPlayers.add(player)
    }

    private fun unSelectPlayer(itemView: View) {
        itemView.findViewById<ConstraintLayout>(R.id.clPlayerPopUp)
            .setBackgroundResource(R.color.primary_dark)
        itemView.findViewById<TextView>(R.id.tvPlayerName)
            .setTextColor(itemView.resources.getColor(R.color.white))
        itemView.findViewById<ImageView>(R.id.ivCheck).visibility = View.GONE

        selectedPlayers.remove(itemView)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<PlayerInfo>) {
        playerList = list
        notifyDataSetChanged()
    }

}

