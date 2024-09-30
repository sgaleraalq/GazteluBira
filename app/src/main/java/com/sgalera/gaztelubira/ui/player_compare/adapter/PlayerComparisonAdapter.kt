package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.PlayerModel

class PlayerComparisonAdapter(
    private var playersList: List<PlayerModel?> = emptyList(),
    private val onPlayerSelected: (PlayerModel) -> Unit
): RecyclerView.Adapter<PlayerComparisonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerComparisonViewHolder {
        return PlayerComparisonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_comparison, parent, false)
        )
    }

    override fun getItemCount() = playersList.size

    override fun onBindViewHolder(holder: PlayerComparisonViewHolder, position: Int) {
        holder.render(playersList[position]) { player -> onPlayerSelected(player) }
    }

    fun updateList(playersList: MutableList<PlayerModel?>) {
        this.playersList = playersList
        notifyDataSetChanged()
    }

}