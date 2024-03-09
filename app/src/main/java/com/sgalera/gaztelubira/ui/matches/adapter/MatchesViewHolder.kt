package com.sgalera.gaztelubira.ui.matches.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)
    fun render(match: MatchInfo, onItemSelected: (Int) -> Unit) {
        val context = binding.tvLocalName.context
        val localTeam = getTeam(match.homeTeam)
        val awayTeam = getTeam(match.awayTeam)

        binding.ivLocalTeam.setImageResource(getImage(localTeam))
        binding.tvLocalName.text = context.getString(localTeam.name)
        binding.tvGoalsLocal.text = match.homeGoals.toString()

        binding.ivVisitorTeam.setImageResource(getImage(awayTeam))
        binding.tvVisitorName.text = context.getString(awayTeam.name)
        binding.tvGoalsVisitor.text = match.awayGoals.toString()

        setCardBackgroundColor(match)

        if (match.awayTeam == "Lezkairu") {
            Log.d("sgalera", "Match type: ${match.match}")
            binding.ivMatchType.setImageResource(R.drawable.ic_cup)
        }

        binding.parent.setOnClickListener {
            onItemSelected(match.id)
        }
    }

    private fun setCardBackgroundColor(match: MatchInfo) {
        if (match.homeGoals == match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.lemon_chiffon))
        } else if (match.homeTeam == "Gaztelu Bira" && match.homeGoals > match.awayGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else if (match.awayTeam == "Gaztelu Bira" && match.awayGoals > match.homeGoals) {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.green))
        } else {
            binding.parent.setCardBackgroundColor(binding.parent.context.getColor(R.color.indian_red))
        }
    }

    private fun getTeam(homeTeam: String): Team {
        return when (homeTeam) {
            "Gaztelu Bira" -> GazteluBira
            "Anaitasuna" -> Anaitasuna
            "Arsenal" -> Arsenal
            "Aterbea" -> Aterbea
            "ESIC Gazteak" -> EsicGazteak
            "Esmeraldeños" -> Esmeraldenos
            "Garre" -> Garre
            "Iturrama" -> Iturrama
            "IZN" -> Izn
            "La Unica" -> LaUnica
            "Peña School" -> PenaSchool
            "San Cristobal" -> SanCristobal
            "Lezkairu" -> Lezkairu
            else -> Tingla2Legens
        }
    }

    private fun getImage(team: Team): Int {
        return when (team) {
            GazteluBira -> R.drawable.img_gaztelu_bira
            Anaitasuna -> R.drawable.img_anaitasuna
            Arsenal -> R.drawable.img_arsenal
            Aterbea -> R.drawable.img_aterbea
            EsicGazteak -> R.drawable.img_esic_gazteak
            Esmeraldenos -> R.drawable.img_esmeraldenos
            Garre -> R.drawable.img_garre
            Iturrama -> R.drawable.img_iturrama
            Izn -> R.drawable.img_izn
            LaUnica -> R.drawable.img_la_unica
            PenaSchool -> R.drawable.img_pena_school
            SanCristobal -> R.drawable.img_san_cristobal
            Lezkairu -> R.drawable.img_lezkairu
            else -> R.drawable.img_no_football_shield
        }
    }

}
