package com.sgalera.gaztelubira.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentStatsBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private val statsViewModel by viewModels<StatsViewModel>()

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        initUI()
    }

    private fun initUI() {
        initComponents()
        initListeners()
    }

    private fun initListeners() {
        binding.cvAdmin.setOnClickListener {
            showAdminPopUp()
        }
    }

    private fun initComponents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                statsViewModel.state.collect { it ->
                    when (it) {
                        StatsState.Loading -> loadingState()
                        is StatsState.Success -> successState(it.data.sortedByDescending { it.percentage })
                        is StatsState.Error -> errorState(it.message)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.pbLoading.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun successState(stats: List<PlayerStats>) {
        binding.pbLoading.visibility = View.GONE
        for (player in stats) {
            val view = setRowPlayerView(player)
            binding.tlClassification.addView(view)
        }
    }

    private fun showAdminPopUp() {
        println("Admin")
    }

    private fun setRowPlayerView(player: PlayerStats): View {
        val view = layoutInflater.inflate(R.layout.item_table_row, null)
        val arrow = getArrow(player)
        view.findViewById<ImageView>(R.id.ivArrow).setImageResource(arrow)
        view.findViewById<TextView>(R.id.tvPlayerName).text = player.name.toString()
        view.findViewById<TextView>(R.id.tvPlayerProportion).text = player.percentage
        view.findViewById<TextView>(R.id.tvPlayerGoals).text = player.goals.toString()
        view.findViewById<TextView>(R.id.tvPlayerAssists).text = player.assists.toString()
        view.findViewById<TextView>(R.id.tvPlayerBigMistakes).text = player.bigMistakes.toString()
        view.findViewById<TextView>(R.id.tvPlayerCleanSheet).text = player.cleanSheet.toString()
        view.findViewById<TextView>(R.id.tvPlayerGames).text = player.gamesPlayed.toString()

        return view
    }

    private fun getArrow(player: PlayerStats): Int {
        return if (player.lastRanking < player.ranking) {
            R.drawable.ic_arrow_up
        } else if (player.lastRanking > player.ranking) {
            R.drawable.ic_arrow_down
        } else {
            R.drawable.ic_arrow_equal
        }
    }
}