package com.sgalera.gaztelubira.ui.matches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.databinding.FragmentMatchesBinding
import com.sgalera.gaztelubira.domain.model.Match
import com.sgalera.gaztelubira.domain.model.Team.*
import com.sgalera.gaztelubira.ui.matches.adapter.MatchesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFragment : Fragment() {

    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private lateinit var matchesAdapter: MatchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initList()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        matchesAdapter = MatchesAdapter(
            listOf(
                Match(
                    local = GazteluBira,
                    visitor = Aterbea,
                    localGoals = 3,
                    visitorGoals = 1
                ),
                Match(
                    local = GazteluBira,
                    visitor = Aterbea,
                    localGoals = 3,
                    visitorGoals = 1
                )
            )
        )

        binding.recyclerViewMatches.apply {
            adapter = matchesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initList() {
        // Get matches from call here
    }
}