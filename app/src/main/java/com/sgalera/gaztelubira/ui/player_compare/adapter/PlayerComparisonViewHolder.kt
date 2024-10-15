package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemPlayerSelectionBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerPosition

class PlayerComparisonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerSelectionBinding.bind(view)

    private val gradients = listOf(
        Pair(R.color.first_gradient_start, R.color.first_gradient_end),
        Pair(R.color.second_gradient_start, R.color.second_gradient_end),
        Pair(R.color.third_gradient_start, R.color.third_gradient_end),
        Pair(R.color.fourth_gradient_start, R.color.fourth_gradient_end),
    )

    fun render(playerModel: PlayerModel?, onPlayerSelected: (PlayerModel) -> Unit) {
        applyGradientBackground(playerModel?.position)

        playerModel?.let { player ->
            with(binding) {
                tvPlayerName.text = player.name
                tvDorsal.text = player.dorsal.toString()
                tvPlayerPosition.text = mapPosition(player.position)

                loadImage(player.img)

                updateSelectionState(player.selected)

                itemView.setOnClickListener {
                    onPlayerSelected(player)
                }
            }
        }
    }

    private fun updateSelectionState(isSelected: Boolean) {
        with(binding) {
            if (isSelected) {
                clTextViewBackground.setBackgroundResource(R.color.antique_white)
                setTextColor(R.color.black)
            } else {
                clTextViewBackground.setBackgroundResource(R.color.primary_soft)
                setTextColor(R.color.antique_white)
            }
        }
    }

    private fun setTextColor(colorResId: Int) {
        with(binding) {
            val color = itemView.resources.getColor(colorResId, null)
            tvPlayerName.setTextColor(color)
            tvDorsal.setTextColor(color)
            tvPlayerPosition.setTextColor(color)
        }
    }

    private fun applyGradientBackground(position: PlayerPosition?) {
        val gradient = when (position){
            PlayerPosition.GOALKEEPER -> gradients[0]
            PlayerPosition.DEFENDER -> gradients[1]
            PlayerPosition.MIDFIELDER -> gradients[2]
            PlayerPosition.FORWARD -> gradients[3]
            PlayerPosition.TECHNICAL_STAFF -> gradients[0]
            PlayerPosition.UNKNOWN -> gradients[0]
            null -> gradients[0]
        }

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                ContextCompat.getColor(binding.root.context, gradient.first),
                ContextCompat.getColor(binding.root.context, gradient.second)
            )
        )
        gradientDrawable.cornerRadius = 16f
        binding.cvBackground.background = gradientDrawable
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

    private fun mapPosition(position: PlayerPosition): String {
        return when (position) {
            PlayerPosition.GOALKEEPER -> binding.root.context.getString(R.string.goalkeeper)
            PlayerPosition.DEFENDER -> binding.root.context.getString(R.string.defender)
            PlayerPosition.MIDFIELDER -> binding.root.context.getString(R.string.midfielder)
            PlayerPosition.FORWARD -> binding.root.context.getString(R.string.forward)
            PlayerPosition.TECHNICAL_STAFF -> "?"
            PlayerPosition.UNKNOWN -> "?"
        }
    }
}
