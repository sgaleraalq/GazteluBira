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
import com.sgalera.gaztelubira.databinding.ItemPlayerPopupBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class PopUpViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemPlayerPopupBinding.bind(view)

    fun render(player: PlayerInformation) {
        binding.tvPlayerName.text = player.name

        // Configurar Glide para cargar la imagen y ocultar la barra de progreso cuando se complete la carga
        Glide.with(view)
            .load(player.img)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pbLoading.visibility = View.GONE
                    binding.ivPlayerImage.setImageResource(R.drawable.img_no_profile_picture)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // La carga de la imagen se ha completado con éxito
                    // Oculta la barra de progreso aquí
                    binding.pbLoading.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivPlayerImage)
    }
}

  