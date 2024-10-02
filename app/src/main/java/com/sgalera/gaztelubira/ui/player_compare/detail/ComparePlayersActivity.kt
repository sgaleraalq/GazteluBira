package com.sgalera.gaztelubira.ui.player_compare.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.core.Constants.PLAYER_NO_IMAGE
import com.sgalera.gaztelubira.databinding.ActivityComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComparePlayersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComparePlayersBinding
    private val comparePlayersViewModel by viewModels<ComparePlayersViewModel>()

    private lateinit var playerOne: Pair<String, String>
    private lateinit var playerTwo: Pair<String, String>

    companion object {
        private const val EXTRA_PLAYER_ONE = "player_one_ref"
        private const val EXTRA_PLAYER_TWO = "player_two_ref"
        private const val EXTRA_PLAYER_ONE_IMG = "player_one_img"
        private const val EXTRA_PLAYER_TWO_IMG = "player_two_img"

        fun create(
            context: Context,
            playerOneRef: String,
            playerTwoRef: String,
            playerOneImg: String,
            playerTwoImg: String
        ) = Intent(context, ComparePlayersActivity::class.java).apply {
            putExtra(EXTRA_PLAYER_ONE, playerOneRef)
            putExtra(EXTRA_PLAYER_TWO, playerTwoRef)
            putExtra(EXTRA_PLAYER_ONE_IMG, playerOneImg)
            putExtra(EXTRA_PLAYER_TWO_IMG, playerTwoImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComparePlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerOne = Pair(
            intent.getStringExtra(EXTRA_PLAYER_ONE) ?: "",
            intent.getStringExtra(EXTRA_PLAYER_ONE_IMG) ?: PLAYER_NO_IMAGE
        )
        playerTwo = Pair(
            intent.getStringExtra(EXTRA_PLAYER_TWO) ?: "",
            intent.getStringExtra(EXTRA_PLAYER_TWO_IMG) ?: PLAYER_NO_IMAGE
        )
        initUI()
        initListeners()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initUI() {
        checkReferences()
        initPlayerImages()
        initPlayersStats()
    }

    private fun checkReferences() {
        if (playerOne.first.isEmpty() || playerTwo.first.isEmpty()) {
            Toast.makeText(this, getString(R.string.an_error_has_occurred), Toast.LENGTH_SHORT).show()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initPlayerImages() {
        Glide.with(this).load(playerOne.second).into(binding.ivPlayerOne)
        Glide.with(this).load(playerTwo.second).into(binding.ivPlayerTwo)
        binding.tvPlayerOneName.text = playerOne.first
        binding.tvPlayerTwoName.text = playerTwo.first
    }


    private fun initPlayersStats() {
        comparePlayersViewModel.getPlayerStats(playerOne.first)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comparePlayersViewModel.playerOneStats.collect { playerOne ->
                    initPlayer(playerOne, binding.ivPlayerOne)
                }
            }
        }
    }

    private fun initPlayer(player: PlayerStatsModel?, itemView: ImageView) {
        // TODO
    }
}