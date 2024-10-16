package com.sgalera.gaztelubira.ui.player_compare.detail

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
        init()
        checkReferences()
        initPlayerImages()
        initPlayersStats()
    }

    private fun init() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                comparePlayersViewModel.uiState.collect { uiState ->
                    when (uiState){
                        UIState.Error -> onErrorState()
                        UIState.Loading -> {}
                        UIState.Success -> onSuccessState()
                    }
                }
            }
        }
    }

    private fun onErrorState() {
        Toast.makeText(this, getString(R.string.an_error_has_occurred), Toast.LENGTH_SHORT).show()
        onBackPressedDispatcher.onBackPressed()
    }

    private fun onSuccessState() {
        binding.pbLoading.visibility = View.GONE
        binding.clParent.visibility = View.GONE
        initAnimations()
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



    // ANIMATIONS
    private fun initAnimations() {
        val playerOneAnimation = initPlayerOneAnimation()
        val playerTwoAnimation = initPlayerTwoAnimation()

        val animationSet = AnimatorSet()
        animationSet.playTogether(playerOneAnimation, playerTwoAnimation)
        animationSet.addListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                initExitScreenAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        animationSet.start()
    }

    private fun initExitScreenAnimation() {
        val alphaAnimation = ObjectAnimator.ofFloat(binding.clCompareAnimations, "alpha", 1f, 0f)
        alphaAnimation.duration = 1000
        alphaAnimation.startDelay = 1000
        alphaAnimation.start()
    }

    private fun initPlayerOneAnimation(): AnimatorSet {
        binding.cvPlayerOne.visibility = View.VISIBLE

        val translationX = ObjectAnimator.ofFloat(binding.cvPlayerOne, "translationX", -1000f, 0f)
        val translationY = ObjectAnimator.ofFloat(binding.cvPlayerOne, "translationY", 800f, 0f)
        translationX.duration = 1000
        translationY.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationX, translationY)
        return animatorSet
    }

    private fun initPlayerTwoAnimation(): AnimatorSet {
        binding.cvPlayerTwo.visibility = View.VISIBLE

        val translationX = ObjectAnimator.ofFloat(binding.cvPlayerTwo, "translationX", 1000f, 0f)
        val translationY = ObjectAnimator.ofFloat(binding.cvPlayerTwo, "translationY", -800f, 0f)
        translationX.duration = 1000
        translationY.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationX, translationY)
        return animatorSet
    }
}