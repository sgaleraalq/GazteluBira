    package com.sgalera.gaztelubira.ui.player_compare.adapter

    import android.view.View
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.sgalera.gaztelubira.databinding.ItemPlayerPopupBinding
    import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

    class PopUpViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemPlayerPopupBinding.bind(view)
        fun render(player: PlayerInformation) {
            binding.tvPlayerName.text = player.name

            Glide.with(view)
                .load(player.img)
                .into(binding.ivPlayerImage)
        }
    }
