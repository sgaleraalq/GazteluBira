package com.sgalera.gaztelubira.ui.stats

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStats1Binding
import com.sgalera.gaztelubira.domain.model.UIState
import com.sgalera.gaztelubira.domain.model.players.PlayerStatsModel
import com.sgalera.gaztelubira.ui.stats.adapter.PlayerStatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStats1Binding? = null
    private val binding get() = _binding!!

    private val statsViewModel by viewModels<StatsViewModel>()
    private lateinit var playersStatsAdapter: PlayerStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStats1Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
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
        binding.btnStats.setOnClickListener {
            showDialog(
                onStatSelected = {
                    statsViewModel.sortPlayersBy(it)
                }
            )
        }

        // TODO be removed
//        binding.btnOpacity.setOnClickListener {
//            if (binding.clChampionsView.alpha == 1f) {
//                statsViewModel.onOpacityChanged(0.5f)
//            } else {
//                statsViewModel.onOpacityChanged(1f)
//            }
//        }
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
        Glide.with(requireContext()).load(champion?.information?.img).into(binding.ivChampion)
        Glide.with(requireContext()).load(second?.information?.img).into(binding.ivSecond)
        Glide.with(requireContext()).load(third?.information?.img).into(binding.ivThird)

        binding.tvChampionName.text = champion?.information?.name
        binding.tvSecondName.text = second?.information?.name
        binding.tvThirdName.text = third?.information?.name

        binding.tvChampionStat.text = when(playersStatsAdapter.statSelected) {
            StatType.PERCENTAGE -> champion?.percentage + " %"
            StatType.GOALS -> champion?.goals.toString()
            StatType.ASSISTS -> champion?.assists.toString()
            StatType.PENALTIES -> champion?.penalties.toString()
            StatType.CLEAN_SHEET -> champion?.cleanSheet.toString()
            StatType.GAMES_PLAYED -> champion?.gamesPlayed.toString()
        }

        binding.tvSecondStat.text = when(playersStatsAdapter.statSelected) {
            StatType.PERCENTAGE -> second?.percentage + " %"
            StatType.GOALS -> second?.goals.toString()
            StatType.ASSISTS -> second?.assists.toString()
            StatType.PENALTIES -> second?.penalties.toString()
            StatType.CLEAN_SHEET -> second?.cleanSheet.toString()
            StatType.GAMES_PLAYED -> second?.gamesPlayed.toString()
        }

        binding.tvThirdStat.text = when(playersStatsAdapter.statSelected) {
            StatType.PERCENTAGE -> third?.percentage + " %"
            StatType.GOALS -> third?.goals.toString()
            StatType.ASSISTS -> third?.assists.toString()
            StatType.PENALTIES -> third?.penalties.toString()
            StatType.CLEAN_SHEET -> third?.cleanSheet.toString()
            StatType.GAMES_PLAYED -> third?.gamesPlayed.toString()
        }
    }

    private fun showDialog(onStatSelected: (StatType) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(
            R.layout.dialog_stats,
            builder.create().window?.decorView as? ViewGroup,
            false
        )

        with(builder) {
            setView(view)
            create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                window?.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                show()
                view.findViewById<LinearLayout>(R.id.llPercentage).setOnClickListener {
                    binding.tvStats.text = getString(R.string.percentage)
                    onStatSelected(StatType.PERCENTAGE)
                    dismiss()
                }
                view.findViewById<LinearLayout>(R.id.llGoals).setOnClickListener {
                    binding.tvStats.text = getString(R.string.goals)
                    onStatSelected(StatType.GOALS)
                    dismiss()
                }
                view.findViewById<LinearLayout>(R.id.llAssists).setOnClickListener {
                    binding.tvStats.text = getString(R.string.assists)
                    onStatSelected(StatType.ASSISTS)
                    dismiss()
                }
                view.findViewById<LinearLayout>(R.id.llPenalties).setOnClickListener {
                    binding.tvStats.text = getString(R.string.penalties)
                    onStatSelected(StatType.PENALTIES)
                    dismiss()
                }
                view.findViewById<LinearLayout>(R.id.llCleanSheet).setOnClickListener {
                    binding.tvStats.text = getString(R.string.clean_sheet)
                    onStatSelected(StatType.CLEAN_SHEET)
                    dismiss()
                }
                view.findViewById<LinearLayout>(R.id.llGamesPlayed).setOnClickListener {
                    binding.tvStats.text = getString(R.string.games_played)
                    onStatSelected(StatType.GAMES_PLAYED)
                    dismiss()
                }
            }
        }
    }

    private fun initTextViewGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        textView.paint.shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.tvBackgroundEnd),
                ContextCompat.getColor(requireContext(), R.color.tvBackgroundStart)
            ),
            null, Shader.TileMode.REPEAT
        )
    }

    private fun initRecyclerViewScroll() {
//        val initialHeight = binding.clStats.height
//        val maxHeight = 1000 // Define una altura máxima deseada para clStats
//        val stopAtY = 100f // Valor donde quieres que clStats se detenga
//
//        binding.rvStats.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                // Obtener la posición actual en pantalla de clStats
//                val location = IntArray(2)
//                binding.clStats.getLocationOnScreen(location)
//                val clStatsY = location[1]
//
//                // Si clStats ha llegado a la posición deseada y estamos haciendo scroll hacia arriba, no hagas nada
//                if (clStatsY <= stopAtY && dy > 0) {
//                    return
//                }
//
//                // Calcular nueva altura basada en el desplazamiento (scroll)
//                val newHeight = (binding.clStats.height - dy).coerceIn(initialHeight, maxHeight)
//
//                // Asignar la nueva altura
//                val layoutParams = binding.clStats.layoutParams
//                layoutParams.height = newHeight
//                binding.clStats.layoutParams = layoutParams
//            }
//        })
    }

    // Función para actualizar la posición Y de clStats
    private fun updateTranslationY(newTranslationY: Float) {
        binding.clStats.translationY = newTranslationY
    }

    private fun hideHeader() {
        binding.ivChampion.visibility = View.GONE
    }

    private fun showHeader() {
        binding.ivChampion.visibility = View.VISIBLE
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



//    private fun initUI() {
//        initComponents()
//        initListeners()
//    }
//
//    private fun initComponents() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                statsViewModel.uiState.collect { uiState ->
//                    when (uiState) {
//                        UIState.Loading -> { onLoading() }
//                        is UIState.Error -> { onError() }
//                        UIState.Success -> {}
//                    }
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                statsViewModel.playersStats.collect { playersStats ->
//                    onSuccess(playersStats, playersStats.firstOrNull())
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                statsViewModel.isAdmin.collect { isAdmin ->
//                    if (isAdmin) {
//                        binding.tvLoggedAsAdmin.visibility = View.VISIBLE
//                        binding.cvAdmin.visibility = View.GONE
//                        binding.ibAdminLogOut.visibility = View.VISIBLE
//                    } else {
//                        binding.tvLoggedAsAdmin.visibility = View.GONE
//                        binding.cvAdmin.visibility = View.VISIBLE
//                        binding.ibAdminLogOut.visibility = View.GONE
//                    }
//                }
//            }
//        }
//    }
//
//    private fun onLoading() {
//        binding.pbLoading.visibility = View.VISIBLE
//        binding.pbLoadingChampion.visibility = View.VISIBLE
//    }
//
//    private fun onError() {
//        binding.pbLoading.visibility = View.GONE
//        binding.pbLoadingChampion.visibility = View.GONE
//        binding.ivError.visibility = View.VISIBLE
//        binding.tvError.visibility = View.VISIBLE
//        Toast.makeText(context, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
//    }
//
//    private fun onSuccess(playersListStats: List<PlayerStatsModel?>, champion: PlayerStatsModel?) {
//        binding.pbLoading.visibility = View.GONE
//
//        // Show the table
//        binding.tlClassification.removeAllViews()
//        playersListStats.forEachIndexed { index, player ->
//            binding.tlClassification.addView(insertRow(player, index))
//        }
//
//        // Show champion card
//        if (champion != null){
//            showImage(champion)
//        }
//
//        initButtonListeners()
//    }
//
//    private fun insertRow(player: PlayerStatsModel?, index: Int): View {
//        val binding = ItemTableRowBinding.inflate(layoutInflater)
//        val arrow = getArrow(player)
//
//        with(binding) {
//            ivArrow.setImageResource(arrow)
//            tvRanking.text = getString(R.string.player_ranking, index + 1)
//            tvPlayerName.text = player?.information?.name ?: getString(R.string.could_not_retrieve)
//            tvPlayerProportion.text = player?.percentage
//            tvPlayerGoals.text = player?.goals.toString()
//            tvPlayerAssists.text = player?.assists.toString()
//            tvPlayerPenalties.text = player?.penalties.toString()
//            tvPlayerCleanSheet.text = player?.cleanSheet.toString()
//            tvPlayerGames.text = player?.gamesPlayed.toString()
//        }
//        return binding.root
//    }
//
//    private fun initListeners() {
//        binding.cvAdmin.setOnClickListener { showAdminDialog() }
//        binding.ibAdminLogOut.setOnClickListener { statsViewModel.adminLogOut() }
//    }
//
//    private fun initButtonListeners() {
//        binding.percentageIcon.setOnClickListener { statsViewModel.sortPlayersBy(PERCENTAGE) { changeButtonColor(it) } }
//        binding.goalsIcon.setOnClickListener { statsViewModel.sortPlayersBy(GOALS) { changeButtonColor(it) } }
//        binding.assistsIcon.setOnClickListener { statsViewModel.sortPlayersBy(ASSISTS) { changeButtonColor(it) } }
//        binding.penaltiesIcon.setOnClickListener { statsViewModel.sortPlayersBy(PENALTIES) { changeButtonColor(it) } }
//        binding.cleanSheetIcon.setOnClickListener { statsViewModel.sortPlayersBy(CLEAN_SHEET) { changeButtonColor(it) } }
//        binding.gamesIcon.setOnClickListener { statsViewModel.sortPlayersBy(GAMES_PLAYED) { changeButtonColor(it) } }
//    }
//
//    private fun changeButtonColor(stat: StatType){
//        val buttonMap = mapOf(
//            PERCENTAGE to binding.percentageIcon,
//            GOALS to binding.goalsIcon,
//            ASSISTS to binding.assistsIcon,
//            PENALTIES to binding.penaltiesIcon,
//            CLEAN_SHEET to binding.cleanSheetIcon,
//            GAMES_PLAYED to binding.gamesIcon
//        )
//        buttonMap[stat]?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_80_opacity))
//        buttonMap.filterKeys { it != stat }.forEach { (_, button) ->
//            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
//        }
//    }
//
//    private fun showImage(player: PlayerStatsModel) {
//        makeImageElementsVisible()
//        binding.tvNameChampion.text = player.information?.name
//        binding.tvChampionGoals.text = String.format(Locale.getDefault(),"%,d", player.goals)
//        binding.tvChampionAssists.text = String.format(Locale.getDefault(), "%,d", player.assists)
//        Glide.with(requireContext())
//            .load(player.information?.img ?: PLAYER_NO_IMAGE)
//            .into(binding.ivChampion)
//    }
//
//    private fun makeImageElementsVisible() {
//        binding.ivIconGoals.visibility = View.VISIBLE
//        binding.ivIconAssists.visibility = View.VISIBLE
//        binding.pbLoadingChampion.visibility = View.INVISIBLE
//    }
//
//    @SuppressLint("InflateParams")
//    private fun showAdminDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_admin_dialog, null)
//
//        with(builder){
//            setView(view)
//            create().apply {
//                window?.setBackgroundDrawableResource(android.R.color.transparent)
//                show()
//
//                val password = view.findViewById<EditText>(R.id.etAdminPassword)
//                view.findViewById<AppCompatButton>(R.id.btnLogInAdmin)
//                    .setOnClickListener {
//                        val result = statsViewModel.adminLogIn(password.text.toString())
//                        if (result){
//                            Toast.makeText(context, getString(R.string.correct_password), Toast.LENGTH_SHORT).show()
//                            dismiss()
//                        } else {
//                            Toast.makeText(context, getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            }
//        }
//    }
//
//    private fun getArrow(player: PlayerStatsModel?): Int {
//        return if (player == null){
//            R.drawable.ic_arrow_equal
//        } else if (player.lastRanking < player.ranking) {
//            R.drawable.ic_arrow_down
//        } else if (player.lastRanking > player.ranking) {
//            R.drawable.ic_arrow_up
//        } else {
//            R.drawable.ic_arrow_equal
//        }
//    }
}