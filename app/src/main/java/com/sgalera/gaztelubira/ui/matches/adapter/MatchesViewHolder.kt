package com.sgalera.gaztelubira.ui.matches.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.core.Constants.TEAM_NO_IMAGE
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.TeamModel
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)

    fun render(match: MatchModel, onItemSelected: (Int) -> Unit) {
        // Texts
        binding.tvLocalName.text = match.localTeam?.teamName ?: ""
        binding.tvVisitorName.text = match.visitorTeam?.teamName ?: ""
        binding.tvGoalsLocal.text = match.homeGoals.toString()
        binding.tvGoalsVisitor.text = match.awayGoals.toString()
        binding.tvJourney.text = match.journey

        // Colors
        binding.ivMatchType.setImageResource(if (match.match == "copa") R.drawable.ic_cup else 0)
        setCardBackgroundColor(match, match.localTeam, match.visitorTeam)

        // Images
        manageImages(match.localTeam, match.visitorTeam)

        // Click
        binding.parent.setOnClickListener { onItemSelected(match.id) }
    }

    private fun manageImages(localTeam: TeamModel?, awayTeam: TeamModel?) {
        loadTeamImage(
            teamImg = localTeam?.teamImg ?: TEAM_NO_IMAGE,
            progressBar = binding.pbLocalTeam,
            imageView = binding.ivLocalTeam
        )

        loadTeamImage(
            teamImg = awayTeam?.teamImg ?: TEAM_NO_IMAGE,
            progressBar = binding.pbVisitorTeam,
            imageView = binding.ivVisitorTeam
        )
    }

    private fun loadTeamImage(
        teamImg: String,
        progressBar: View,
        imageView: ImageView
    ) {
        Glide.with(binding.root)
            .load(teamImg)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    imageView.setImageResource(R.drawable.img_no_football_shield)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)
    }

    private fun setCardBackgroundColor(match: MatchModel, localTeam: TeamModel?, awayTeam: TeamModel?) {
        val color = when {
            match.homeGoals == match.awayGoals -> R.color.lemon_chiffon
            localTeam?.teamName == "Gaztelu Bira" && match.homeGoals > match.awayGoals -> R.color.green
            awayTeam?.teamName == "Gaztelu Bira" && match.awayGoals > match.homeGoals -> R.color.green
            else -> R.color.indian_red
        }
        binding.parent.setCardBackgroundColor(binding.parent.context.getColor(color))
    }
}
