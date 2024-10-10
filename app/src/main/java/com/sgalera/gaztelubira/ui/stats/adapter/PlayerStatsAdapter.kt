package com.sgalera.gaztelubira.ui.stats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel

class PlayerStatsAdapter(
    private val playerStats: List<PlayerStatsModel?> = emptyList(),
    private val onItemSelected: () -> Unit = {}
): RecyclerView.Adapter<PlayerStatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerStatsViewHolder {
        return PlayerStatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stats_fragment, parent, false)
        )
    }

    override fun getItemCount() = playerStats.size

    override fun onBindViewHolder(holder: PlayerStatsViewHolder, position: Int) {
        holder.render(playerStats[position]) { onItemSelected() }
    }

}
