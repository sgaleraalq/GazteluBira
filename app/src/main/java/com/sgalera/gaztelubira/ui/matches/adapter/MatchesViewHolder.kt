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
import com.sgalera.gaztelubira.domain.model.TeamInformation
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)

    fun render(match: MatchInfo, onItemSelected: (Int) -> Unit) {
        val localTeam = match.homeTeam!!
        val awayTeam = match.awayTeam!!

        manageImages(localTeam, awayTeam)

        binding.tvLocalName.text = localTeam.name
        binding.tvGoalsLocal.text = match.homeGoals.toString()
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
