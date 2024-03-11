package com.sgalera.gaztelubira.ui.player_compare

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.ui.player_compare.items.PlayerComparisonPopUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlayerComparisonViewModel>()

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
        initListeners()
    }

    private fun initListeners() {
        binding.tvChooseTwoPlayers.setOnClickListener {
            showPlayerComparisonPopUp()
        }
    }

    private fun showPlayerComparisonPopUp() {
        val popUp = PlayerComparisonPopUp(
            this.requireContext(),
            R.layout.item_popup,
            viewModel.getPlayerList(),
            null,
            null
        )
        popUp.show()
    }
}