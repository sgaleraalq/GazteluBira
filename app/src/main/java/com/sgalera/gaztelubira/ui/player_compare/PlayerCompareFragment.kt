package com.sgalera.gaztelubira.ui.player_compare

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.ui.player_compare.adapter.PopUpAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlayerComparisonViewModel>()
    private lateinit var popUpAdapter: PopUpAdapter
    private val popUpList: List<PlayerInfo> by lazy {
        viewModel.getPlayerList()
    }
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
        initComponents()
    }

    private fun initComponents() {
//        lifecycleScope.launch{
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                popUpList = viewModel.getPlayerList()
//            }
//        }
    }

    private fun initListeners() {
        binding.tvChooseTwoPlayers.setOnClickListener {
            showPlayerComparisonPopUp()
        }
    }

    private fun showPlayerComparisonPopUp() {
//        binding.clMessiVsCristiano.visibility = View.GONE
//        binding.clStats.visibility = View.VISIBLE

        popUpAdapter = PopUpAdapter(playerList = popUpList, onItemSelected = {println(1)})
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.item_popup, null)
        val popUpRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
        popUpRecyclerView.apply{
            adapter = popUpAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
    }
}