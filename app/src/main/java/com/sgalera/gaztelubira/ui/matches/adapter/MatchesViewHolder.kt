package com.sgalera.gaztelubira.ui.matches.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ItemMatchesBinding
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.matches.MatchInfo

class MatchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMatchesBinding.bind(view)
    fun render(match: MatchInfo, onItemSelected: (Int) -> Unit) {
//        val context = binding.tvLocalName.context
//
//        // Local variables
//        binding.ivLocalTeam.setImageResource(getImage(match.local))
//        binding.tvLocalName.text = context.getString(match.local.name)
//        binding.tvGoalsLocal.text = match.localGoals.toString()
//
//        // Visitor variables
//        binding.ivVisitorTeam.setImageResource(getImage(match.visitor))
//        binding.tvVisitorName.text = context.getString(match.visitor.name)
//        binding.tvGoalsVisitor.text = match.visitorGoals.toString()
//
//        binding.parent.setOnClickListener {
//            onItemSelected(match.id)
//        }
    }

    private fun getImage(team: Team): Int {
        return when(team) {
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
            else -> R.drawable.img_no_football_shield
        }
    }

}
