package com.sgalera.gaztelubira.ui.stats.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemStatsFragmentBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.ui.stats.StatType

class PlayerStatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding: ItemStatsFragmentBinding = ItemStatsFragmentBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(
        player: PlayerStatsModel?,
        position: Int,
        statSelected: StatType,
        onPlayerSelected: () -> Unit
    ) {
        binding.tvPlayerName.text = player?.information?.name
        if (position == 1) {
            binding.ivLaurel.visibility = View.VISIBLE
            binding.tvLaurelPosition.visibility = View.VISIBLE
            binding.ivLaurel.setColorFilter(getColor(itemView.context,R.color.champion_gold_primary))
            binding.tvLaurelPosition.setTextColor(getColor(itemView.context,R.color.champion_gold_primary))
            binding.tvLaurelPosition.text = "1"
        } else if(position == 2) {
            binding.ivLaurel.visibility = View.VISIBLE
            binding.tvLaurelPosition.visibility = View.VISIBLE
            binding.ivLaurel.setColorFilter(getColor(itemView.context,R.color.second_silver_center))
            binding.tvLaurelPosition.setTextColor(getColor(itemView.context,R.color.second_silver_center))
            binding.tvLaurelPosition.text = "2"
        } else if(position == 3) {
            binding.ivLaurel.visibility = View.VISIBLE
            binding.tvLaurelPosition.visibility = View.VISIBLE
            binding.ivLaurel.setColorFilter(getColor(itemView.context,R.color.third_bronze_primary))
            binding.tvLaurelPosition.setTextColor(getColor(itemView.context,R.color.third_bronze_primary))
            binding.tvLaurelPosition.text = "3"
        } else{
            binding.ivLaurel.visibility = View.INVISIBLE
            binding.tvLaurelPosition.text = position.toString()
            binding.tvLaurelPosition.setTextColor(getColor(itemView.context,R.color.white))
        }

        if (statSelected == StatType.PERCENTAGE) {
            binding.ivArrow.visibility = View.VISIBLE
        } else {
            binding.ivArrow.visibility = View.GONE
        }

        when (statSelected) {
            StatType.PERCENTAGE -> binding.tvPlayerStat.text = player?.percentage.toString()
            StatType.GOALS -> binding.tvPlayerStat.text = player?.goals.toString()
            StatType.ASSISTS -> binding.tvPlayerStat.text = player?.assists.toString()
            StatType.PENALTIES -> binding.tvPlayerStat.text = player?.penalties.toString()
            StatType.CLEAN_SHEET -> binding.tvPlayerStat.text = player?.cleanSheet.toString()
            StatType.GAMES_PLAYED -> binding.tvPlayerStat.text = player?.gamesPlayed.toString()
        }

        if (player != null && player.ranking < player.lastRanking) {
            binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
        } else if (player != null && player.ranking > player.lastRanking) {
            binding.ivArrow.setImageResource(R.drawable.ic_arrow_down)
        } else {
            binding.ivArrow.setImageResource(R.drawable.ic_arrow_equal)
        }

        binding.parent.setOnClickListener { onPlayerSelected() }
    }
}
