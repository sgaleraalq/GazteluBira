package com.sgalera.gaztelubira.ui.team.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.PlayerModel

class PlayersAdapter(
    val playersList: List<PlayerModel> = emptyList(),
    private val onPlayerSelected: (PlayerModel) -> Unit
): RecyclerView.Adapter<PlayersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_players, parent, false)
        )
    }

    override fun getItemCount() = playersList.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.render(playersList[position], onPlayerSelected)
    }
}
