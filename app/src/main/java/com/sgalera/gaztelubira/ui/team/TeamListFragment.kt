package com.sgalera.gaztelubira.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentTeamBinding
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.PlayerPosition.DEFENDER
import com.sgalera.gaztelubira.domain.model.PlayerPosition.FORWARD
import com.sgalera.gaztelubira.domain.model.PlayerPosition.GOALKEEPER
import com.sgalera.gaztelubira.domain.model.PlayerPosition.MIDFIELDER
import com.sgalera.gaztelubira.domain.model.PlayerPosition.TECHNICAL_STAFF
import com.sgalera.gaztelubira.domain.model.PlayerPosition.UNKNOWN
import com.sgalera.gaztelubira.ui.team.adapters.PlayersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamListFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!

    private val teamListViewModel by viewModels<TeamListViewModel>()
    private var goalKeeperList = mutableListOf<PlayerModel>()
    private var defendersList = mutableListOf<PlayerModel>()
    private var midfieldersList = mutableListOf<PlayerModel>()
    private var forwardsList = mutableListOf<PlayerModel>()
    private var technicalStaffList = mutableListOf<PlayerModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initAdapters()
    }

    private fun initUI() {
        fetchInformation()
    }

    private fun initAdapters() {
        val navigateToPlayerDetail: (PlayerModel) -> Unit = { player ->
            findNavController().navigate(
                TeamListFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                    name = player.name,
                    dorsal = player.dorsal ?: 0,
                    image = player.img,
                    position = player.position.name
                )
            )
        }
        bindAdapters(
            recyclerView = binding.rvGoalKeepers,
            adapter = PlayersAdapter(
                playersList = goalKeeperList,
                onPlayerSelected = { navigateToPlayerDetail(it) }
            )
        )
        bindAdapters(
            recyclerView = binding.rvDefenders,
            adapter = PlayersAdapter(
                playersList = defendersList,
                onPlayerSelected = { navigateToPlayerDetail(it) }
            )
        )
        bindAdapters(
            recyclerView = binding.rvMidFielders,
            adapter = PlayersAdapter(
                playersList = midfieldersList,
                onPlayerSelected = { navigateToPlayerDetail(it) }
            )
        )
        bindAdapters(
            recyclerView = binding.rvForwards,
            adapter = PlayersAdapter(
                playersList = forwardsList,
                onPlayerSelected = { navigateToPlayerDetail(it) }
            )
        )
        bindAdapters(
            recyclerView = binding.rvTechnicalStaff,
            adapter = PlayersAdapter(
                playersList = technicalStaffList,
                onPlayerSelected = { navigateToPlayerDetail(it) }
            )
        )
    }

    private fun fetchInformation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamListViewModel.playersList.collect { teamList ->
                    initPlayersList(teamList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamListViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        TeamInfoState.Error -> onError()
                        TeamInfoState.Loading -> {}
                        TeamInfoState.Success -> onSuccess()
                    }
                }
            }
        }
    }

    private fun onSuccess() {
        binding.progressBarLoadingTeamTemplate.visibility = View.GONE
        binding.scrollViewTeamTemplate.visibility = View.VISIBLE
    }

    private fun onError() {
        binding.progressBarLoadingTeamTemplate.visibility = View.GONE
        Toast.makeText(
            requireContext(),
            getString(R.string.an_error_has_occurred),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initPlayersList(teamList: List<PlayerModel?>) {
        teamList.forEach { player -> inflateRecyclerView(player) }
    }

    private fun inflateRecyclerView(player: PlayerModel?) {
        when (player?.position) {
            GOALKEEPER -> goalKeeperList.add(player)
            DEFENDER -> defendersList.add(player)
            MIDFIELDER -> midfieldersList.add(player)
            FORWARD -> forwardsList.add(player)
            TECHNICAL_STAFF -> technicalStaffList.add(player)
            UNKNOWN -> {}
            null -> {}
        }
    }

    private fun bindAdapters(recyclerView: RecyclerView, adapter: PlayersAdapter) {
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }
}