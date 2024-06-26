package com.sgalera.gaztelubira.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sgalera.gaztelubira.databinding.FragmentTeamBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.ui.team.adapters.DefendersAdapter
import com.sgalera.gaztelubira.ui.team.adapters.ForwardsAdapter
import com.sgalera.gaztelubira.ui.team.adapters.GoalKeeperAdapter
import com.sgalera.gaztelubira.ui.team.adapters.MidfieldersAdapter
import com.sgalera.gaztelubira.ui.team.adapters.TechnicalStaffAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!

    private val teamViewModel by viewModels<TeamViewModel>()

    private var goalKeeperList = mutableListOf<PlayerInformation>()
    private var defendersList = mutableListOf<PlayerInformation>()
    private var midfieldersList = mutableListOf<PlayerInformation>()
    private var forwardsList = mutableListOf<PlayerInformation>()
    private var technicalStaffList = mutableListOf<PlayerInformation>()

    private lateinit var goalKeeperAdapters: GoalKeeperAdapter
    private lateinit var defendersAdapter: DefendersAdapter
    private lateinit var midfieldersAdapter: MidfieldersAdapter
    private lateinit var forwardsAdapter: ForwardsAdapter
    private lateinit var technicalStaffAdapter: TechnicalStaffAdapter

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
    }

    private fun initUI() {
        fetchPlayerInformation()
    }

    private fun fetchPlayerInformation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                teamViewModel.players.collect{teamState ->
                    when(teamState) {
                        is TeamInfoState.Loading -> loadingState()
                        is TeamInfoState.Error -> errorState(teamState.error)
                        is TeamInfoState.Success -> successState(teamState.playerList)
                    }
                }

            }
        }
    }

    private fun loadingState() {
        binding.progressBarLoadingTeamTemplate.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.progressBarLoadingTeamTemplate.visibility = View.GONE
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun successState(playerList: List<PlayerInformation>){
        binding.progressBarLoadingTeamTemplate.visibility = View.GONE
        binding.scrollViewTeamTemplate.visibility = View.VISIBLE
        initLists(playerList)
    }

    private fun initLists(playerList: List<PlayerInformation>) {
        goalKeeperList.clear()
        defendersList.clear()
        midfieldersList.clear()
        forwardsList.clear()
        technicalStaffList.clear()
        for (player in playerList){
            when(player.position){
                "Goal keeper" -> goalKeeperList.add(player)
                "Defender" -> defendersList.add(player)
                "Midfielder" -> midfieldersList.add(player)
                "Forward" -> forwardsList.add(player)
                "Technical Staff" -> technicalStaffList.add(player)
            }
        }
        initRecyclerViews()
    }

    private fun initRecyclerViews() {
        initGoalKeepers()
        initDefenders()
        initMidfielders()
        initForwards()
        initTechnicalStaff()
    }

    private fun initGoalKeepers() {
        goalKeeperAdapters = GoalKeeperAdapter(goalKeeperList.sortedBy { it.dorsal },
            onItemSelected = {
                findNavController().navigate(
                    TeamFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                        name = it.name,
                        dorsal = it.dorsal!!,
                        image = it.img,
                        reference = it.stats!!.path
                    )
                )
            })
        binding.rvGoalKeepers.apply {
            adapter = goalKeeperAdapters
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initDefenders() {
        defendersAdapter = DefendersAdapter(defendersList.sortedBy { it.dorsal },
            onItemSelected = {
                findNavController().navigate(
                    TeamFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                        name = it.name,
                        dorsal = it.dorsal!!,
                        image = it.img,
                        reference = it.stats!!.path
                    )
                )
            })
        binding.rvDefenders.apply {
            adapter = defendersAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initMidfielders() {
        midfieldersAdapter = MidfieldersAdapter(midfieldersList.sortedBy { it.dorsal },
            onItemSelected = {
                findNavController().navigate(
                    TeamFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                        name = it.name,
                        dorsal = it.dorsal!!,
                        image = it.img,
                        reference = it.stats!!.path
                    )
                )
            })
        binding.rvMidFielders.apply {
            adapter = midfieldersAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initForwards() {
        forwardsAdapter = ForwardsAdapter(forwardsList.sortedBy { it.dorsal },
            onItemSelected = {
                findNavController().navigate(
                    TeamFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                        name = it.name,
                        dorsal = it.dorsal!!,
                        image = it.img,
                        reference = it.stats!!.path
                    )
                )
            })
        binding.rvForwards.apply {
            adapter = forwardsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initTechnicalStaff() {
        technicalStaffAdapter = TechnicalStaffAdapter(technicalStaffList,
            onItemSelected = {
                findNavController().navigate(
                    TeamFragmentDirections.actionTeamFragmentToPlayerInformationDetail(
                        name = it.name,
                        dorsal = 0,
                        image = it.img,
                        reference = null
                    )
                )
            }
        )
        binding.rvTechnicalStaff.apply {
            adapter = technicalStaffAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            isNestedScrollingEnabled = false
        }
    }

}