package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

class PenaltiesAdapter(
    private val penaltiesList: List<PlayerModel?> = emptyList(),
    private val onPlayerSelected: (String) -> Unit
) : RecyclerView.Adapter<StatsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        return StatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stats, parent, false)
        )
    }

    override fun getItemCount() = penaltiesList.size

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.render(penaltiesList[position]) { onPlayerSelected(it) }
    }
}