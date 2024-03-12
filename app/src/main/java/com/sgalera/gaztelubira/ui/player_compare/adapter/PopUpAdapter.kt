package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

class PopUpAdapter(
    private var playerList: List<PlayerInfo> = emptyList(),
    private val onItemSelected: () -> Unit
    ): RecyclerView.Adapter<PopUpViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        return PopUpViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_popup, parent,false)
        )
    }

    override fun getItemCount() = playerList.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        holder.render(playerList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<PlayerInfo>){
        playerList = list
        notifyDataSetChanged()
    }

}
