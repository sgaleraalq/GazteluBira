package com.sgalera.gaztelubira.ui.matches.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.data.network.services.TeamsApiService
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MatchesAdapter(
    private var matchesList: List<MatchInfo> = emptyList(),
    private val onItemSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<MatchesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        return MatchesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_matches, parent, false)
        )
    }

    override fun getItemCount() = matchesList.size

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.render(matchesList[position], onItemSelected)
//        val match = matchesList[position]
//        CoroutineScope(Dispatchers.Main).launch {
//            val localTeam = teamsApiService.getTeam(match.homeTeam)
//            val awayTeam = teamsApiService.getTeam(match.awayTeam)
//            holder.render(match, localTeam, awayTeam, onItemSelected)
//        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<MatchInfo>) {
        matchesList = list
        notifyDataSetChanged()
    }
}
