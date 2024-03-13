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
    private val onItemSelected: (PlayerInfo) -> Unit
    ): RecyclerView.Adapter<PopUpViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        return PopUpViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_popup, parent,false)
        )
    }

    override fun getItemCount() = playerList.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        val player = playerList[position]
        holder.render(player)
        holder.itemView.setOnClickListener {
            selectPlayer(holder.itemView)
        }
    }

    private fun selectPlayer(player: View) {
        player.findViewById<ConstraintLayout>(R.id.clPlayerPopUp).setBackgroundResource(R.color.white)
        player.findViewById<TextView>(R.id.tvPlayerName).setTextColor(player.resources.getColor(R.color.black))
        player.findViewById<ImageView>(R.id.ivCheck).visibility = View.VISIBLE

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<PlayerInfo>){
        playerList = list
        notifyDataSetChanged()
    }

}
