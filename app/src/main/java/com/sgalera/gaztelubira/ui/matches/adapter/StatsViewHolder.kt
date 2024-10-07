package com.sgalera.gaztelubira.ui.matches.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.databinding.ItemStatsBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

class StatsViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemStatsBinding.bind(view)

    fun render(playerModel: PlayerModel?, onPlayerSelected: (String) -> Unit) {
        Glide.with(itemView.context)
            .load(playerModel?.img)
            .into(binding.ivStatPlayer)
        binding.tvStatPlayerName.text = playerModel?.name
        binding.parent.setOnClickListener{
            onPlayerSelected(playerModel?.name ?: "")
        }
    }
}
