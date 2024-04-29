package com.sgalera.gaztelubira.ui.matches.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityDetailMatchBinding
import com.sgalera.gaztelubira.domain.model.matches.Match
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

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
        initUIState()
        initListeners()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchViewModel.getMatch(args.id)
                matchViewModel.state.collect {
                    when (it) {
                        DetailMatchState.Loading -> loadingState()
                        is DetailMatchState.Error -> errorState(it.error)
                        is DetailMatchState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.progressBar.visibility = View.INVISIBLE
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }

    private fun successState(state: DetailMatchState.Success) {
        binding.progressBar.visibility = View.INVISIBLE
        if (!matchViewModel.hasDataLoaded) {
            initComponents(state.match)
        }
    }

    private fun initComponents(match: Match) {
        binding.tvSlash.visibility = View.VISIBLE
        binding.clStarters.visibility = View.VISIBLE


        // TODO change to call to the database
        // Local
        binding.ivLocalTeam.setImageResource(match.local.img)
        binding.tvLocalTeam.text = this.getString(match.local.name)
        binding.tvLocalGoals.text = match.localGoals.toString()

        // Visitor
        binding.ivAwayTeam.setImageResource(match.visitor.img)
        binding.tvAwayTeam.text = this.getString(match.visitor.name)
        binding.tvAwayGoals.text = match.visitorGoals.toString()

        initLinearLayouts(match.scorers, match.assistants)
        initBenchPlayers(match.bench)
        initStarters(match.starters)
    }

    @SuppressLint("InflateParams")
    private fun initLinearLayouts(goalPlayers: List<String>, assistsPlayers: List<String>) {
        binding.llGoals.removeAllViews()
        binding.llAssists.removeAllViews()
        binding.llBench.removeAllViews()
        binding.llBench2.removeAllViews()

        for (player in goalPlayers) {
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_detail_match, null) as View
            itemLayout.findViewById<TextView>(R.id.tvPlayer).text =
                player.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            itemLayout.findViewById<ImageView>(R.id.ivGoal)
                .setImageResource(R.drawable.ic_football_ball)
            binding.llGoals.addView(itemLayout)
        }
        for (player in assistsPlayers) {
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_detail_match, null) as View
            itemLayout.findViewById<TextView>(R.id.tvPlayer).text =
                player.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            itemLayout.findViewById<ImageView>(R.id.ivGoal)
                .setImageResource(R.drawable.ic_football_shoe)
            binding.llAssists.addView(itemLayout)
        }
    }
    @SuppressLint("InflateParams")
    private fun initBenchPlayers(bench: List<PlayerInformation?>) {
        val benchList = bench.sortedBy { it!!.dorsal }
        for ((index, player) in benchList.withIndex()) {
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_bench, null) as View
            itemLayout.findViewById<TextView>(R.id.tvBenchPlayer).text = player!!.name
            itemLayout.findViewById<TextView>(R.id.tvDorsal).text = player.dorsal.toString()

            // Decidir en qué LinearLayout colocar el elemento según el índice
            if (bench.size % 2 == 0) {
                if (index < bench.size / 2) {
                    binding.llBench.addView(itemLayout)
                } else {
                    binding.llBench2.addView(itemLayout)
                }
            } else {
                if (index < bench.size / 2 + 1) {
                    binding.llBench.addView(itemLayout)
                } else {
                    binding.llBench2.addView(itemLayout)
                }
            }
        }
    }
    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initStarters(starters: Map<String, PlayerInformation?>) {
        includeGoalKeeper(starters["goal_keeper"])
        includeBacks(starters["left_back"], starters["right_back"])
        includeCentreBacks(starters["left_centre_back"], starters["right_centre_back"])
        includeMidFielders(starters["defensive_mid_fielder"], starters["left_mid_fielder"], starters["right_mid_fielder"])
        includeStrikers(starters["left_striker"], starters["right_striker"], starters["striker"])
    }

    private fun includeGoalKeeper(playerInfo: PlayerInformation?) {
        binding.ivGoalKeeper.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
        binding.tvGoalKeeper.text = playerInfo?.name
    }

    private fun includeBacks(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?) {
        binding.ivLeftBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
        binding.tvLeftBack.text = playerInfo?.name

        binding.ivRightBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
        binding.tvRightBack.text = playerInfo1?.name
    }

    private fun includeCentreBacks(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?) {
        binding.ivLeftCentreBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
        binding.tvLeftCentreBack.text = playerInfo?.name

        binding.ivRightCentreBack.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
        binding.tvRightCentreBack.text = playerInfo1?.name
    }

    private fun includeMidFielders(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?, playerInfo2: PlayerInformation?) {
        binding.ivDefensiveMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
        binding.tvDefensiveMidFielder.text = playerInfo?.name

        binding.ivLeftMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
        binding.tvLeftMidFielder.text = playerInfo1?.name

        binding.ivRightMidFielder.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo2?.dorsal.toString()
        binding.tvRightMidFielder.text = playerInfo2?.name
    }

    private fun includeStrikers(playerInfo: PlayerInformation?, playerInfo1: PlayerInformation?, playerInfo2: PlayerInformation?) {
        binding.ivLeftStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo?.dorsal.toString()
        binding.tvLeftStriker.text = playerInfo?.name

        binding.ivRightStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo1?.dorsal.toString()
        binding.tvRightStriker.text = playerInfo1?.name

        binding.ivStriker.root.findViewById<TextView>(R.id.dorsalTextView).text = playerInfo2?.dorsal.toString()
        binding.tvStriker.text = playerInfo2?.name
    }
}