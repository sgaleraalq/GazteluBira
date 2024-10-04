package com.sgalera.gaztelubira.ui.insert_game

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
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

    private var inventedList = listOf(
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
        Pair("Gaztelu Bira", "https://firebasestorage.googleapis.com/v0/b/gaztelubira-2067b.appspot.com/o/teams%2Fimg_gaztelu_bira.webp?alt=media&token=6bb7082e-3dea-414b-831c-c60857a155c6"),
    ) // TODO

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
                        LEAGUE -> { setMatchType(binding.btnLeague) }
                        CUP -> { setMatchType(binding.btnCup) }
                        null -> { deselectMatchType() }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                insertGameViewModel.matchLocal.collect { matchLocal ->
                    when (matchLocal) {
                        HOME -> { setMatchType(binding.btnLocal, matchLocal) }
                        AWAY -> { setMatchType(binding.btnVisitor, matchLocal) }
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
                        STARTERS -> { showItem(binding.ivArrowPlayers, null, binding.clStarters) }
                        BENCH -> { showItem(binding.ivArrowBench, binding.llBench) }
                        STATS -> { showItem(binding.ivArrowStats, binding.llStats) }
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
        binding.cvStarters.setOnClickListener{ insertGameViewModel.onExpandableChanged(STARTERS) }
        binding.cvBench.setOnClickListener{ insertGameViewModel.onExpandableChanged(BENCH) }
        binding.cvStats.setOnClickListener{ insertGameViewModel.onExpandableChanged(STATS) }
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


    private fun setMatchType(btn: MaterialButton, local: MatchLocal? = null) {
        if (local != null){
            // Local / Visitor team inserted
            deselectMatchLocal()
            setLocalVisitor(local)
        } else{
            deselectMatchType()
        }
        btn.setBackgroundColor(resources.getColor(R.color.green, null))
    }

    private fun setLocalVisitor(local: MatchLocal) {
        binding.tvSetLocal.visibility = View.GONE
        when (local) {
            HOME -> {
                binding.ivLocal.setImageResource(R.drawable.img_gaztelu_bira)
                binding.tvLocalTeam.text = resources.getString(R.string.gaztelu_bira)
                binding.ivVisitor.setImageResource(R.drawable.img_no_football_shield)
                binding.tvVisitorTeam.text = resources.getString(R.string.select_team)
                binding.ivVisitor.setOnClickListener { showDialog(inventedList, getString(R.string.select_team)) }
            }
            AWAY -> {
                binding.ivVisitor.setImageResource(R.drawable.img_gaztelu_bira)
                binding.tvVisitorTeam.text = resources.getString(R.string.gaztelu_bira)
                binding.ivLocal.setImageResource(R.drawable.img_no_football_shield)
                binding.tvLocalTeam.text = resources.getString(R.string.select_team)
                binding.ivLocal.setOnClickListener { showDialog(inventedList, getString(R.string.select_team)) }
            }
        }
    }

    private fun showDialog(dialogList: List<Pair<String, String>>, dialogTitle: String) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        with(builder){
            setView(view)
            create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
                view.findViewById<TextView>(R.id.tvDialogTitle).text = dialogTitle
                setDialogGridLayout(view.findViewById(R.id.glDialog), dialogList)
            }
        }
    }

    private fun setDialogGridLayout(view: GridLayout, dialogList: List<Pair<String, String>>) {
        dialogList.forEach {
            val item = LayoutInflater.from(this).inflate(R.layout.item_dialog, null)
            item.findViewById<TextView>(R.id.tvDialog).text = it.first
            Glide.with(this).load(it.second).into(item.findViewById(R.id.ivDialog))
            view.addView(item)
        }
    }

    private fun deselectMatchType() {
        binding.btnLeague.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        binding.btnCup.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
    }

    private fun deselectMatchLocal(){
        binding.tvSetLocal.visibility = View.VISIBLE
        binding.tvLocalTeam.text = ""
        binding.tvVisitorTeam.text = ""
        binding.btnLocal.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        binding.btnVisitor.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
    }

    private fun deselectAll() {
        binding.ivArrowMatchType.rotation = 0f
        binding.ivArrowMatchLocal.rotation = 0f
        binding.ivArrowResult.rotation = 0f
        binding.ivArrowPlayers.rotation = 0f
        binding.ivArrowBench.rotation = 0f
        binding.ivArrowStats.rotation = 0f
        binding.llMatchType.visibility = LinearLayout.GONE
        binding.llMatchLocal.visibility = LinearLayout.GONE
        binding.clResult.visibility = LinearLayout.GONE
        binding.clStarters.visibility = LinearLayout.GONE
        binding.llBench.visibility = LinearLayout.GONE
        binding.llStats.visibility = LinearLayout.GONE
    }
}