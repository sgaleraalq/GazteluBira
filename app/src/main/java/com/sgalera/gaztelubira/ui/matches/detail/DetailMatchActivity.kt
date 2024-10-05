package com.sgalera.gaztelubira.ui.matches.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.core.Constants.TEAM_NO_IMAGE
import com.sgalera.gaztelubira.databinding.ActivityDetailMatchBinding
import com.sgalera.gaztelubira.databinding.StartersDorsalImageViewBinding
import com.sgalera.gaztelubira.domain.model.matches.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.domain.model.teams.TeamModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMatchBinding
    private val matchViewModel: DetailMatchViewModel by viewModels()
    private val args: DetailMatchActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initMatchStats()
        initListeners()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initMatchStats(){
        matchViewModel.init(args.id)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchViewModel.matchStats.collect { matchStats ->
                    if (matchStats != null) {
                        initMatchComponents(matchStats)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchViewModel.isLoading.collect { isLoading ->
                    if (isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }


    private fun initMatchComponents(match: MatchStatsModel) {
        initMatchTeams(match.homeTeam, match.homeGoals, match.awayTeam, match.awayGoals)
        initScorers(match.scorers)
        initAssistants(match.assistants)
        initStarters(match.starters)
        initBench(match.bench)
    }

    private fun initMatchTeams(homeTeam: TeamModel?, homeGoals: Int, awayTeam: TeamModel?, awayGoals: Int) {
        Glide.with(this).load(homeTeam?.teamImg ?: TEAM_NO_IMAGE).into(binding.ivLocalTeam)
        Glide.with(this).load(awayTeam?.teamImg ?: TEAM_NO_IMAGE).into(binding.ivAwayTeam)
        binding.tvLocalTeam.text = homeTeam?.teamName ?: getString(R.string.could_not_retrieve)
        binding.tvAwayTeam.text = awayTeam?.teamName ?: getString(R.string.could_not_retrieve)
        binding.tvLocalGoals.text = homeGoals.toString()
        binding.tvAwayGoals.text = awayGoals.toString()
    }

    private fun initScorers(scorers: List<PlayerModel?>) {
        scorers.forEach { inflateView(it, binding.llGoals) }
    }

    private fun initAssistants(assistants: List<PlayerModel?>) {
        assistants.forEach { inflateView(it, binding.llAssists,false) }
    }

    private fun initStarters(starters: Map<String, PlayerModel?>) {
        starters.forEach { (position, player) ->
            when (position) {
                "goal_keeper" -> inflateStarterView(player, binding.clStarters.ivGoalKeeper)
                "left_back" -> inflateStarterView(player, binding.clStarters.ivLeftBack)
                "right_back" -> inflateStarterView(player, binding.clStarters.ivRightBack)
                "left_centre_back" -> inflateStarterView(player, binding.clStarters.ivLeftCentreBack)
                "right_centre_back" -> inflateStarterView(player, binding.clStarters.ivRightCentreBack)
                "defensive_mid_fielder" -> inflateStarterView(player, binding.clStarters.ivDefensiveMidFielder)
                "left_mid_fielder" -> inflateStarterView(player, binding.clStarters.ivLeftMidFielder)
                "right_mid_fielder" -> inflateStarterView(player, binding.clStarters.ivRightMidFielder)
                "left_striker" -> inflateStarterView(player, binding.clStarters.ivLeftStriker)
                "right_striker" -> inflateStarterView(player, binding.clStarters.ivRightStriker)
                "striker" -> inflateStarterView(player, binding.clStarters.ivStriker)
            }
        }
    }

    private fun initBench(bench: List<PlayerModel?>) {
        bench.forEachIndexed { index, benchPlayer -> inflateBenchView(benchPlayer, index, binding.llBench1, binding.llBench2) }
    }

    private fun inflateView(player: PlayerModel?, parent: ViewGroup, isGoal: Boolean = true) {
        val itemLayout = layoutInflater.inflate(R.layout.item_detail_match, parent, false) as View
        itemLayout.findViewById<TextView>(R.id.tvPlayer).text = player?.name ?: getString(R.string.could_not_retrieve)
        if (isGoal) itemLayout.findViewById<ImageView>(R.id.ivGoal).setImageResource(R.drawable.ic_football_ball)
        parent.addView(itemLayout)
    }

    private fun inflateBenchView(benchPlayer: PlayerModel?, index: Int, parent: ViewGroup, parent2: ViewGroup) {
        val itemLayout = layoutInflater.inflate(R.layout.item_bench, parent, false) as View
        itemLayout.findViewById<TextView>(R.id.tvBenchPlayer).text = benchPlayer?.name
        itemLayout.findViewById<TextView>(R.id.tvDorsal).text = benchPlayer?.dorsal.toString()

        if (index % 2 == 0) { parent.addView(itemLayout) } else { parent2.addView(itemLayout) }
    }

    private fun inflateStarterView(player: PlayerModel?, ivPlayer: StartersDorsalImageViewBinding) {
        ivPlayer.dorsalTextView.text = player?.dorsal.toString()
        ivPlayer.tvPlayerName.text = player?.name ?: getString(R.string.could_not_retrieve)
    }
}
