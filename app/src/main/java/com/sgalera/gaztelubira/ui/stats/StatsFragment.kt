package com.sgalera.gaztelubira.ui.stats

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStatsBinding
import com.sgalera.gaztelubira.databinding.ItemTableRowBinding
import com.sgalera.gaztelubira.domain.model.Credentials
import com.sgalera.gaztelubira.domain.model.PlayerStatsModel
import com.sgalera.gaztelubira.ui.stats.StatType.ASSISTS
import com.sgalera.gaztelubira.ui.stats.StatType.CLEAN_SHEET
import com.sgalera.gaztelubira.ui.stats.StatType.GAMES_PLAYED
import com.sgalera.gaztelubira.ui.stats.StatType.GOALS
import com.sgalera.gaztelubira.ui.stats.StatType.PENALTIES
import com.sgalera.gaztelubira.ui.stats.StatType.PERCENTAGE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var credentials: Credentials
    private val statsViewModel by viewModels<StatsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCredentials()
        initUI()
    }

    private fun getCredentials(){
        arguments?.let {
            credentials =
                Credentials(
                    isAdmin = it.getBoolean("isAdmin"),
                    player = it.getString("player"),
                    year = it.getInt("year")
                )
        }
    }

    private fun initUI() {
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        statsViewModel.getPlayersStats(credentials.year)
        statsViewModel.initAdmin(credentials.isAdmin)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                statsViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        StatsUiState.Loading -> {  }
                        is StatsUiState.Error -> { onError() }
                        is StatsUiState.Success -> { onSuccess(uiState.playersStats, uiState.champion) }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.isAdmin.collect { isAdmin ->
                    if (isAdmin) {
                        binding.tvLoggedAsAdmin.visibility = View.VISIBLE
                        binding.cvAdmin.visibility = View.GONE
                        binding.ibAdminLogOut.visibility = View.VISIBLE
                    } else {
                        binding.tvLoggedAsAdmin.visibility = View.GONE
                        binding.cvAdmin.visibility = View.VISIBLE
                        binding.ibAdminLogOut.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun onError() {
        binding.pbLoading.visibility = View.GONE
        Toast.makeText(context, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(playersListStats: List<PlayerStatsModel>, champion: PlayerStatsModel?) {
        binding.pbLoading.visibility = View.GONE

        // Show the table
        binding.tlClassification.removeAllViews()
        playersListStats.forEachIndexed { index, player ->
            binding.tlClassification.addView(insertRow(player, index))
        }

        // Show champion card
        if (champion != null){
            showImage(champion)
        }

        initButtonListeners()
    }

    private fun insertRow(player: PlayerStatsModel, index: Int): View {
        val binding = ItemTableRowBinding.inflate(layoutInflater)
        val arrow = getArrow(player)

        with(binding) {
            ivArrow.setImageResource(arrow)
            tvRanking.text = getString(R.string.player_ranking, index + 1)
            tvPlayerName.text = player.information?.name ?: getString(R.string.could_not_retrieve)
            tvPlayerProportion.text = player.percentage
            tvPlayerGoals.text = player.goals.toString()
            tvPlayerAssists.text = player.assists.toString()
            tvPlayerPenalties.text = player.penalties.toString()
            tvPlayerCleanSheet.text = player.cleanSheet.toString()
            tvPlayerGames.text = player.gamesPlayed.toString()
        }

        return binding.root
    }

    private fun initListeners() {
        binding.cvAdmin.setOnClickListener { showAdminDialog() }
        binding.ibAdminLogOut.setOnClickListener { statsViewModel.adminLogOut() }
    }

    private fun initButtonListeners() {
        binding.percentageIcon.setOnClickListener { statsViewModel.sortPlayersBy(PERCENTAGE) { changeButtonColor(it) } }
        binding.goalsIcon.setOnClickListener { statsViewModel.sortPlayersBy(GOALS) { changeButtonColor(it) } }
        binding.assistsIcon.setOnClickListener { statsViewModel.sortPlayersBy(ASSISTS) { changeButtonColor(it) } }
        binding.penaltiesIcon.setOnClickListener { statsViewModel.sortPlayersBy(PENALTIES) { changeButtonColor(it) } }
        binding.cleanSheetIcon.setOnClickListener { statsViewModel.sortPlayersBy(CLEAN_SHEET) { changeButtonColor(it) } }
        binding.gamesIcon.setOnClickListener { statsViewModel.sortPlayersBy(GAMES_PLAYED) { changeButtonColor(it) } }
    }

    private fun changeButtonColor(stat: StatType){
        val buttonMap = mapOf(
            PERCENTAGE to binding.percentageIcon,
            GOALS to binding.goalsIcon,
            ASSISTS to binding.assistsIcon,
            PENALTIES to binding.penaltiesIcon,
            CLEAN_SHEET to binding.cleanSheetIcon,
            GAMES_PLAYED to binding.gamesIcon
        )
        buttonMap[stat]?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_80_opacity))
        buttonMap.filterKeys { it != stat }.forEach { (_, button) ->
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primary))
        }
    }

    private fun showImage(player: PlayerStatsModel) {
        makeImageElementsVisible()
        binding.tvNameChampion.text = player.information!!.name
        binding.tvChampionGoals.text = player.goals.toString()
        binding.tvChampionAssists.text = player.assists.toString()
        Glide.with(requireContext())
            .load(player.information.img)
            .into(binding.ivChampion)
    }

    private fun makeImageElementsVisible() {
        binding.ivIconGoals.visibility = View.VISIBLE
        binding.ivIconAssists.visibility = View.VISIBLE
        binding.pbLoadingChampion.visibility = View.INVISIBLE
    }

    @SuppressLint("InflateParams")
    private fun showAdminDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_admin_popup, null)

        with(builder){
            setView(view)
            create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()

                val password = view.findViewById<EditText>(R.id.etAdminPassword)
                view.findViewById<AppCompatButton>(R.id.btnLogInAdmin)
                    .setOnClickListener {
                        val result = statsViewModel.adminLogIn(password.text.toString())
                        if (result){
                            Toast.makeText(context, getString(R.string.correct_password), Toast.LENGTH_SHORT).show()
                            dismiss()
                        } else {
                            Toast.makeText(context, getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun getArrow(player: PlayerStatsModel): Int {
        return if (player.lastRanking < player.ranking) {
            R.drawable.ic_arrow_down
        } else if (player.lastRanking > player.ranking) {
            R.drawable.ic_arrow_up
        } else {
            R.drawable.ic_arrow_equal
        }
    }
}