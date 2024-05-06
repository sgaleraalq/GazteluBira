package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.databinding.ItemPlayerPopupBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class InsertGameViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerPopupBinding.bind(view)
    fun render(player: PlayerInformation) {
        binding.tvPlayerName.text = player.name
        Glide.with(itemView.context).load(player.img).into(binding.ivPlayerImage)
        binding.ivCancel.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
    }
}
