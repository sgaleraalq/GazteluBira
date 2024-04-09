package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.databinding.ItemPlayerPopupBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

class PopUpViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerPopupBinding.bind(view)
    fun render(player: PlayerInfo) {
        binding.tvPlayerName.text = player.name
        binding.ivPlayerImage.setImageResource(player.img)
    }
}
