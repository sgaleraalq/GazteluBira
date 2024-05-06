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
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerInformationDetail : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerInformationDetailBinding
    private val playerInformationViewModel: PlayerInformationViewModel by viewModels()
    private val args: PlayerInformationDetailArgs by navArgs()

    private lateinit var playerName: String
    private lateinit var playerImage: String
    private var playerReference: String? = null
    private var playerDorsal = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerInformationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        initUI()
        initListeners()
    }

    private fun initComponents() {
        playerName = args.name
        playerImage = args.image
        playerReference = args.reference
        playerDorsal = args.dorsal
    }

    private fun initUI() {
        if (playerReference != null) {
            initPlayerComponents()
        } else {
            initManagerComponents()
        }
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initPlayerComponents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val reference = playerInformationViewModel.getReference(playerReference!!)
                if (reference == null) {
                    errorState(getString(R.string.error_player_reference_not_found))
                    return@repeatOnLifecycle
                }
                playerInformationViewModel.getPlayerStatsByReference(reference)
                playerInformationViewModel.statePlayerInformation.collect { state ->
                    when (state) {
                        PlayerInformationDetailState.Loading -> loadingState()
                        is PlayerInformationDetailState.Error -> errorState(state.error)
                        is PlayerInformationDetailState.Success -> successState(state.playerStats)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbLoadingPlayerInformation.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        Toast.makeText(this@PlayerInformationDetail, error, Toast.LENGTH_SHORT).show()
    }

    private fun successState(playerStats: PlayerStats) {
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        binding.parentView.visibility = View.VISIBLE
        binding.tvPlayerName.text = playerName
        binding.tvPlayerPosition.text = playerStats.position
        Glide.with(this)
            .load(playerImage)
            .into(binding.ivPlayerImage)

        initPlayerStats(playerStats)
    }

    private fun initPlayerStats(playerStats: PlayerStats) {
        binding.tvPlayerDorsal.text = playerDorsal.toString()
        binding.tvGoals.text = playerStats.goals.toString()
        binding.tvAssists.text = playerStats.assists.toString()
        binding.tvGames.text = playerStats.gamesPlayed.toString()
        binding.tvPenalties.text = playerStats.penalties.toString()
        binding.tvCleanSheet.text = playerStats.cleanSheet.toString()

        binding.clPlayerStats.alpha = 0f
        binding.clPlayerStats.animate()
            .alpha(1f)
            .setDuration(2000)
            .start()
    }

    private fun initManagerComponents() {
        binding.pbLoadingPlayerInformation.visibility = View.GONE
        binding.clPlayerStats.visibility = View.GONE
        binding.parentView.visibility = View.VISIBLE
        binding.tvPlayerName.text = playerName
        binding.tvPlayerPosition.text = getString(R.string.manager)
        Glide.with(this)
            .load(playerImage)
            .into(binding.ivPlayerImage)
    }
}