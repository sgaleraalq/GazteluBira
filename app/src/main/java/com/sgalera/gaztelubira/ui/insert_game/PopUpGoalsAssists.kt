package com.sgalera.gaztelubira.ui.insert_game

import androidx.fragment.app.DialogFragment
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameDetailBinding
import com.sgalera.gaztelubira.domain.model.Team

class PopUpGoalsAssists(
    private val localTeam: Team,
    private val awayTeam: Team,
    private var goals: Int,
    private var awayGoals: Int,
    private val binding: ActivityInsertGameDetailBinding
): DialogFragment() {
    init {
        if (localTeam.name == R.string.gaztelu_bira) {
            goals = binding.etLocalGoals.text.toString().toInt()
            awayGoals = binding.etAwayGoals.text.toString().toInt()
        } else {
            goals = binding.etAwayGoals.text.toString().toInt()
            awayGoals = binding.etLocalGoals.text.toString().toInt()
        }

    }
}