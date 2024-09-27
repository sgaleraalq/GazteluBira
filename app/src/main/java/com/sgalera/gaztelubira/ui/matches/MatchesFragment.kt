package com.sgalera.gaztelubira.ui.matches

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.databinding.FragmentMatchesBinding
import com.sgalera.gaztelubira.ui.manager.SharedPreferences
import com.sgalera.gaztelubira.ui.matches.adapter.MatchesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesFragment: Fragment() {
    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var journey: Int = 0
    private var id: Int = 0

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
        matchesViewModel.init()
        initMatchesList()
//        initListeners()
//        initUIState()
//        initRecyclerView()
    }

    private fun initMatchesList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.matchesList.collect { matchesList ->
                    matchesAdapter.updateList(matchesList)
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnInsertGame.setOnClickListener {
            insertGame()
        }
    }

    private fun initComponents() {
        matchesAdapter = MatchesAdapter(
            onItemSelected = { id ->
                findNavController().navigate(
                    MatchesFragmentDirections.actionMatchesFragmentToDetailMatchActivity(id)
                )
            }
        )
        checkToken()
    }

    private fun checkToken() {
        sharedPreferences = SharedPreferences(requireContext())
        val token = sharedPreferences.getAdminToken()
        if (token != null){
            binding.btnInsertGame.visibility = View.VISIBLE
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.state.collect {
                    when (it) {
                        MatchInfoState.Loading -> loadingState()
                        is MatchInfoState.Error -> errorState(it.error)
                        is MatchInfoState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun successState(state: MatchInfoState.Success) {
        binding.progressBar.visibility = View.INVISIBLE
//        matchesAdapter.updateList(state.matchesList.sortedByDescending { it.id })

        val lastMatch = state.matchesList.sortedByDescending { it.id }[0]
        id = lastMatch.id
        journey = lastMatch.journey.split(" ")[1].toInt()
    }

    private fun errorState(error: String) {
        binding.progressBar.visibility = View.INVISIBLE
        Toast.makeText(context, "Error $error", Toast.LENGTH_SHORT).show()
    }

    private fun loadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        binding.recyclerViewMatches.apply {
            adapter = matchesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun insertGame() {
        if (id > 0 && journey > 0){
            findNavController().navigate(
                MatchesFragmentDirections.actionMatchesFragmentToInsertGame(journey+1, id+1)
            )
        } else{
            Toast.makeText(context, "Please wait until matches are loaded", Toast.LENGTH_SHORT).show()
        }
    }
}