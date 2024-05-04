package com.sgalera.gaztelubira.ui.team.adapters

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemPlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class PlayersViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val binding = ItemPlayersBinding.bind(view)
    fun render(playerInformation: PlayerInformation, onItemSelected: (Int) -> Unit) {
        if (playerInformation.dorsal != null) {
            binding.tvPlayerDorsal.text = playerInformation.dorsal.toString()
        } else {
            binding.tvPlayerDorsal.visibility = View.GONE
        }
        binding.tvPlayerName.text = playerInformation.name
        binding.tvPlayerPosition.text = playerInformation.position

        binding.ivPlayer.loadImage(playerInformation.img)
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this)
    }
}
