package com.sgalera.gaztelubira.ui.player_compare

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.player_compare.adapter.PopUpAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PlayerComparisonViewModel>()
    private val popUpList: List<PlayerInfo> by lazy {
        viewModel.getPlayerList()
    }

    private lateinit var playerOne: PlayerStats
    private lateinit var playerTwo: PlayerStats
    private lateinit var playerOneName: String
    private lateinit var playerTwoName: String

    private var isPlayerOneLoaded = false
    private var isPlayerTwoLoaded = false

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

        // Change only one playerStats
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
        val popUpRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
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
        getAllStats()
    }

    private fun getPlayerInfo() {
        playerOneName = ""
        playerTwoName = ""
        for (element in popUpList){
            if (element.selected){
                if (playerOneName.isNotEmpty()){
                    playerTwoName = element.name
                } else {
                    playerOneName = element.name
                }
            }
        }
    }

    private fun getAllStats() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPlayerStats(playerOneName, 1)
                viewModel.getPlayerStats(playerTwoName, 2)
                viewModel.state.collect { state ->
                    when (state) {
                        is PlayerComparisonState.Loading -> loadingState()
                        is PlayerComparisonState.Success -> successState(state.playerStats, state.id)
                        is PlayerComparisonState.Error -> errorState(state.error)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        binding.pbPlayerOne.visibility = View.VISIBLE
        binding.pbPlayerTwo.visibility = View.VISIBLE
    }

    private fun errorState(error: String) {
        binding.pbPlayerOne.visibility = View.INVISIBLE
        binding.pbPlayerTwo.visibility = View.INVISIBLE
        Toast.makeText(requireContext(), "Ha ocurrido un error $error", Toast.LENGTH_SHORT).show()

    }

    private fun successState(playerStats: PlayerStats, id: Int) {
        binding.pbPlayerOne.visibility = View.INVISIBLE
        binding.pbPlayerTwo.visibility = View.INVISIBLE

        if (id == 1){
            playerOne = playerStats
            isPlayerOneLoaded = true
        } else if (id == 2) {
            playerTwo = playerStats
            isPlayerTwoLoaded = true
        }
        if (isPlayerOneLoaded && isPlayerTwoLoaded){
            initPercentages()
            initComponents()
        }
    }


    private fun initPercentages() {
        playerOne.percentage = getPercentage(playerOne)
        playerTwo.percentage = getPercentage(playerTwo)
    }

    @SuppressLint("SetTextI18n")
    private fun initComponents() {
        // Main ppal components player one
        binding.tvPlayerOneName.text = playerOne.name.name
        binding.ivPlayerOne.setImageResource(playerOne.name.img)
        binding.tvDorsalPlayerOne.text = playerOne.name.dorsal.toString()
        binding.tvPositionPlayerOne.text = playerOne.position
        binding.tvParticipationPlayerOne.text = "${playerOne.percentage.toString()} %"
        binding.tvGoalsPlayerOne.text = playerOne.goals.toString()
        binding.tvAssistsPlayerOne.text = playerOne.assists.toString()
        binding.tvBigMistakesPlayerOne.text = playerOne.bigMistakes.toString()
        binding.tvCleanSheetPlayerOne.text = playerOne.cleanSheet.toString()
        binding.tvGamesPlayedPlayerOne.text = playerOne.gamesPlayed.toString()

        // Main ppal components player two
        binding.tvPlayerTwoName.text = playerTwo.name.name
        binding.ivPlayerTwo.setImageResource(playerTwo.name.img)
        binding.tvDorsalPlayerTwo.text = playerTwo.name.dorsal.toString()
        binding.tvPositionPlayerTwo.text = playerTwo.position
        binding.tvParticipationPlayerTwo.text = "${playerTwo.percentage.toString()} %"
        binding.tvGoalsPlayerTwo.text = playerTwo.goals.toString()
        binding.tvAssistsPlayerTwo.text = playerTwo.assists.toString()
        binding.tvBigMistakesPlayerTwo.text = playerTwo.bigMistakes.toString()
        binding.tvCleanSheetPlayerTwo.text = playerTwo.cleanSheet.toString()
        binding.tvGamesPlayedPlayerTwo.text = playerTwo.gamesPlayed.toString()

        initProgressViews()
    }


    private fun getPercentage(player: PlayerStats): String {
        val total = player.goals + player.assists + player.cleanSheet - player.bigMistakes
        val gamesPlayed = player.gamesPlayed.toFloat()
        return if (gamesPlayed != 0f) {
            val percentage = (total.toFloat() / gamesPlayed)
            DecimalFormat("#.##").format(percentage)
        } else {
            "0"
        }
    }

    private fun initProgressViews() {
        val maxValue = getMaxValue()
        // Player One
        binding.pvGoalsPlayerOne.max = maxValue.toFloat()
        binding.pvGoalsPlayerOne.progress = playerOne.goals.toFloat()
        binding.pvAssistsPlayerOne.max = maxValue.toFloat()
        binding.pvAssistsPlayerOne.progress = playerOne.assists.toFloat()
        binding.pvBigMistakesPlayerOne.max = maxValue.toFloat()
        binding.pvBigMistakesPlayerOne.progress = playerOne.bigMistakes.toFloat()
        binding.pvCleanSheetPlayerOne.max = maxValue.toFloat()
        binding.pvCleanSheetPlayerOne.progress = playerOne.cleanSheet.toFloat()
        binding.pvGamesPlayedPlayerOne.max = maxValue.toFloat()
        binding.pvGamesPlayedPlayerOne.progress = playerOne.gamesPlayed.toFloat()

        // Player Two
        binding.pvGoalsPlayerTwo.max = maxValue.toFloat()
        binding.pvGoalsPlayerTwo.progress = playerTwo.goals.toFloat()
        binding.pvAssistsPlayerTwo.max = maxValue.toFloat()
        binding.pvAssistsPlayerTwo.progress = playerTwo.assists.toFloat()
        binding.pvBigMistakesPlayerTwo.max = maxValue.toFloat()
        binding.pvBigMistakesPlayerTwo.progress = playerTwo.bigMistakes.toFloat()
        binding.pvCleanSheetPlayerTwo.max = maxValue.toFloat()
        binding.pvCleanSheetPlayerTwo.progress = playerTwo.cleanSheet.toFloat()
        binding.pvGamesPlayedPlayerTwo.max = maxValue.toFloat()
        binding.pvGamesPlayedPlayerTwo.progress = playerTwo.gamesPlayed.toFloat()
    }


    private fun getMaxValue(): Int {
        return maxOf(playerOne.goals, playerTwo.goals,
            playerOne.assists, playerTwo.assists,
            playerOne.gamesPlayed, playerTwo.gamesPlayed,
            playerOne.bigMistakes, playerTwo.bigMistakes,
            playerOne.cleanSheet, playerTwo.cleanSheet)
    }
}