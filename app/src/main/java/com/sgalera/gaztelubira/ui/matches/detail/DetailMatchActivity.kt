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
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
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
        setContentView(R.layout.activity_detail_match)
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
    private fun initBenchPlayers(bench: List<PlayerInfo>) {
        val benchList = bench.sortedBy { it.dorsal }
        for ((index, player) in benchList.withIndex()) {
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_bench, null) as View
            itemLayout.findViewById<TextView>(R.id.tvBenchPlayer).text = player.name
            itemLayout.findViewById<TextView>(R.id.tvDorsal).text = player.dorsal.toString()

            // Decidir en qué LinearLayout colocar el elemento según el índice
            if (index-1 < bench.size / 2) {
                binding.llBench.addView(itemLayout)
            } else {
                binding.llBench2.addView(itemLayout)
            }
        }
    }
    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initStarters(starters: Map<String, String>) {
        println(1)
    }

}