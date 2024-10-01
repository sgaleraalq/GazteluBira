package com.sgalera.gaztelubira.ui.player_compare

import android.app.AlertDialog
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.player_compare.adapter.PlayerComparisonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!

    private val playerComparisonViewModel by viewModels<PlayerComparisonViewModel>()

    private lateinit var playerComparisonAdapter: PlayerComparisonAdapter
    private var playersList = mutableListOf<PlayerModel?>()

//    private val dialogPlayerList: List<PlayerInformation> = InformationList.players!!

    private lateinit var playerOneReference: DocumentReference
    private lateinit var playerOne: PlayerStats

    private lateinit var playerTwoReference: DocumentReference
    private lateinit var playerTwo: PlayerStats

    private var isPlayerOneLoaded = false
    private var isPlayerTwoLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComparePlayersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initPlayers()
        initAdapter()
        initListeners()
    }

    private fun initPlayers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playersList.collect { players ->
                    if (playerComparisonAdapter.playersList.isEmpty()){
                        playerComparisonAdapter.updateList(players.toMutableList())
                    } else {
                        val indexOne = players.indexOfFirst { it?.selected == true }
                        val indexTwo = players.indexOfLast { it?.selected == true }
                        playerComparisonAdapter.updatePlayer(
                            indexOne = indexOne,
                            indexTwo = indexTwo
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playerOne.collect { playerOne ->
                    initPlayer(playerOne, 1)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playerTwo.collect { playerTwo ->
                    initPlayer(playerTwo, 2)
                }
            }
        }
    }

    private fun initAdapter() {
        playerComparisonAdapter = PlayerComparisonAdapter(
            onPlayerSelected = { playerComparisonViewModel.selectPlayer(it) }
        )
        binding.rvPlayers.apply {
            adapter = playerComparisonAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun initListeners() {
//        binding.tvChooseTwoPlayers.setOnClickListener { showPlayerComparison() }
    }

    private fun initPlayer(player: PlayerModel?, playerIdx: Int) {
        when (playerIdx){
            1 -> {
                Glide.with(requireContext()).load(player?.img).into(binding.ivPlayerOne)
                binding.tvPlayerOneName.text = player?.name
                binding.tvPlayerOneName.visibility = View.VISIBLE
            }
            else -> {
                Glide.with(requireContext()).load(player?.img).into(binding.ivPlayerTwo)
                binding.tvPlayerTwoName.text = player?.name
                binding.tvPlayerTwoName.visibility = View.VISIBLE
            }
        }
    }
}