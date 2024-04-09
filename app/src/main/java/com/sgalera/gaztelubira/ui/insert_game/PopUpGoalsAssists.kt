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
        return dialogView
    }

    private fun initComponents(dialogView: AlertDialog, view: View) {
        if (goals == 0 && awayGoals > 0){
            // TODO THIS PART
            Toast.makeText(binding.root.context, "Game inserted", Toast.LENGTH_SHORT).show()
            dialogView.dismiss()
        } else if (goals == 0 && awayGoals == 0){
            view.findViewById<TextView>(R.id.tvCleanSheet).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.llCleanSheet).visibility = View.GONE
        } else if (goals > 0 && awayGoals > 0) {
            view.findViewById<TextView>(R.id.tvGoals).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.llGoals).visibility = View.GONE
            view.findViewById<TextView>(R.id.tvAssists).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.llAssists).visibility = View.GONE
            view.findViewById<TextView>(R.id.tvPenalties).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.llPenalties).visibility = View.GONE
            addGoalsItem(view)
        } else {
            println("hajñlafdskf")
        }
    }

    private fun addGoalsItem(view: View) {
        for ( goal in 1..goals){
            val layoutItem = createLayoutItem()
            view.findViewById<LinearLayout>(R.id.llGoals).addView(layoutItem)
        }
    }

    private fun createLayoutItem(): View {
        return requireActivity().layoutInflater.inflate(R.layout.item_add_goal_or_assist, null)
    }
}