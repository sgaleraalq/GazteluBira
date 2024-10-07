package com.sgalera.gaztelubira.ui.insert_game

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.*
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.databinding.ItemFootballFieldBinding
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.BENCH
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.MATCH_LOCAL
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.MATCH_TYPE
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.RESULT
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.STARTERS
import com.sgalera.gaztelubira.ui.insert_game.InsertGameExpandable.STATS
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.AWAY
import com.sgalera.gaztelubira.ui.insert_game.MatchLocal.HOME
import com.sgalera.gaztelubira.ui.insert_game.MatchType.CUP
import com.sgalera.gaztelubira.ui.insert_game.MatchType.LEAGUE
import com.sgalera.gaztelubira.ui.insert_game.PlayerPositions.*
import com.sgalera.gaztelubira.ui.insert_game.adapter.BenchAdapter
import com.sgalera.gaztelubira.ui.matches.adapter.ScorersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InsertGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertGameBinding
    private val insertGameViewModel by viewModels<InsertGameViewModel>()

    private lateinit var benchAdapter: BenchAdapter
    private lateinit var scorersAdapters: ScorersAdapter

    private var id: Int = 0
    private var journey: Int = 0

    companion object {
        private const val ID = "id"
        private const val LEAGUE_JOURNEY = "leagueJourney"
        fun create(context: Context, id: Int, leagueJourney: Int) =
            Intent(context, InsertGameActivity::class.java).apply {
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                insertGameViewModel.matchType.collect { matchType ->
                    when (matchType) {
                        LEAGUE -> setMatchType(binding.btnLeague)
                        CUP -> setMatchType(binding.btnCup)
                        null -> deselectMatchType()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                insertGameViewModel.matchLocal.collect { matchLocal ->
                    when (matchLocal) {
                        HOME -> setMatchType(binding.btnLocal, matchLocal)
                        AWAY -> setMatchType(binding.btnVisitor, matchLocal)
                        null -> deselectMatchLocal()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                insertGameViewModel.expandable.collect { expandable ->
                    when (expandable) {
                        MATCH_TYPE -> showItem(binding.ivArrowMatchType, binding.llMatchType)
                        MATCH_LOCAL -> showItem(binding.ivArrowMatchLocal, binding.llMatchLocal)
                        RESULT -> showItem(binding.ivArrowResult, null, binding.clResult)
                        STARTERS -> showItem(binding.ivArrowPlayers, null, null, binding.clStarters)
                        BENCH -> showItem(binding.ivArrowBench, binding.llBench)
                        STATS -> showItem(binding.ivArrowStats, binding.llStats)
                        null -> deselectAll()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                insertGameViewModel.benchPlayers.collect { benchPlayersList ->
                    benchAdapter = BenchAdapter(benchPlayersList, onCancelSelected = { playerName ->
                        insertGameViewModel.onBenchPlayerRemoved(playerName)
                    })
                    binding.rvBench.apply {
                        adapter = benchAdapter
                        layoutManager = LinearLayoutManager(
                            this@InsertGameActivity,
                            HORIZONTAL,
                            false
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                insertGameViewModel.scorers.collect { scorersList ->
                    scorersAdapters = ScorersAdapter(
                        scorersList,
                        onPlayerSelected = { insertGameViewModel.onScorerRemoved(it) }
                    )
                    binding.rvScorers.apply {
                        adapter = scorersAdapters
                        layoutManager =
                            LinearLayoutManager(this@InsertGameActivity, VERTICAL, false)
                    }
                }
            }
        }

    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Match buttons
        binding.btnLeague.setOnClickListener { insertGameViewModel.onMatchTypeSelected(LEAGUE) }
        binding.btnCup.setOnClickListener { insertGameViewModel.onMatchTypeSelected(CUP) }
        binding.btnLocal.setOnClickListener { insertGameViewModel.onMatchLocalSelected(HOME) }
        binding.btnVisitor.setOnClickListener { insertGameViewModel.onMatchLocalSelected(AWAY) }

        // Expandable buttons
        binding.cvMatchType.setOnClickListener { insertGameViewModel.onExpandableChanged(MATCH_TYPE) }
        binding.cvMatchLocal.setOnClickListener {
            insertGameViewModel.onExpandableChanged(
                MATCH_LOCAL
            )
        }
        binding.cvResult.setOnClickListener { insertGameViewModel.onExpandableChanged(RESULT) }
        binding.cvStarters.setOnClickListener { insertGameViewModel.onExpandableChanged(STARTERS) }
        binding.cvBench.setOnClickListener { insertGameViewModel.onExpandableChanged(BENCH) }
        binding.cvStats.setOnClickListener { insertGameViewModel.onExpandableChanged(STATS) }

        // Starters
        binding.clStarters.ivGoalKeeper.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_goalkeeper),
                null,
                GOAL_KEEPER
            )
        }
        binding.clStarters.ivLeftBack.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_left_back),
                null,
                LEFT_BACK
            )
        }
        binding.clStarters.ivRightBack.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_right_back),
                null,
                RIGHT_BACK
            )
        }
        binding.clStarters.ivLeftCentreBack.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_left_centre_back),
                null,
                LEFT_CENTRE_BACK
            )
        }
        binding.clStarters.ivRightCentreBack.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_right_centre_back),
                null,
                RIGHT_CENTRE_BACK
            )
        }
        binding.clStarters.ivDefensiveMidFielder.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_defensive_midfielder),
                null,
                DEFENSIVE_MID_FIELDER
            )
        }
        binding.clStarters.ivLeftMidFielder.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_left_midfielder),
                null,
                LEFT_MID_FIELDER
            )
        }
        binding.clStarters.ivRightMidFielder.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_right_midfielder),
                null,
                RIGHT_MID_FIELDER
            )
        }
        binding.clStarters.ivLeftStriker.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_left_striker),
                null,
                LEFT_STRIKER
            )
        }
        binding.clStarters.ivRightStriker.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_right_striker),
                null,
                RIGHT_STRIKER
            )
        }
        binding.clStarters.ivStriker.parent.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_striker),
                null,
                STRIKER
            )
        }

        // Bench
        binding.btnInsertBenchPlayer.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_bench_player),
                null,
                PlayerPositions.BENCH
            )
        }

        // Stats
        binding.btnInsertGoal.setOnClickListener {
            showDialog(
                insertGameViewModel.providePlayersList(),
                getString(R.string.select_scorer),
                null,
                null,
                MatchStats.SCORERS
            )
        }

        // Insert game
        binding.btnInsertGame.setOnClickListener {
            reDoInsertGame()
            insertGameViewModel.insertGame(
                id = id,
                journey = journey,
                homeGoals = binding.etLocalGoals.text.toString(),
                awayGoals = binding.etVisitorGoals.text.toString(),
                onSuccess = { onBackPressedDispatcher.onBackPressed() },
                onMissingField = { showErrors(it) }
            )
        }
    }

    private fun showItem(
        arrow: ImageView,
        childView: LinearLayout?,
        childConstraint: ConstraintLayout? = null,
        footballFieldBinding: ItemFootballFieldBinding? = null
    ) {
        deselectAll()
        arrow.rotation = if (arrow.rotation == 0f) 90f else 0f

        if (childView != null) {
            childView.visibility =
                if (childView.visibility == LinearLayout.VISIBLE) LinearLayout.GONE else LinearLayout.VISIBLE
            childView.setBackgroundColor(resources.getColor(R.color.primary, null))
        } else if (childConstraint != null) {
            childConstraint.visibility =
                if (childConstraint.visibility == ConstraintLayout.VISIBLE) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            childConstraint.setBackgroundColor(resources.getColor(R.color.primary, null))
        } else if (footballFieldBinding != null) {
            footballFieldBinding.root.visibility =
                if (footballFieldBinding.root.visibility == ConstraintLayout.VISIBLE) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            footballFieldBinding.root.setBackgroundColor(resources.getColor(R.color.primary, null))
        }
    }


    private fun setMatchType(btn: MaterialButton, local: MatchLocal? = null) {
        if (local != null) {
            // Local / Visitor team inserted
            deselectMatchLocal()
            setLocalVisitor(local)
        } else {
            deselectMatchType()
        }
        btn.setBackgroundColor(resources.getColor(R.color.green, null))
    }

    private fun setLocalVisitor(local: MatchLocal) {
        binding.tvSetLocal.visibility = GONE
        when (local) {
            HOME -> {
                binding.ivLocal.setImageResource(R.drawable.img_gaztelu_bira)
                binding.tvLocalTeam.text = resources.getString(R.string.gaztelu_bira)
                binding.ivVisitor.setImageResource(R.drawable.img_no_football_shield)
                binding.tvVisitorTeam.text = resources.getString(R.string.select_team)
                binding.ivVisitor.setOnClickListener {
                    showDialog(
                        dialogList = insertGameViewModel.provideTeamList(),
                        dialogTitle = getString(R.string.select_team),
                        team = AWAY,
                        playerPosition = null
                    )
                }
            }

            AWAY -> {
                binding.ivVisitor.setImageResource(R.drawable.img_gaztelu_bira)
                binding.tvVisitorTeam.text = resources.getString(R.string.gaztelu_bira)
                binding.ivLocal.setImageResource(R.drawable.img_no_football_shield)
                binding.tvLocalTeam.text = resources.getString(R.string.select_team)
                binding.ivLocal.setOnClickListener {
                    showDialog(
                        dialogList = insertGameViewModel.provideTeamList(),
                        dialogTitle = getString(R.string.select_team),
                        team = HOME,
                        playerPosition = null
                    )
                }
            }
        }
    }

    private fun showDialog(
        dialogList: List<Pair<String, String>?>,
        dialogTitle: String,
        team: MatchLocal?,
        playerPosition: PlayerPositions?,
        matchStat: MatchStats? = null
    ) {
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(
            R.layout.dialog_layout,
            builder.create().window?.decorView as? ViewGroup,
            false
        )

        with(builder) {
            setView(view)
            create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
                view.findViewById<TextView>(R.id.tvDialogTitle).text = dialogTitle
                setDialogGridLayout(
                    view = view.findViewById(R.id.glDialog),
                    dialogList = dialogList,
                    team = team,
                    playerPosition = playerPosition,
                    matchStat = matchStat,
                    onTeamSelected = { team, teamName, teamImg ->
                        setTeam(team, teamName, teamImg)
                        dismiss()
                    },
                    onPlayerSelected = { playerPosition, playerName, playerDorsal ->
                        setPlayer(playerPosition, playerName, playerDorsal)
                        dismiss()
                    },
                    onStatSelected = { matchStat, playerName ->
                        insertGameViewModel.setStat(matchStat, playerName)
                        dismiss()
                    }
                )

                if (dialogList.isEmpty()) {
                    Toast.makeText(
                        this@InsertGameActivity,
                        getString(R.string.an_error_has_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                }
            }
        }
    }

    private fun setDialogGridLayout(
        view: GridLayout,
        dialogList: List<Pair<String, String>?>,
        team: MatchLocal?,
        playerPosition: PlayerPositions?,
        matchStat: MatchStats?,
        onTeamSelected: (MatchLocal, String?, String?) -> Unit,
        onPlayerSelected: (PlayerPositions, String?, String?) -> Unit,
        onStatSelected: (MatchStats?, String?) -> Unit = { _, _ -> }
    ) {
        dialogList.forEach { dialogItem ->
            val item = layoutInflater.inflate(R.layout.item_dialog, view, false) as View
            item.findViewById<TextView>(R.id.tvDialog).text = dialogItem?.first
            if (team != null) {
                Glide.with(this).load(dialogItem?.second).into(item.findViewById(R.id.ivDialog))
                item.setOnClickListener {
                    onTeamSelected(
                        team,
                        dialogItem?.first,
                        dialogItem?.second
                    )
                }
            } else if (playerPosition != null) {
                Glide.with(this).load(insertGameViewModel.getPlayerImg(dialogItem?.first))
                    .into(item.findViewById(R.id.ivDialog))
                item.setOnClickListener {
                    onPlayerSelected(
                        playerPosition,
                        dialogItem?.first,
                        dialogItem?.second
                    )
                }
            } else if (matchStat != null){
                Glide.with(this).load(insertGameViewModel.getPlayerImg(dialogItem?.first))
                    .into(item.findViewById(R.id.ivDialog))
                item.setOnClickListener { onStatSelected(matchStat, dialogItem?.first) }
            }
            view.addView(item)
        }
    }

    private fun setTeam(team: MatchLocal, teamName: String?, teamImg: String?) {
        insertGameViewModel.setLocalVisitor(team, teamName)
        when (team) {
            HOME -> {
                Glide.with(this).load(teamImg).into(binding.ivLocal)
                binding.tvLocalTeam.text = teamName
            }

            AWAY -> {
                Glide.with(this).load(teamImg).into(binding.ivVisitor)
                binding.tvVisitorTeam.text = teamName
            }
        }
    }

    private fun setPlayer(
        playerPosition: PlayerPositions,
        playerName: String?,
        playerDorsal: String?
    ) {
        val showMaxBenchPlayersError = {
            Toast.makeText(
                this,
                getString(R.string.max_bench_players_error),
                Toast.LENGTH_SHORT
            ).show()
        }
        insertGameViewModel.setPlayerInMatchStats(
            playerPosition,
            playerName
        ) { showMaxBenchPlayersError() }
        when (playerPosition) {
            GOAL_KEEPER -> {
                binding.clStarters.ivGoalKeeper.tvPlayerName.text = playerName
                binding.clStarters.ivGoalKeeper.dorsalTextView.text = playerDorsal
            }

            LEFT_BACK -> {
                binding.clStarters.ivLeftBack.tvPlayerName.text = playerName
                binding.clStarters.ivLeftBack.dorsalTextView.text = playerDorsal
            }

            RIGHT_BACK -> {
                binding.clStarters.ivRightBack.tvPlayerName.text = playerName
                binding.clStarters.ivRightBack.dorsalTextView.text = playerDorsal
            }

            LEFT_CENTRE_BACK -> {
                binding.clStarters.ivLeftCentreBack.tvPlayerName.text = playerName
                binding.clStarters.ivLeftCentreBack.dorsalTextView.text = playerDorsal
            }

            RIGHT_CENTRE_BACK -> {
                binding.clStarters.ivRightCentreBack.tvPlayerName.text = playerName
                binding.clStarters.ivRightCentreBack.dorsalTextView.text = playerDorsal
            }

            DEFENSIVE_MID_FIELDER -> {
                binding.clStarters.ivDefensiveMidFielder.tvPlayerName.text = playerName
                binding.clStarters.ivDefensiveMidFielder.dorsalTextView.text = playerDorsal
            }

            LEFT_MID_FIELDER -> {
                binding.clStarters.ivLeftMidFielder.tvPlayerName.text = playerName
                binding.clStarters.ivLeftMidFielder.dorsalTextView.text = playerDorsal
            }

            RIGHT_MID_FIELDER -> {
                binding.clStarters.ivRightMidFielder.tvPlayerName.text = playerName
                binding.clStarters.ivRightMidFielder.dorsalTextView.text = playerDorsal
            }

            LEFT_STRIKER -> {
                binding.clStarters.ivLeftStriker.tvPlayerName.text = playerName
                binding.clStarters.ivLeftStriker.dorsalTextView.text = playerDorsal
            }

            RIGHT_STRIKER -> {
                binding.clStarters.ivRightStriker.tvPlayerName.text = playerName
                binding.clStarters.ivRightStriker.dorsalTextView.text = playerDorsal
            }

            STRIKER -> {
                binding.clStarters.ivStriker.tvPlayerName.text = playerName
                binding.clStarters.ivStriker.dorsalTextView.text = playerDorsal
            }

            PlayerPositions.BENCH -> {}
        }
    }

    private fun deselectMatchType() {
        binding.btnLeague.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
        binding.btnCup.setBackgroundColor(resources.getColor(R.color.grey_selected, null))
    }

    private fun deselectMatchLocal() {
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
        binding.clResult.visibility = GONE
        binding.clStarters.parent.visibility = GONE
        binding.llBench.visibility = GONE
        binding.llStats.visibility = GONE
    }

    private fun reDoInsertGame() {
        binding.tvMatchType.setTextColor(resources.getColor(R.color.black, null))
        binding.tvMatchLocal.setTextColor(resources.getColor(R.color.black, null))
        binding.tvResult.setTextColor(resources.getColor(R.color.black, null))
        binding.tvStarters.setTextColor(resources.getColor(R.color.black, null))
        binding.tvBench.setTextColor(resources.getColor(R.color.black, null))
        binding.tvStats.setTextColor(resources.getColor(R.color.black, null))
    }

    private fun showErrors(check: InsertGameChecks) {
        Toast.makeText(this, getString(R.string.missing_field_error), Toast.LENGTH_SHORT).show()
        when (check) {
            InsertGameChecks.MATCH_TYPE -> {
                binding.tvMatchType.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.MATCH_LOCAL -> {
                binding.tvMatchLocal.setTextColor(resources.getColor(R.color.main_red, null))
                binding.tvResult.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.RESULT -> {
                binding.tvResult.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.STARTERS -> {
                binding.tvStarters.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.BENCH -> {
                binding.tvBench.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.GOALS -> {
                binding.tvStats.setTextColor(resources.getColor(R.color.main_red, null))
            }

            InsertGameChecks.CLEAN_SHEET -> {
                binding.tvStats.setTextColor(resources.getColor(R.color.main_red, null))
            }
        }
    }
}