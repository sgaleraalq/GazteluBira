package com.sgalera.gaztelubira.ui.team.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityPlayerInformationDetailBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerInformationDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerInformationDetailBinding

    private val playerInformationViewModel: PlayerInformationViewModel by viewModels()
    private val args: PlayerInformationDetailArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerInformationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initStats()
        initListeners()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initStats() {
        if (args.position != "TECHNICAL_STAFF") {
            playerInformationViewModel.getPlayerStats(args.name)
        } else {
            playerInformationViewModel.initManagers()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerInformationViewModel.player.collect {
                    initUI(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                playerInformationViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        PlayerInformationState.Loading -> {}
                        PlayerInformationState.Error -> onError()
                        PlayerInformationState.Success -> onSuccess()
                    }
                }
            }
        }
    }

    private fun onError() {
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        Toast.makeText(
            this@PlayerInformationDetail,
            getString(R.string.error_player_reference_not_found),
            Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess() {
        if (args.position == "TECHNICAL_STAFF") {
            binding.clPlayerStats.visibility = View.GONE
            binding.tvPlayerPosition.text = getString(R.string.manager)
            binding.ivPlayerDorsal.visibility = View.GONE
            binding.tvPlayerDorsal.visibility = View.GONE
        } else {
            binding.tvPlayerPosition.text = args.position
        }
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        binding.tvPlayerName.text = args.name
        binding.tvPlayerDorsal.text = args.dorsal.toString()
        binding.clPlayerStats.alpha = 0f
        binding.clPlayerStats.animate().alpha(1f).setDuration(2000).start()
        Glide.with(this).load(args.image).into(binding.ivPlayerImage)
    }

    private fun initUI(player: PlayerStatsModel?) {
        binding.tvGoals.text = player?.goals.toString()
        binding.tvAssists.text = player?.assists.toString()
        binding.tvGames.text = player?.gamesPlayed.toString()
        binding.tvPenalties.text = player?.penalties.toString()
        binding.tvCleanSheet.text = player?.cleanSheet.toString()
    }
}