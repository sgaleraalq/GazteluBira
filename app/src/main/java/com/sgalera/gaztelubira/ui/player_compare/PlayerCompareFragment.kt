package com.sgalera.gaztelubira.ui.player_compare

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerModel
import com.sgalera.gaztelubira.ui.player_compare.adapter.PlayerComparisonAdapter
import com.sgalera.gaztelubira.ui.player_compare.detail.ComparePlayersActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!

    private val playerComparisonViewModel by viewModels<PlayerComparisonViewModel>()

    private lateinit var playerComparisonAdapter: PlayerComparisonAdapter

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
        initTextViewGradient(binding.tvSelectPlayers)
        initPlayers()
        initAdapter()
        initListeners()
    }

    private fun initPlayers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playersList.collect { players ->
                    if (playerComparisonAdapter.playersList.isEmpty()){
                        playerComparisonAdapter.updateList(players.toMutableList())
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playerOne.collect { playerOne ->
                    if (playerOne != null) {
                        binding.tvPlayerOne.text = playerOne.name
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playerTwo.collect { playerTwo ->
                    if (playerTwo != null) {
                        binding.tvPlayerTwo.text = playerTwo.name
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                playerComparisonViewModel.updatedPlayers.collect { updatedPlayers ->
                    playerComparisonAdapter.updatePlayer(updatedPlayers)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                playerComparisonViewModel.uiState.collect {
                    when (it) {
                        PlayerComparisonUiState.Error -> { onError() }
                        PlayerComparisonUiState.Loading -> { }
                        PlayerComparisonUiState.Success -> { binding.pbLoading.visibility = View.GONE }
                        PlayerComparisonUiState.HideButton -> { hideCompareButton() }
                        PlayerComparisonUiState.ShowButton -> { showCompareButton() }
                    }
                }
            }
        }
    }

    private fun onError() {
        binding.pbLoading.visibility = View.GONE
        Toast.makeText(requireContext(), getString(R.string.an_error_has_occurred), Toast.LENGTH_SHORT).show()
    }

    private fun hideCompareButton() {
        binding.pbLoading.visibility = View.GONE
        binding.btnCompare.visibility = View.GONE
    }

    private fun showCompareButton() {
        binding.pbLoading.visibility = View.GONE
        binding.btnCompare.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        playerComparisonAdapter = PlayerComparisonAdapter(
            onPlayerSelected = { playerComparisonViewModel.selectPlayer(it) }
        )
        binding.rvPlayers.apply {
            adapter = playerComparisonAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun initListeners() {
        binding.btnCompare.setOnClickListener{
            startActivity(ComparePlayersActivity.create(
                requireContext(),
                playerComparisonViewModel.playerOne.value?.name.orEmpty(),
                playerComparisonViewModel.playerTwo.value?.name.orEmpty(),
                playerComparisonViewModel.playerOne.value?.img.orEmpty(),
                playerComparisonViewModel.playerTwo.value?.img.orEmpty()
            ))
        }
    }

    private fun initTextViewGradient(textView: TextView) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        textView.paint.shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            intArrayOf(
                getColor(requireContext(), R.color.tvBackgroundEnd),
                getColor(requireContext(), R.color.tvBackgroundStart)
            ),
            null, Shader.TileMode.REPEAT
        )
    }
}