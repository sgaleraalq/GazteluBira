package com.sgalera.gaztelubira.ui.matches.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.domain.model.matches.Match

class MatchesAdapter(private var matchesList: List<Match> = emptyList()) :
    RecyclerView.Adapter<MatchesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        return MatchesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_matches, parent, false)
        )
    }

    override fun getItemCount() = matchesList.size

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.render(matchesList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Match>) {
        matchesList = list
        notifyDataSetChanged()
    }
}
