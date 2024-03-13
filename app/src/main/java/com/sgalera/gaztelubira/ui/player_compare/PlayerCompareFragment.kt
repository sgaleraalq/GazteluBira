package com.sgalera.gaztelubira.ui.player_compare

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.ui.player_compare.adapter.PopUpAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlayerComparisonViewModel>()
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
    }


    private fun initListeners() {
        binding.tvChooseTwoPlayers.setOnClickListener {
            showPlayerComparisonPopUp(null, null)
        }
        binding.btnChooseTwoPlayers.setOnClickListener {
            showPlayerComparisonPopUp(null, null)
        }

        // Change only one player
        binding.tvPlayerOneName.setOnClickListener {
            showPlayerComparisonPopUp(null, viewModel.mapPlayer(binding.tvPlayerTwoName.text))
        }
        binding.tvPlayerTwoName.setOnClickListener {
            showPlayerComparisonPopUp(viewModel.mapPlayer(binding.tvPlayerOneName.text), null)
        }
    }

    private fun showPlayerComparisonPopUp(playerOne: PlayerInfo?, playerTwo: PlayerInfo?) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.item_popup, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        selectPlayers(playerOne, playerTwo)
        val doneButtonPopUp = view.findViewById<CardView>(R.id.cvDone)
        val popUpAdapter = PopUpAdapter(
            playerList = popUpList,
            selectedPlayers = mutableListOf(),
            showDoneButton = {
                doneButtonPopUp.visibility = View.VISIBLE
            },
            hideDoneButton = {
                doneButtonPopUp.visibility = View.GONE
            })

        popUpAdapter.updateList(popUpList)
        val popUpRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
        popUpRecyclerView.apply {
            adapter = popUpAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        doneButtonPopUp.setOnClickListener {
            dialog.dismiss()
            showPlayersInfo()
        }
    }

    private fun selectPlayers(playerOne: PlayerInfo?, playerTwo: PlayerInfo?): List<PlayerInfo> {
        popUpList.forEach { player ->
            if (player != playerOne && player != playerTwo) {
                player.selected = false
            }
            if (player == playerOne || player == playerTwo) {
                player.selected = true
            }
        }
        return popUpList
    }
    private fun showPlayersInfo() {
        binding.clMessiVsCristiano.visibility = View.GONE
        binding.clStats.visibility = View.VISIBLE
        fetchData()
    }

    private fun fetchData() {
        getPlayerInfo()
        initComponents()
    }

    private fun getPlayerInfo() {
        println(1)
    }

    private fun initComponents() {
        println(1)
    }
}