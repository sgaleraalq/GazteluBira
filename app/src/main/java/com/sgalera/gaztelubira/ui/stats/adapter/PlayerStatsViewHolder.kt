package com.sgalera.gaztelubira.ui.stats.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.databinding.ItemStatsFragmentBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel

class PlayerStatsViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding: ItemStatsFragmentBinding = ItemStatsFragmentBinding.bind(view)

    fun render(player: PlayerStatsModel?, onPlayerSelected: () -> Unit) {
        binding.tvLaurelPosition.text = "1"
        binding.tvPlayerName.text = player?.information?.name
        binding.tvPlayerStat.text = player?.percentage

        binding.parent.setOnClickListener { onPlayerSelected() }
    }
}
