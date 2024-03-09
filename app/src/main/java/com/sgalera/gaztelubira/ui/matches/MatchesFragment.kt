package com.sgalera.gaztelubira.ui.matches

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.databinding.FragmentMatchesBinding
import com.sgalera.gaztelubira.ui.matches.adapter.MatchesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesFragment: Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private lateinit var matchesAdapter: MatchesAdapter

    private val matchesViewModel by viewModels<MatchesViewModel>()

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
        initComponents()
        initUIState()
        initRecyclerView()
    }

    private fun initComponents() {
        matchesAdapter = MatchesAdapter(onItemSelected = {
            findNavController().navigate(
                MatchesFragmentDirections.actionMatchesFragmentToDetailMatchActivity(it)
            )
        })
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                matchesViewModel.state.collect {
                    when (it) {
                        MatchInfoState.Loading -> loadingState()
                        is MatchInfoState.Error -> errorState()
                        is MatchInfoState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun successState(state: MatchInfoState.Success) {
        binding.progressBar.visibility = View.INVISIBLE
        matchesAdapter.updateList(state.matchesList.sortedBy { it.id })
    }

    private fun errorState() {
        Log.e("sgalera", "Ha ocurrido un error")
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
}