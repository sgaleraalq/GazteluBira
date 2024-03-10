package com.sgalera.gaztelubira.ui.matches.detail

import android.os.Bundle
import android.os.PersistableBundle
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
import com.sgalera.gaztelubira.domain.model.Team
import com.sgalera.gaztelubira.domain.model.matches.Match
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@AndroidEntryPoint
class DetailMatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMatchBinding
    private val matchViewModel: DetailMatchViewModel by viewModels()
    private val args: DetailMatchActivityArgs by navArgs()

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putBoolean("hasDataLoaded", matchViewModel.hasDataLoaded)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        matchViewModel.hasDataLoaded = savedInstanceState.getBoolean("hasDataLoaded", false)
    }

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

        initLinearLayouts(match.scorers, match.assistants, match.bench)
    }

    private fun initLinearLayouts(goalPlayers: List<String>, assistsPlayers: List<String>, bench: List<String>) {
        for (player in goalPlayers){
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_detail_match, null) as View
            itemLayout.findViewById<TextView>(R.id.tvPlayer).text = player.capitalize()
            itemLayout.findViewById<ImageView>(R.id.ivGoal).setImageResource(R.drawable.ic_football_ball)
            binding.llGoals.addView(itemLayout)
        }
        for (player in assistsPlayers){
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_detail_match, null) as View
            itemLayout.findViewById<TextView>(R.id.tvPlayer).text = player.capitalize()
            itemLayout.findViewById<ImageView>(R.id.ivGoal).setImageResource(R.drawable.ic_football_shoe)
            binding.llAssists.addView(itemLayout)
        }

        for ((index, player) in bench.withIndex()) {
            val inflater = LayoutInflater.from(this)
            val itemLayout = inflater.inflate(R.layout.item_bench, null) as View
            itemLayout.findViewById<TextView>(R.id.tvBenchPlayer).text = player.capitalize()
            itemLayout.findViewById<TextView>(R.id.tvDorsal).text = "10" // TODO

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
            onBackPressed()
        }
    }

}