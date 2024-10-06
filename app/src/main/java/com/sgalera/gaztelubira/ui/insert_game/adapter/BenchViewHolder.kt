package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.core.Constants.PLAYER_NO_IMAGE
import com.sgalera.gaztelubira.databinding.ItemPlayerSelectionBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

class BenchViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerSelectionBinding.bind(view)

    fun render(player: PlayerModel?, onCancelSelected: (String) -> Unit) {
        binding.ivCancel.visibility = View.VISIBLE
        binding.ivCancel.setOnClickListener { onCancelSelected(player?.name ?: "") }
        binding.tvPlayerName.text = player?.name
        binding.pbLoadingPlayer.visibility = View.GONE
        binding.tvDorsal.text = player?.dorsal.toString()
        Glide.with(itemView.context)
            .load(player?.img ?: PLAYER_NO_IMAGE)
            .into(binding.ivPlayer)
    }
}
