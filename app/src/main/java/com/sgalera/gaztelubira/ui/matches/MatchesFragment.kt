package com.sgalera.gaztelubira.ui.matches

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.databinding.FragmentMatchesBinding
import com.sgalera.gaztelubira.ui.insert_game.InsertGameActivity
import com.sgalera.gaztelubira.ui.matches.adapter.MatchesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesFragment : Fragment() {
    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private val matchesViewModel by viewModels<MatchesViewModel>()
    private lateinit var matchesAdapter: MatchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initRecyclerView()
        initMatchesList()
        insertGameListener()
    }

    private fun initRecyclerView() {
        matchesAdapter = MatchesAdapter(
            onItemSelected = { id ->
                findNavController().navigate(
                    MatchesFragmentDirections.actionMatchesFragmentToDetailMatchActivity(id)
                )
            }
        )
        binding.recyclerViewMatches.apply {
            adapter = matchesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initMatchesList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.matchesList.collect { matchesList ->
                    matchesAdapter.updateList(matchesList)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.uiState.collect {
                    when (it) {
                        UIState.Loading -> loadingState()
                        UIState.Error -> errorState()
                        UIState.Success -> successState()
                    }
                }
            }
        }

        matchesViewModel.checkAdminStatus()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.isAdmin.collect {
                    when (it) {
                        true -> binding.btnInsertGame.visibility = View.VISIBLE
                        false -> binding.btnInsertGame.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbLoadingGames.visibility = View.VISIBLE
    }

    private fun errorState() {
        binding.pbLoadingGames.visibility = View.INVISIBLE
        Toast.makeText(context, "Error loading games", Toast.LENGTH_SHORT).show()
    }

    private fun successState() {
        binding.pbLoadingGames.visibility = View.INVISIBLE
    }

    private fun insertGameListener() {
        binding.btnInsertGame.setOnClickListener {
            startActivity(
                InsertGameActivity.create(
                    context = requireContext(),
                    id = matchesViewModel.id.value,
                    leagueJourney = matchesViewModel.leagueJourney.value
                )
            )
        }
    }
}