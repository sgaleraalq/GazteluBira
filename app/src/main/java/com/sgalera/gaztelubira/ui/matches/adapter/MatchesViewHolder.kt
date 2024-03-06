package com.sgalera.gaztelubira.ui.matches.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.matches.Match

class MatchesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)
    fun render(match: Match) {
//        // Local variables
//        binding.ivLocalTeam.setImageResource(match.visitor.img)
//        binding.tvLocalName.text = match.local.name
        binding.tvGoalsLocal.text = match.localGoals.toString()

        // Visitor variables
//        binding.ivVisitorTeam.setImageResource(match.visitor.img)
//        binding.tvVisitorName.text = match.visitor.name
        binding.tvGoalsVisitor.text = match.visitorGoals.toString()
    }
}
