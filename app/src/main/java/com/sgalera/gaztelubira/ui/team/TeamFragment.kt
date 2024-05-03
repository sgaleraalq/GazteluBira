package com.sgalera.gaztelubira.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentTeamBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}