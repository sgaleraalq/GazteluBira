package com.sgalera.gaztelubira.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
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
                statsViewModel.state.collect {
                    when (it){
                        StatsState.Loading -> loadingState()
                        is StatsState.Success -> successState(it.data)
                        is StatsState.Error -> errorState(it.message)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        println(1)
    }

    private fun errorState(error: String) {
        println(1)
    }

    private fun successState(stats: List<PlayerStats>) {

    }

    private fun showAdminPopUp() {
        println("Admin")
    }

}