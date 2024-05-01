package com.sgalera.gaztelubira.ui.matches.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)

    fun render(match: MatchInfo, onItemSelected: (Int) -> Unit) {
        val localTeam = match.homeTeam!!
        val awayTeam = match.awayTeam!!

        Glide.with(binding.root)
            .load(localTeam.img)
            .into(binding.ivLocalTeam)

        binding.tvLocalName.text = localTeam.name
        binding.tvGoalsLocal.text = match.homeGoals.toString()

        Glide.with(binding.root)
            .load(awayTeam.img)
            .into(binding.ivVisitorTeam)

        binding.tvVisitorName.text = awayTeam.name
        binding.tvGoalsVisitor.text = match.awayGoals.toString()

        binding.tvJourney.text = match.journey

        setCardBackgroundColor(match, localTeam, awayTeam)

        if (match.match == "copa") {
            binding.ivMatchType.setImageResource(R.drawable.ic_cup)
        } else {
            binding.ivMatchType.setImageResource(0)
        }

        binding.parent.setOnClickListener {
            onItemSelected(match.id)
        }
    }

    private fun setCardBackgroundColor(match: MatchInfo, localTeam: TeamInformation, awayTeam: TeamInformation) {
        if (match.homeGoals == match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.lemon_chiffon))
        } else if (localTeam.name == "Gaztelu Bira" && match.homeGoals > match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else if (awayTeam.name == "Gaztelu Bira" && match.awayGoals > match.homeGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.indian_red))
        }
    }

}
