package com.sgalera.gaztelubira.ui.matches

import android.os.Bundle
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
                matchesViewModel.matches.collect {
                    matchesAdapter.updateList(it)
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMatches.apply {
            adapter = matchesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}