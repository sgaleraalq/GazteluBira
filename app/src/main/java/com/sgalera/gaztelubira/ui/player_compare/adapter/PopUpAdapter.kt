package com.sgalera.gaztelubira.ui.player_compare.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation

class PopUpAdapter(
    private var playerList: List<PlayerInformation> = emptyList(),
    private var selectedPlayers: MutableList<PlayerInformation>,
    private val showDoneButton: () -> Unit,
    private val hideDoneButton: () -> Unit
) : RecyclerView.Adapter<PopUpViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        return PopUpViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_player_popup, parent, false)
        )
    }

    override fun getItemCount() = playerList.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        val player = playerList[position]
        holder.render(player)

        holder.itemView.layoutParams.width = 100.dpToPx(holder.itemView.context)
        holder.itemView.layoutParams.height = 100.dpToPx(holder.itemView.context)

        // Marcar la vista como seleccionada si el jugador está en la lista de seleccionados
        if (selectedPlayers.contains(player)) {
            selectPlayer(holder.itemView)
        } else {
            unSelectPlayer(holder.itemView)
        }
        holder.itemView.setOnClickListener {
            dealWithSelection(holder, player)
        }
    }

    private fun dealWithSelection(holder: PopUpViewHolder, player: PlayerInformation) {
        if (selectedPlayers.contains(player)) {
            unSelectPlayer(holder.itemView)
            selectedPlayers.remove(player)
            player.selected = false
        } else {
            // Si ya hay dos jugadores seleccionados, eliminamos el primero
            if (selectedPlayers.size == 2) {
                val firstSelectedPlayer = selectedPlayers[0]
                firstSelectedPlayer.selected = false
                selectedPlayers.removeAt(0)
                notifyItemChanged(playerList.indexOf(firstSelectedPlayer))
            }

            // Seleccionamos el nuevo jugador
            selectPlayer(holder.itemView)
            selectedPlayers.add(player)
            player.selected = true
        }

        if (selectedPlayers.size == 2) {
            showDoneButton()
        } else {
            hideDoneButton()
        }
    }

    private fun selectPlayer(playerView: View) {
        playerView.findViewById<ConstraintLayout>(R.id.clPlayerPopUp)
            .setBackgroundResource(R.color.white_80_opacity)
        playerView.findViewById<TextView>(R.id.tvPlayerName)
            .setTextColor(ContextCompat.getColor(playerView.context,R.color.black))
        playerView.findViewById<ImageView>(R.id.ivCheck).visibility = View.VISIBLE
    }

    private fun unSelectPlayer(playerView: View) {
        playerView.findViewById<ConstraintLayout>(R.id.clPlayerPopUp)
            .setBackgroundResource(R.color.primary_dark)
        playerView.findViewById<TextView>(R.id.tvPlayerName)
            .setTextColor(ContextCompat.getColor(playerView.context,R.color.white))
        playerView.findViewById<ImageView>(R.id.ivCheck).visibility = View.GONE
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<PlayerInformation>) {
        playerList = list
        notifyDataSetChanged()
    }

    private fun Int.dpToPx(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
}

