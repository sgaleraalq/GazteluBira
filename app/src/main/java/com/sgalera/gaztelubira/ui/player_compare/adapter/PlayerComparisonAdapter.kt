package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerModel

class PlayerComparisonAdapter(
    var playersList: MutableList<PlayerModel?> = mutableListOf(),
    private val onPlayerSelected: (PlayerModel) -> Unit
) : RecyclerView.Adapter<PlayerComparisonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerComparisonViewHolder {
        return PlayerComparisonViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_player_selection, parent, false)
        )
    }

    override fun getItemCount() = playersList.size

    override fun onBindViewHolder(holder: PlayerComparisonViewHolder, position: Int) {
        holder.render(playersList[position]) { player -> onPlayerSelected(player) }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<PlayerModel?>) {
        playersList = newList
        notifyDataSetChanged()
    }

    fun updatePlayer(players: Pair<Int?, Int?>) {
        players.first?.let { index ->
            playersList[index]?.selected = true
            notifyItemChanged(index)
        }
        players.second?.let { index ->
            playersList[index]?.selected = false
            notifyItemChanged(index)
        }
    }
}