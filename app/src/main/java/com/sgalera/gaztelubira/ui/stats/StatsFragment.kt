package com.sgalera.gaztelubira.ui.stats

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStats1Binding
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
                statsViewModel.playersStats.collect{ playersStats ->
                    initRecyclerView(playersStats)
                }
            }
        }
    }

    private fun initRecyclerView(playersStats: List<PlayerStatsModel?>) {
        playersStatsAdapter = PlayerStatsAdapter(playersStats)
        binding.rvStats.apply {
            adapter = playersStatsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
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

    private fun initTextViewGradient(textView: TextView){
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