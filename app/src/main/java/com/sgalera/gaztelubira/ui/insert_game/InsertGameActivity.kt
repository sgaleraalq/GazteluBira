package com.sgalera.gaztelubira.ui.insert_game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.*
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.AWAY
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.HOME
import com.sgalera.gaztelubira.ui.insert_game.MatchType.CUP
import com.sgalera.gaztelubira.ui.insert_game.MatchType.LEAGUE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InsertGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertGameBinding
    private val insertGameViewModel by viewModels<InsertGameViewModel>()
    private var id: Int = 0
    private var journey: Int = 0

    companion object {
        private const val ID = "id"
        private const val LEAGUE_JOURNEY = "leagueJourney"
        fun create(context: Context, id: Int, leagueJourney: Int) = Intent(context, InsertGameActivity::class.java).apply {
            putExtra(ID, id)
            putExtra(LEAGUE_JOURNEY, leagueJourney)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the passed arguments
        id = intent.getIntExtra(ID, 0)
        journey = intent.getIntExtra(LEAGUE_JOURNEY, 0)
        Log.i("InsertGameActivity", "id: $id, journey: $journey")
        initUI()
    }

    private fun initUI() {
        initExpandable()
        initListeners()

    }

    private fun initExpandable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                insertGameViewModel.matchType.collect { matchType ->
                    when (matchType) {
                        LEAGUE -> { setMatchType(binding.btnLeague, matchType) }
                        CUP -> { setMatchType(binding.btnCup, matchType) }
                        null -> { deselectMatchType() }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                insertGameViewModel.matchLocal.collect { matchType ->
                    when (matchType) {
                        HOME -> { setMatchType(binding.btnLocal) }
                        AWAY -> { setMatchType(binding.btnVisitor) }
                        null -> { deselectMatchLocal() }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                insertGameViewModel.expandable.collect { expandable ->
                    when (expandable) {
                        MATCH_TYPE -> { showItem(binding.ivArrowMatchType, binding.llMatchType) }
                        MATCH_LOCAL -> { showItem(binding.ivArrowMatchLocal, binding.llMatchLocal) }
                        RESULT -> { showItem(binding.ivArrowResult, null, binding.clResult) }
                        PLAYERS -> { showItem(binding.ivArrowPlayers, null, binding.clPlayers) }
                        null -> { deselectAll() }
                    }
                }
            }
        }
    }

    private fun initListeners(){
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Match buttons
        binding.btnLeague.setOnClickListener { insertGameViewModel.onMatchTypeSelected(LEAGUE) }
        binding.btnCup.setOnClickListener { insertGameViewModel.onMatchTypeSelected(CUP) }
        binding.btnLocal.setOnClickListener { insertGameViewModel.onMatchLocalSelected(HOME) }
        binding.btnVisitor.setOnClickListener { insertGameViewModel.onMatchLocalSelected(AWAY) }

        // Expandable buttons
        binding.cvMatchType.setOnClickListener{ insertGameViewModel.onExpandableChanged(MATCH_TYPE) }
        binding.cvMatchLocal.setOnClickListener{ insertGameViewModel.onExpandableChanged(MATCH_LOCAL) }
        binding.cvResult.setOnClickListener{ insertGameViewModel.onExpandableChanged(RESULT) }
        binding.cvPlayers.setOnClickListener{ insertGameViewModel.onExpandableChanged(PLAYERS) }
    }

    private fun showItem(arrow: ImageView, childView: LinearLayout?, childConstraint: ConstraintLayout? = null) {
        deselectAll()
        arrow.rotation = if (arrow.rotation == 0f) 90f else 0f

        if (childView != null){
            childView.visibility = if (childView.visibility == LinearLayout.VISIBLE) LinearLayout.GONE else LinearLayout.VISIBLE
            childView.setBackgroundColor(resources.getColor(R.color.primary, null))
        } else {
            childConstraint!!.visibility = if (childConstraint.visibility == ConstraintLayout.VISIBLE) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            childConstraint.setBackgroundColor(resources.getColor(R.color.primary, null))
        }

    }


    private fun setMatchType(btn: MaterialButton, type: MatchType? = null) {
        if (type != null) deselectMatchType() else deselectMatchLocal()
        btn.setBackgroundColor(resources.getColor(R.color.green, null))
    }

    private fun deselectMatchType() {
        binding.btnLeague.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        binding.btnCup.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
    }

    private fun deselectMatchLocal(){
        binding.btnLocal.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        binding.btnVisitor.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
    }

    private fun deselectAll() {
        binding.ivArrowMatchType.rotation = 0f
        binding.ivArrowMatchLocal.rotation = 0f
        binding.ivArrowResult.rotation = 0f
        binding.llMatchType.visibility = LinearLayout.GONE
        binding.llMatchLocal.visibility = LinearLayout.GONE
        binding.clResult.visibility = LinearLayout.GONE
        binding.clPlayers.visibility = LinearLayout.GONE
    }
}


