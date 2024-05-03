package com.sgalera.gaztelubira.ui.team.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.databinding.ItemPlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class PlayersViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val binding = ItemPlayersBinding.bind(view)
    fun render(playerInformation: PlayerInformation, onItemSelected: (Int) -> Unit) {
        binding.tvPlayerDorsal.text = playerInformation.dorsal.toString()
        binding.tvPlayerName.text = playerInformation.name
        binding.tvPlayerPosition.text = playerInformation.position
        Glide.with(binding.root)
            .load(playerInformation.img)
            .into(binding.ivPlayer)
    }
}
