package com.sgalera.gaztelubira.ui.insert_game

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.item_popup_goals_assists, null)
        builder.setView(view)
        val dialogView = builder.create()
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initComponents(dialogView, view)
        println("Goals: $goals, AwayGoals: $awayGoals")
        return dialogView
    }

    private fun initComponents(dialogView: AlertDialog, view: View) {
        if (goals > 0){
            view.findViewById<ConstraintLayout>(R.id.clGoals).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.clAssists).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.clPenalties).visibility = View.VISIBLE
        } else if (awayGoals == 0){
            view.findViewById<ConstraintLayout>(R.id.clCleanSheet).visibility = View.VISIBLE
        } else {
            // TODO insert game
            Toast.makeText(binding.root.context, "Game inserted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addGoalsItem(view: View) {
        for (goal in 1..goals){
            val layoutItem = createLayoutItem()
            println(goal)
            view.findViewById<LinearLayout>(R.id.llGoals).addView(layoutItem)
        }
    }

    private fun createLayoutItem(): View {
        return requireActivity().layoutInflater.inflate(R.layout.item_add_goal_or_assist, null)
    }
}