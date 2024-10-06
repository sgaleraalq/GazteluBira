package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

class BenchAdapter(
    private val benchList: List<PlayerModel?> = emptyList(),
    private val onCancelSelected: (String) -> Unit
): RecyclerView.Adapter<BenchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenchViewHolder {
        return BenchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_selection, parent, false)
        )
    }

    override fun getItemCount() = benchList.size

    override fun onBindViewHolder(holder: BenchViewHolder, position: Int) {
        holder.render(benchList[position]) { onCancelSelected(it) }
    }
}