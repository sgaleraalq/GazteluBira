package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemPlayerSelectionBinding
import com.sgalera.gaztelubira.domain.model.PlayerModel

class PlayerComparisonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerSelectionBinding.bind(view)

    fun render(playerModel: PlayerModel?, onPlayerSelected: (PlayerModel) -> Unit) {
        binding.tvPlayerName.text = playerModel?.name
        Glide.with(itemView).load(playerModel?.img).into(binding.ivPlayer)
        binding.tvDorsal.text = playerModel?.dorsal.toString()
        if (playerModel?.selected == true){
            binding.parent.setBackgroundResource(R.color.antique_white)
            binding.tvPlayerName.setTextColor(itemView.resources.getColor(R.color.black, null))
            binding.tvDorsal.setTextColor(itemView.resources.getColor(R.color.black, null))
        } else {
            binding.parent.setBackgroundResource(R.color.primary_soft)
            binding.tvPlayerName.setTextColor(itemView.resources.getColor(R.color.antique_white, null))
            binding.tvDorsal.setTextColor(itemView.resources.getColor(R.color.antique_white, null))
        }
        loadImage(playerModel?.img)
        itemView.setOnClickListener {
            playerModel?.let { onPlayerSelected(it) }
        }
    }

    private fun loadImage(img: String?) {
        Glide.with(binding.root)
            .load(img ?: "")
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pbLoadingPlayer.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pbLoadingPlayer.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivPlayer)
    }
}
