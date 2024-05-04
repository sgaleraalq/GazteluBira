package com.sgalera.gaztelubira.ui.team.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class GoalKeeperAdapter(
    private var goalKeeperList: List<PlayerInformation> = emptyList(),
    private val onItemSelected: (PlayerInformation) -> Unit
): RecyclerView.Adapter<PlayersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_players, parent, false)
        )
    }

    override fun getItemCount() = goalKeeperList.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.render(goalKeeperList[position], onItemSelected)
    }

}
