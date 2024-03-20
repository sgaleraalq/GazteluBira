package com.sgalera.gaztelubira.ui.insert_game.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo

class InsertGameAdapter(
    private val benchList: List<PlayerInfo>
): RecyclerView.Adapter<InsertGameViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InsertGameViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: InsertGameViewHolder, position: Int) {
        holder.render(benchList[position])
    }

    override fun getItemCount() = benchList.size

}
