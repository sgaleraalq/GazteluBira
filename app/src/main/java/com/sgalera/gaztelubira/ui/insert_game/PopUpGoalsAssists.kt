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
import androidx.core.view.marginStart
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

    private var scorers = mutableListOf<String>()
    private var assisters = mutableListOf<String>()
    private var penaltyScorers = mutableListOf<String>()
    private var emptySheeters = mutableListOf<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = requireActivity().layoutInflater.inflate(R.layout.item_popup_goals_assists, null)
        builder.setView(view)
        val dialogView = builder.create()
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initComponents(view)
        return dialogView
    }

    private fun initComponents(view: View) {
        if (goals > 0){
            view.findViewById<ConstraintLayout>(R.id.clGoals).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.clAssists).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.clPenalties).visibility = View.VISIBLE
            addGoalsItem(view)
            addAssistsItem(view)
        } else if (awayGoals == 0){
            view.findViewById<ConstraintLayout>(R.id.clCleanSheet).visibility = View.VISIBLE
        } else {
            // TODO insert game
            Toast.makeText(binding.root.context, "Game inserted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addGoalsItem(view: View) {
        val linearLayout = view.findViewById<LinearLayout>(R.id.llGoals)

        for (goal in 1..goals) {
            val layoutItem = createLayoutItem()

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val marginVertical = resources.getDimensionPixelSize(R.dimen.vertical_margin_between_items)
            params.setMargins(0, marginVertical, 0, marginVertical)

            layoutItem.layoutParams = params

            layoutItem.setOnClickListener {
                showPlayersPopUp("scorers")
            }
            linearLayout.addView(layoutItem)
        }
    }

    private fun addAssistsItem(view: View) {
        showPlayersPopUp("assisters")
    }
    private fun showPlayersPopUp(type: String) {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.create()
        val view = requireActivity().layoutInflater.inflate(R.layout.item_popup_insert_starter, null)

        when (type) {
            "scorers" -> {
                dialog.setTitle("Select scorer")
                dialog.setView(view)
                dialog.show()
            }
            "assisters" -> {
                dialog.setTitle("Select assister")
                dialog.setView(view)
                dialog.show()
            }
        }
    }

    private fun createLayoutItem(): View {
        return requireActivity().layoutInflater.inflate(R.layout.item_add_goal_or_assist, null)
    }
}