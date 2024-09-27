package com.sgalera.gaztelubira.ui.matches.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.MatchModel
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.TeamModel
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)

    fun render(match: MatchModel, onItemSelected: (Int) -> Unit) {
        binding.tvLocalName.text = match.localTeam?.teamName ?: ""
        binding.tvVisitorName.text = match.visitorTeam?.teamName ?: ""
        binding.tvGoalsLocal.text = match.homeGoals.toString()
        binding.tvGoalsVisitor.text = match.awayGoals.toString()
        binding.tvJourney.text = match.journey

        binding.ivMatchType.setImageResource(if (match.match == "copa") R.drawable.ic_cup else 0)
        setCardBackgroundColor(match, match.localTeam, match.visitorTeam)
//        manageImages(localTeam, awayTeam)
//

//
//        binding.tvJourney.text = match.journey
//
//        setCardBackgroundColor(match, localTeam, awayTeam)
//
//
//        binding.parent.setOnClickListener {
//            onItemSelected(match.id)
//        }
    }

    private fun manageImages(localTeam: TeamInformation, awayTeam: TeamInformation) {
        Glide.with(binding.root)
            .load(localTeam.img)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pbLocalTeam.visibility = View.GONE
                    binding.ivLocalTeam.setImageResource(R.drawable.img_no_football_shield)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Oculta la ProgressBar una vez que la imagen se haya cargado correctamente
                    binding.pbLocalTeam.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivLocalTeam)

        Glide.with(binding.root)
            .load(awayTeam.img)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.pbVisitorTeam.visibility = View.GONE
                    binding.ivVisitorTeam.setImageResource(R.drawable.img_no_football_shield)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Oculta la ProgressBar una vez que la imagen se haya cargado correctamente
                    binding.pbVisitorTeam.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivVisitorTeam)
    }

    private fun setCardBackgroundColor(match: MatchModel, localTeam: TeamModel?, awayTeam: TeamModel?) {
        if (match.homeGoals == match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.lemon_chiffon))
        } else if (localTeam?.teamName == "Gaztelu Bira" && match.homeGoals > match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else if (awayTeam?.teamName == "Gaztelu Bira" && match.awayGoals > match.homeGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.indian_red))
        }
    }

}
