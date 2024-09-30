package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.databinding.ItemPlayerComparisonBinding
import com.sgalera.gaztelubira.domain.model.PlayerModel

class PlayerComparisonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerComparisonBinding.bind(view)

    fun render(playerModel: PlayerModel?, onPlayerSelected: (PlayerModel) -> Unit) {
        binding.tvPlayerName.text = playerModel?.name
        Glide.with(itemView).load(playerModel?.img).into(binding.ivPlayerImage)
    }
}
