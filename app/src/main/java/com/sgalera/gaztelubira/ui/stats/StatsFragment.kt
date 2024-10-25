package com.sgalera.gaztelubira.ui.stats

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
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

    private var minHeight = 0
    private var maxHeight = 0
    private var currentHeight = minHeight

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

    override fun onResume() {
        super.onResume()
        binding.tvStats.text = when (statsViewModel.statSelected.value) {
            PERCENTAGE -> getString(R.string.percentage)
            GOALS -> getString(R.string.goals)
            ASSISTS -> getString(R.string.assists)
            PENALTIES -> getString(R.string.penalties)
            CLEAN_SHEET -> getString(R.string.clean_sheet)
            GAMES_PLAYED -> getString(R.string.games_played)
        }
    }

    private fun initMinHeight() {
        val layoutParams = binding.clStats.layoutParams
        minHeight = layoutParams.height - 50
        maxHeight = minHeight * 2
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
    }

    private fun initRanking() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.showBlockDialog.collect {
                    if (it == true) showBlockDialog()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        UIState.Loading -> onLoadingState()
                        UIState.Success -> binding.pbStats.visibility = View.GONE
                        is UIState.Error -> onErrorState()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.playersStats.collect { playersStats ->
                    initRecyclerView(playersStats)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.seasons.collect { seasons ->
                    if (seasons.isNotEmpty()){
                        initSeasons(seasons)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.playersChampions.collect { championsMap ->
                    val champion = championsMap["Champion"]
                    val second = championsMap["Second"]
                    val third = championsMap["Third"]
                    initChampions(champion, second, third, statsViewModel.statSelected.value)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.isAdmin.collect {
                    if (it) {
                        binding.btnLogOut.visibility = View.VISIBLE
                        binding.tvAdmin.text = getString(R.string.admin_logged_in)
                    } else {
                        binding.btnLogOut.visibility = View.GONE
                        binding.tvAdmin.text = getString(R.string.admin_log_in)
                    }
                }
            }
        }
    }

    private fun onLoadingState() {
        binding.pbStats.visibility = View.VISIBLE
    }

    private fun onErrorState() {
        Toast.makeText(
            context,
            getString(R.string.main_error),
            Toast.LENGTH_SHORT
        ).show()
        binding.pbStats.visibility = View.GONE
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
                        // LISTENER
                        binding.btnStats.setOnClickListener {
                            showDialog(
                                onStatSelected = {
                                    statsViewModel.sortPlayersBy(it)
                                    resetPosition(binding.clStats)
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

        binding.btnAdmin.setOnClickListener {
            showAdminDialog()
        }

        binding.btnLogOut.setOnClickListener {
            statsViewModel.adminLogOut()
        }
    }

    private fun initRecyclerView(playersStats: List<PlayerStatsModel?>) {
        playersStatsAdapter = PlayerStatsAdapter(playersStats) { showDialog(it) }
        binding.rvStats.apply {
            adapter = playersStatsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initSeasons(seasonsList: List<String>) {
        binding.psSeason.visibility = View.VISIBLE
        binding.psSeason.text = statsViewModel.season.value.toString()
        binding.psSeason.setOnClickListener { binding.psSeason.show() }
        binding.psSeason.setItems(seasonsList)
        binding.psSeason.setOnSpinnerItemSelectedListener<String> { _, _, _, newSeason ->
            statsViewModel.onSeasonChanged(newSeason.toInt())
        }
    }

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
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
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
        val slideUp = ObjectAnimator.ofFloat(imageView, "translationY", 1000f, 0f).apply { duration = 1000 }

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
        currentHeight = minHeight
        val scrollThreshold = 5
        val heightChangeFactor = 0.2

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

                    val newOpacity = 1 - ((newHeight - minHeight) / (maxHeight - minHeight).toFloat())
                    statsViewModel.onOpacityChanged(newOpacity)
                }
            }
        })
    }

    private fun resetPosition(view: View) {
        val layoutParams = view.layoutParams
        layoutParams.height = minHeight
        view.layoutParams = layoutParams

        currentHeight = minHeight

        statsViewModel.onOpacityChanged(1f)
        view.alpha = 1f
    }

    private fun showDialog(player: PlayerStatsModel?){
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_stats_player, null)
        val dialog = builder.setView(view).create().apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
        }

        Glide.with(this).load(player?.information?.img).into(view.findViewById(R.id.ivPlayer))
        initTextViewGradient(view.findViewById(R.id.tvPlayerName))
        view.findViewById<TextView>(R.id.tvPlayerName).text = player?.information?.name
        view.findViewById<TextView>(R.id.tvGoals).text = player?.goals.toString()
        view.findViewById<TextView>(R.id.tvAssists).text = player?.assists.toString()
        view.findViewById<TextView>(R.id.tvPenalties).text = player?.penalties.toString()
        view.findViewById<TextView>(R.id.tvCleanSheet).text = player?.cleanSheet.toString()
        view.findViewById<TextView>(R.id.tvGamesPlayed).text = player?.gamesPlayed.toString()
        view.findViewById<TextView>(R.id.tvPercentage).text = player?.percentage.toString()
        view.findViewById<MaterialButton>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showAdminDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_admin_dialog, null)
        val dialog = builder.setView(view).create().apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
        }

        view.findViewById<AppCompatButton>(R.id.btnLogInAdmin).setOnClickListener {
            val password = view.findViewById<TextView>(R.id.etAdminPassword).text.toString()
            val result = statsViewModel.adminLogIn(password)
            if (!result) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.wrong_password),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun showBlockDialog() {
        binding.blockView.visibility = View.VISIBLE

        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_block, null)

        val dialog = builder.setView(view).create().apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.attributes?.windowAnimations = R.style.DialogAnimation
        }

        view.findViewById<CardView>(R.id.btnUpdate).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://github.com/sgaleraalq/GazteluBira/releases")
            }
            startActivity(intent)
        }
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }
}