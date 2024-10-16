package com.sgalera.gaztelubira.ui.player_compare.detail

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.core.Constants.PLAYER_NO_IMAGE
import com.sgalera.gaztelubira.databinding.ActivityComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.skydoves.progressview.ProgressView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComparePlayersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityComparePlayersBinding
    private val comparePlayersViewModel by viewModels<ComparePlayersViewModel>()

    private lateinit var playerOne: Pair<String, String>
    private lateinit var playerTwo: Pair<String, String>

    private lateinit var playerOneStats: PlayerStatsModel
    private lateinit var playerTwoStats: PlayerStatsModel

    private var maxValue = 30

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
    }


    private fun init() {
        comparePlayersViewModel.getPlayerStats(playerOne.first, playerTwo.first)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comparePlayersViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        UIState.Error -> onErrorState()
                        UIState.Loading -> {}
                        UIState.Success -> onSuccessState()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comparePlayersViewModel.playerOneStats.collect { playerOne ->
                    if (playerOne != null) {
                        initPlayer(
                            playerOne,
                            binding.ivPlayerOneStats,
                            binding.tvPlayerOneNameStats,
                            binding.tvPlayerOnePercentage,
                            binding.tvPlayerOneGoals,
                            binding.tvPlayerOneAssists,
                            binding.tvPlayerOnePenalties,
                            binding.tvPlayerOneCleanSheet,
                            binding.tvPlayerOneGamesPlayed,
                            binding.pvGoalsPlayerOne,
                            binding.pvAssistsPlayerOne,
                            binding.pvPenaltiesPlayerOne,
                            binding.pvCleanSheetPlayerOne,
                            binding.pvGamesPlayedPlayerOne
                        )
                        playerOneStats = playerOne
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comparePlayersViewModel.playerTwoStats.collect { playerTwo ->
                    if (playerTwo != null) {
                        initPlayer(
                            playerTwo,
                            binding.ivPlayerTwoStats,
                            binding.tvPlayerTwoNameStats,
                            binding.tvPlayerTwoPercentage,
                            binding.tvPlayerTwoGoals,
                            binding.tvPlayerTwoAssists,
                            binding.tvPlayerTwoPenalties,
                            binding.tvPlayerTwoCleanSheet,
                            binding.tvPlayerTwoGamesPlayed,
                            binding.pvGoalsPlayerTwo,
                            binding.pvAssistsPlayerTwo,
                            binding.pvPenaltiesPlayerTwo,
                            binding.pvCleanSheetPlayerTwo,
                            binding.pvGamesPlayedPlayerTwo,
                        )
                        playerTwoStats = playerTwo
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comparePlayersViewModel.bothPlayersStats.collect { bothPlayers ->
                    if (bothPlayers != null) {
                        setupRadarChart(binding.rcStats, bothPlayers.first, bothPlayers.second)
                    }
                }
            }
        }
    }

    private fun initPlayer(
        player: PlayerStatsModel?,
        playerImage: ImageView,
        playerText: TextView,
        playerPercentage: TextView,
        playerGoals: TextView,
        playerAssists: TextView,
        playerPenalties: TextView,
        playerCleanSheet: TextView,
        playerGamesPlayed: TextView,
        goalProgress: ProgressView,
        assistProgress: ProgressView,
        penaltyProgress: ProgressView,
        cleanSheetProgress: ProgressView,
        gamesPlayedProgress: ProgressView,
    ) {
        maxValue = comparePlayersViewModel.provideMaxStatValue()

        Glide.with(this).load(player?.information?.img).into(playerImage)
        playerText.text = player?.information?.name
        playerPercentage.text = player?.percentage
        playerGoals.text = player?.goals.toString()
        playerAssists.text = player?.assists.toString()
        playerPenalties.text = player?.penalties.toString()
        playerCleanSheet.text = player?.cleanSheet.toString()
        playerGamesPlayed.text = player?.gamesPlayed.toString()

        val stats = listOf(
            player?.goals?.toFloat() ?: 0f,
            player?.assists?.toFloat() ?: 0f,
            player?.penalties?.toFloat() ?: 0f,
            player?.cleanSheet?.toFloat() ?: 0f,
            player?.gamesPlayed?.toFloat() ?: 0f
        )
        val progressViews = listOf(
            goalProgress,
            assistProgress,
            penaltyProgress,
            cleanSheetProgress,
            gamesPlayedProgress
        )

        stats.zip(progressViews) { stat, progressView ->
            progressView.progress = stat
            progressView.max = maxValue.toFloat()
        }
    }


    private fun setupRadarChart(
        radarChart: RadarChart,
        firstPlayer: PlayerStatsModel?,
        secondPlayer: PlayerStatsModel?
    ) {
        val context = this

        radarChart.clear()
        val labels = listOf("Goals", "Assists", "Penalties", "Clean Sheets", "Games Played")

        val firstPlayerStats = listOf(
            firstPlayer?.goals?.toFloat() ?: 0f,
            firstPlayer?.assists?.toFloat() ?: 0f,
            firstPlayer?.penalties?.toFloat() ?: 0f,
            firstPlayer?.cleanSheet?.toFloat() ?: 0f,
            firstPlayer?.gamesPlayed?.toFloat() ?: 0f
        )

        val secondPlayerStats = listOf(
            secondPlayer?.goals?.toFloat() ?: 0f,
            secondPlayer?.assists?.toFloat() ?: 0f,
            secondPlayer?.penalties?.toFloat() ?: 0f,
            secondPlayer?.cleanSheet?.toFloat() ?: 0f,
            secondPlayer?.gamesPlayed?.toFloat() ?: 0f
        )

        val entryListPlayerOne = ArrayList<RadarEntry>()
        val entryListPlayerTwo = ArrayList<RadarEntry>()

        for (i in labels.indices) {
            entryListPlayerOne.add(RadarEntry(firstPlayerStats[i]))
            entryListPlayerTwo.add(RadarEntry(secondPlayerStats[i]))
        }

        val dataSetPlayerOne = RadarDataSet(entryListPlayerOne, playerOneStats.information?.name).apply {
            color = ContextCompat.getColor(context, R.color.red_radar_chart)
            fillColor = ContextCompat.getColor(context, R.color.red_radar_chart)
            lineWidth = 1f
            fillAlpha = 180
            setDrawFilled(true)
            setDrawValues(false)
        }

        val dataSetPlayerTwo = RadarDataSet(entryListPlayerTwo, playerTwoStats.information?.name).apply {
            color = ContextCompat.getColor(context, R.color.blue_radar_chart)
            fillColor = ContextCompat.getColor(context, R.color.blue_radar_chart)
            lineWidth = 1f
            fillAlpha = 180
            setDrawFilled(true)
            setDrawValues(false)
        }

        val yAxis = radarChart.yAxis
        yAxis.setDrawLabels(false)
        yAxis.setLabelCount(5, true)

        val xAxis = radarChart.xAxis
        xAxis.textColor = Color.WHITE
        xAxis.yOffset = 5f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.setDrawLabels(true)
        xAxis.setLabelCount(labels.size, true)

        radarChart.legend.textColor = Color.WHITE
        radarChart.webLineWidth = 1f
        radarChart.webLineWidthInner = 0.5f
        radarChart.webAlpha = 100
        radarChart.description = null
        radarChart.legend.horizontalAlignment = CENTER

        radarChart.data = RadarData(dataSetPlayerOne, dataSetPlayerTwo)
        radarChart.invalidate()
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
            Toast.makeText(this, getString(R.string.an_error_has_occurred), Toast.LENGTH_SHORT)
                .show()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initPlayerImages() {
        Glide.with(this).load(playerOne.second).into(binding.ivPlayerOne)
        Glide.with(this).load(playerTwo.second).into(binding.ivPlayerTwo)
        binding.tvPlayerOneName.text = playerOne.first
        binding.tvPlayerTwoName.text = playerTwo.first
    }


    // ANIMATIONS
    private fun initAnimations() {
        val playerOneAnimation = initPlayerOneAnimation()
        val playerTwoAnimation = initPlayerTwoAnimation()

        val animationSet = AnimatorSet()
        animationSet.playTogether(playerOneAnimation, playerTwoAnimation)
        animationSet.addListener(object : Animator.AnimatorListener {
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
        alphaAnimation.startDelay = 1500
        alphaAnimation.start()
    }

    private fun initPlayerOneAnimation(): AnimatorSet {
        binding.cvPlayerOne.visibility = View.VISIBLE

        val translationX = ObjectAnimator.ofFloat(binding.cvPlayerOne, "translationX", -1000f, 0f)
        val translationY = ObjectAnimator.ofFloat(binding.cvPlayerOne, "translationY", 800f, 0f)
        translationX.duration = 750
        translationY.duration = 750
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationX, translationY)
        return animatorSet
    }

    private fun initPlayerTwoAnimation(): AnimatorSet {
        binding.cvPlayerTwo.visibility = View.VISIBLE

        val translationX = ObjectAnimator.ofFloat(binding.cvPlayerTwo, "translationX", 1000f, 0f)
        val translationY = ObjectAnimator.ofFloat(binding.cvPlayerTwo, "translationY", -800f, 0f)
        translationX.duration = 750
        translationY.duration = 750
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translationX, translationY)
        return animatorSet
    }
}
