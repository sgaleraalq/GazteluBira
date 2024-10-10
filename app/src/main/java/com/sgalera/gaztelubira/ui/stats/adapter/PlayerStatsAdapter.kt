package com.sgalera.gaztelubira.ui.stats.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.ui.stats.StatType

class PlayerStatsAdapter(
    private val playerStats: List<PlayerStatsModel?> = emptyList(),
    var statSelected: StatType = StatType.PERCENTAGE,
    private val onPlayerSelected: () -> Unit = {}
): RecyclerView.Adapter<PlayerStatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerStatsViewHolder {
        return PlayerStatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stats_fragment, parent, false)
        )
    }

    override fun getItemCount() = playerStats.size

    override fun onBindViewHolder(holder: PlayerStatsViewHolder, position: Int) {
        holder.render(
            player = playerStats[position],
            position = position + 1,
            statSelected = statSelected, // TODO
            onPlayerSelected = { onPlayerSelected() }
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeStatSelected(stat: StatType) {
        this.statSelected = stat
        notifyDataSetChanged()
    }
}
