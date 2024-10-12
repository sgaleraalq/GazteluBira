package com.sgalera.gaztelubira.ui.stats

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStatsBinding
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.ui.stats.StatType.ASSISTS
import com.sgalera.gaztelubira.ui.stats.StatType.CLEAN_SHEET
import com.sgalera.gaztelubira.ui.stats.StatType.GAMES_PLAYED
import com.sgalera.gaztelubira.ui.stats.StatType.GOALS
import com.sgalera.gaztelubira.ui.stats.StatType.PENALTIES
import com.sgalera.gaztelubira.ui.stats.StatType.PERCENTAGE
import com.sgalera.gaztelubira.ui.stats.adapter.PlayerStatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val statsViewModel by viewModels<StatsViewModel>()
    private lateinit var playersStatsAdapter: PlayerStatsAdapter

    private val minHeight = 1000
    private val maxHeight = 1600

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initMinHeight()
    }

    private fun initMinHeight() {
        val layoutParams = binding.clStats.layoutParams
        layoutParams.height = minHeight
        binding.clStats.layoutParams = layoutParams
    }


    private fun initUI() {
        initTextViewColors()
        initRanking()
        initListeners()
        initRecyclerViewScroll()
    }

    private fun initTextViewColors() {
        initTextViewGradient(binding.tvChampionName)
        initTextViewGradient(binding.tvSecondName)
        initTextViewGradient(binding.tvThirdName)

//        // Reflection effect
//        startReflectionAnimation(binding.tvChampionName)
//        startReflectionAnimation(binding.tvSecondName)
//        startReflectionAnimation(binding.tvThirdName)
    }

    private fun initRanking() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        UIState.Loading -> binding.pbStats.visibility = View.VISIBLE
                        UIState.Success -> binding.pbStats.visibility = View.GONE
                        is UIState.Error -> {
                            Toast.makeText(
                                context,
                                getString(R.string.main_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.pbStats.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.playersStats.collect { playersStats ->
                    initRecyclerView(playersStats)
                    initChampions(
                        playersStats.firstOrNull(),
                        playersStats.getOrNull(1),
                        playersStats.getOrNull(2),
                        statsViewModel.statSelected.value
                    )
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.headerOpacity.collect {
                    binding.clChampionsView.alpha = it
                }
            }
        }
    }

    private fun initListeners() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.responsiveUI.collect { isResponsive ->
                    if (isResponsive) {
                        binding.btnStats.setCardBackgroundColor(
                            getColor(
                                requireContext(),
                                R.color.cvStats
                            )
                        )
                        binding.cvStatsBackground.setCardBackgroundColor(
                            getColor(
                                requireContext(),
                                R.color.cvStats
                            )
                        )
                        binding.tvStats.setTextColor(
                            getColor(
                                requireContext(),
                                R.color.cvTextStats
                            )
                        )
                        binding.btnStats.setOnClickListener {
                            showDialog(
                                onStatSelected = {
                                    statsViewModel.sortPlayersBy(it)
                                    initMinHeight()
                                })
                        }
                    } else {
                        binding.btnStats.setCardBackgroundColor(
                            getColor(
                                requireContext(),
                                R.color.grey_selected_soft
                            )
                        )
                        binding.cvStatsBackground.setCardBackgroundColor(
                            getColor(
                                requireContext(),
                                R.color.grey_selected_soft
                            )
                        )
                        binding.tvStats.setTextColor(getColor(requireContext(), R.color.black))
                    }
                }
            }
        }

        binding.psSeason.setOnClickListener{

        }
    }

    private fun initRecyclerView(playersStats: List<PlayerStatsModel?>) {
        playersStatsAdapter = PlayerStatsAdapter(playersStats)
        binding.rvStats.apply {
            adapter = playersStatsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initChampions(
        champion: PlayerStatsModel?,
        second: PlayerStatsModel?,
        third: PlayerStatsModel?,
        stat: StatType
    ) {
        playersStatsAdapter.changeStatSelected(stat)
        setChampionsOpacity()

        binding.tvChampionName.text = champion?.information?.name ?: "N/A"
        binding.tvChampionStat.text = getChampionStat(champion, stat)

        loadChampionImage(
            champion?.information?.img,
            binding.ivChampion,
            binding.tvChampionName,
            binding.tvChampionStat,
            binding.ivChampionBackground,
            binding.ivChampionCrown
        ) {
            binding.tvSecondName.text = second?.information?.name ?: "N/A"
            binding.tvSecondStat.text = getChampionStat(second, stat)

            loadChampionImage(
                second?.information?.img,
                binding.ivSecond,
                binding.tvSecondName,
                binding.tvSecondStat,
                binding.ivSecondBackground,
                binding.ivSecondCrown
            ) {
                binding.tvThirdName.text = third?.information?.name ?: "N/A"
                binding.tvThirdStat.text = getChampionStat(third, stat)

                loadChampionImage(
                    third?.information?.img,
                    binding.ivThird,
                    binding.tvThirdName,
                    binding.tvThirdStat,
                    binding.ivThirdBackground,
                    binding.ivThirdCrown
                ) {
                    statsViewModel.onResponsiveUIChanged(true)
                }
            }
        }
    }

    private fun setChampionsOpacity() {
        binding.ivChampion.alpha = 0f
        binding.ivSecond.alpha = 0f
        binding.ivThird.alpha = 0f

        binding.ivChampionBackground.alpha = 0f
        binding.ivSecondBackground.alpha = 0f
        binding.ivThirdBackground.alpha = 0f

        binding.ivChampionCrown.alpha = 0f
        binding.ivSecondCrown.alpha = 0f
        binding.ivThirdCrown.alpha = 0f

        binding.tvChampionName.alpha = 0f
        binding.tvSecondName.alpha = 0f
        binding.tvThirdName.alpha = 0f
        binding.tvChampionStat.alpha = 0f
        binding.tvSecondStat.alpha = 0f
        binding.tvThirdStat.alpha = 0f
    }

    private fun loadChampionImage(
        imageUrl: String?,
        imageView: ImageView,
        playerName: TextView,
        playerStats: TextView,
        ivBackground: ImageView,
        ivCrown: ImageView,
        onLoadComplete: () -> Unit
    ) {
        Glide.with(requireContext())
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startImageAnimation(
                        imageView,
                        playerName,
                        playerStats,
                        ivBackground,
                        ivCrown,
                    ) {
                        onLoadComplete()
                    }
                    return false
                }
            })
            .into(imageView)
    }

    private fun startImageAnimation(
        imageView: ImageView,
        playerName: TextView,
        playerStats: TextView,
        ivBackground: ImageView,
        ivCrown: ImageView,
        onAnimationFinish: () -> Unit
    ) {
        imageView.translationY = 1000f
        imageView.alpha = 1f
        val slideUp =
            ObjectAnimator.ofFloat(imageView, "translationY", 1000f, 0f).apply { duration = 1000 }

        val fadeInViews = listOf(playerName, playerStats, ivBackground, ivCrown)

        slideUp.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                fadeInViews.forEach { view ->
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
                        duration = 1000
                        start()
                    }
                }
                onAnimationFinish()
            }
        })
        slideUp.start()
    }


    private fun getChampionStat(champion: PlayerStatsModel?, stat: StatType): String {
        return when (stat) {
            PERCENTAGE -> champion?.percentage ?: "0"
            GOALS -> champion?.goals?.toString() ?: "0"
            ASSISTS -> champion?.assists?.toString() ?: "0"
            PENALTIES -> champion?.penalties?.toString() ?: "0"
            CLEAN_SHEET -> champion?.cleanSheet?.toString() ?: "0"
            GAMES_PLAYED -> champion?.gamesPlayed?.toString() ?: "0"
        }
    }

    private fun showDialog(onStatSelected: (StatType) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_stats, null)

        val dialog = builder.setView(view)
            .create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                window?.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                show()
            }

        setupStatClickListener(view, R.id.llPercentage, PERCENTAGE, onStatSelected, dialog)
        setupStatClickListener(view, R.id.llGoals, GOALS, onStatSelected, dialog)
        setupStatClickListener(view, R.id.llAssists, ASSISTS, onStatSelected, dialog)
        setupStatClickListener(view, R.id.llPenalties, PENALTIES, onStatSelected, dialog)
        setupStatClickListener(view, R.id.llCleanSheet, CLEAN_SHEET, onStatSelected, dialog)
        setupStatClickListener(view, R.id.llGamesPlayed, GAMES_PLAYED, onStatSelected, dialog)
    }

    private fun setupStatClickListener(
        view: View,
        layoutId: Int,
        statType: StatType,
        onStatSelected: (StatType) -> Unit,
        dialog: AlertDialog
    ) {
        view.findViewById<LinearLayout>(layoutId).setOnClickListener {
            binding.tvStats.text = when (statType) {
                PERCENTAGE -> getString(R.string.percentage)
                GOALS -> getString(R.string.goals)
                ASSISTS -> getString(R.string.assists)
                PENALTIES -> getString(R.string.penalties)
                CLEAN_SHEET -> getString(R.string.clean_sheet)
                GAMES_PLAYED -> getString(R.string.games_played)
            }
            onStatSelected(statType)
            dialog.dismiss()
        }
    }

    private fun initTextViewGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        textView.paint.shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                getColor(requireContext(), R.color.tvBackgroundEnd),
                getColor(requireContext(), R.color.tvBackgroundStart)
            ),
            null, Shader.TileMode.REPEAT
        )
    }

    private fun initRecyclerViewScroll() {
        var currentHeight = minHeight
        val scrollThreshold = 3
        val heightChangeFactor = 0.5

        binding.rvStats.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (abs(dy) < scrollThreshold) return

                val change = (dy * heightChangeFactor).toInt()
                val newHeight = (currentHeight + change).coerceIn(minHeight, maxHeight)

                if (newHeight != currentHeight) {
                    val layoutParams = binding.clStats.layoutParams
                    layoutParams.height = newHeight
                    binding.clStats.layoutParams = layoutParams
                    currentHeight = newHeight

                    val newOpacity =
                        1 - ((newHeight - minHeight) / (maxHeight - minHeight).toFloat())
                    statsViewModel.onOpacityChanged(newOpacity)
                }
            }
        })
    }


    // TODO
    private fun startReflectionAnimation(textView: TextView) {
        val width = textView.width.toFloat()
        val reflection = ObjectAnimator.ofFloat(textView, "translationX", -width, width)
        reflection.duration = 2000
        reflection.repeatCount = ValueAnimator.INFINITE
        reflection.repeatMode = ValueAnimator.RESTART
        reflection.start()
    }
}