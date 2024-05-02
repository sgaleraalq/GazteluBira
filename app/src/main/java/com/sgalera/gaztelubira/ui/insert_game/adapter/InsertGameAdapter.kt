package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.ui.insert_game.PlayerAddListener

class InsertGameAdapter(
    private var benchList: MutableList<PlayerInformation> = mutableListOf(),
    private val playerAddListener: PlayerAddListener
): RecyclerView.Adapter<InsertGameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsertGameViewHolder {
        return InsertGameViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_popup, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InsertGameViewHolder, position: Int) {
        holder.render(benchList[position])
        val view = holder.itemView
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT
        view.layoutParams = layoutParams

        view.findViewById<ImageView>(R.id.ivCancel).setOnClickListener {
            playerAddListener.onPlayerAdded(benchList[position])
            benchList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = benchList.size

    fun addPlayer(player: PlayerInformation) {
        benchList.add(player)
        notifyItemInserted(benchList.size - 1)
    }
}
