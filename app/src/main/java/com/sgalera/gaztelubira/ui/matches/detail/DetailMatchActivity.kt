package com.sgalera.gaztelubira.ui.matches.detail

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
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
import com.sgalera.gaztelubira.domain.model.MatchStatsModel
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.TeamModel
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
        matchViewModel.getMatch(args.id)
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

    private fun initScorers(scorers: List<String>) {
        scorers.forEach { inflateView(it) }
    }

    private fun initAssistants(assistants: List<String>) {
        assistants.forEach { inflateView(it, false) }
    }

    private fun initStarters(starters: Map<String, PlayerModel?>) {

    }

    private fun initBench(bench: List<PlayerModel?>) {
        bench.forEachIndexed { index, benchPlayer -> inflateBenchView(benchPlayer, index) }
    }

    private fun inflateView(scorer: String, isGoal: Boolean = true) {
        val itemLayout = layoutInflater.inflate(R.layout.item_detail_match, null) as View
        itemLayout.findViewById<TextView>(R.id.tvPlayer).text = scorer
        if (isGoal) itemLayout.findViewById<ImageView>(R.id.ivGoal).setImageResource(R.drawable.ic_football_ball)
        if (isGoal) binding.llGoals.addView(itemLayout) else binding.llAssists.addView(itemLayout)
    }

    private fun inflateBenchView(benchPlayer: PlayerModel?, index: Int) {
        val itemLayout = layoutInflater.inflate(R.layout.item_bench, null) as View
        itemLayout.findViewById<TextView>(R.id.tvBenchPlayer).text = benchPlayer?.name
        itemLayout.findViewById<TextView>(R.id.tvDorsal).text = benchPlayer?.dorsal.toString()

        val params = GridLayout.LayoutParams().apply {
            columnSpec = GridLayout.spec(index % 2)
            rowSpec = GridLayout.spec(index / 2)
        }

        binding.llBench.addView(itemLayout, params)
    }
}

//    private fun initUIState() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                matchViewModel.getMatch(args.id)
//                matchViewModel.state.collect {
//                    when (it) {
//                        DetailMatchState.Loading -> loadingState()
//                        is DetailMatchState.Error -> errorState(it.error)
//                        is DetailMatchState.Success -> successState(it)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun loadingState() {
//        binding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun errorState(error: String) {
//        binding.progressBar.visibility = View.INVISIBLE
//        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
//    }
//
//    private fun successState(state: DetailMatchState.Success) {
//        binding.progressBar.visibility = View.INVISIBLE
//        if (!matchViewModel.hasDataLoaded) {
//            initComponents(state.match)
//        }
//    }
//    private fun initListeners() {
//        binding.ivBack.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//    }
//
//    private fun initStarters(starters: Map<String, PlayerInformation?>) {
//        includeGoalKeeper(starters["goal_keeper"])
//        includeBacks(starters["left_back"], starters["right_back"])
//        includeCentreBacks(starters["left_centre_back"], starters["right_centre_back"])
//        includeMidFielders(starters["defensive_mid_fielder"], starters["left_mid_fielder"], starters["right_mid_fielder"])
//        includeStrikers(starters["left_striker"], starters["right_striker"], starters["striker"])
//    }
//
//    private fun includeGoalKeeper(playerInfo: PlayerInformation?) {
//        binding.ivGoalKeeper.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
//        binding.tvGoalKeeper.text = playerInfo?.name
//    }
//
//    private fun includeBacks(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?) {
//        binding.ivLeftBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
//        binding.tvLeftBack.text = playerInfo?.name
//
//        binding.ivRightBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
//        binding.tvRightBack.text = playerInfo1?.name
//    }
//
//    private fun includeCentreBacks(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?) {
//        binding.ivLeftCentreBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
//        binding.tvLeftCentreBack.text = playerInfo?.name
//
//        binding.ivRightCentreBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
//        binding.tvRightCentreBack.text = playerInfo1?.name
//    }
//
//    private fun includeMidFielders(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?, playerInfo2: PlayerInformation?) {
//        binding.ivDefensiveMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
//        binding.tvDefensiveMidFielder.text = playerInfo?.name
//
//        binding.ivLeftMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
//        binding.tvLeftMidFielder.text = playerInfo1?.name
//
//        binding.ivRightMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo2?.dorsal.toString()
//        binding.tvRightMidFielder.text = playerInfo2?.name
//    }
//
//    private fun includeStrikers(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?, playerInfo2: PlayerInformation?) {
//        binding.ivLeftStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
//        binding.tvLeftStriker.text = playerInfo?.name
//
//        binding.ivRightStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
//        binding.tvRightStriker.text = playerInfo1?.name
//
//        binding.ivStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo2?.dorsal.toString()
//        binding.tvStriker.text = playerInfo2?.name
//    }
//}