package com.sgalera.gaztelubira.ui.player_compare

import android.app.AlertDialog
import android.graphics.Color.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.FragmentComparePlayersBinding
import com.sgalera.gaztelubira.domain.model.PlayerModel
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.player_compare.adapter.PlayerComparisonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCompareFragment : Fragment() {

    private var _binding: FragmentComparePlayersBinding? = null
    private val binding get() = _binding!!

    private val playerComparisonViewModel by viewModels<PlayerComparisonViewModel>()

    private lateinit var playerComparisonAdapter: PlayerComparisonAdapter
    private var playersList = mutableListOf<PlayerModel?>()

//    private val dialogPlayerList: List<PlayerInformation> = InformationList.players!!

    private lateinit var playerOneReference: DocumentReference
    private lateinit var playerOne: PlayerStats

    private lateinit var playerTwoReference: DocumentReference
    private lateinit var playerTwo: PlayerStats

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
        initPlayers()
        initAdapter()
        initListeners()
    }

    private fun initPlayers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playersList.collect { players ->
                    playersList = players.toMutableList()
                    playerComparisonAdapter.updateList(playersList)
                    Log.i("PlayerCompareFragment", playersList.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playerComparisonViewModel.playerOne.collect {
                    // TODO
                }
            }
        }
    }

    private fun initAdapter() {
        playerComparisonAdapter = PlayerComparisonAdapter(
            playersList = playersList,
            onPlayerSelected = {  }
        )
    }

    private fun initListeners() {
        binding.tvChooseTwoPlayers.setOnClickListener{ showPlayerComparison() }
    }

    private fun showPlayerComparison() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_popup, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialog.show()

        val playersRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
        playersRecyclerView.apply {
            adapter = playerComparisonAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun createPlayersDialog() {


//        selectPlayers(playerOne, playerTwo)
//        val popUpRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
//        val doneButtonPopUp = view.findViewById<CardView>(R.id.cvDone)
//        val popUpAdapter = PopUpAdapter(
//            playerList = popUpPlayerList,
//            selectedPlayers = mutableListOf(),
//            showDoneButton = {
//                doneButtonPopUp.visibility = View.VISIBLE
//            },
//            hideDoneButton = {
//                doneButtonPopUp.visibility = View.GONE
//            })
//
//        popUpAdapter.updateList(popUpPlayerList)
//        popUpRecyclerView.apply {
//            adapter = popUpAdapter
//            layoutManager = GridLayoutManager(requireContext(), 3)
//        }
//        doneButtonPopUp.setOnClickListener {
//            dialog.dismiss()
//            showPlayersInfo()
//        }
    }



//
//
//    private fun initListeners() {
//        binding.tvChooseTwoPlayers.setOnClickListener {
//            showPlayerComparisonPopUp(null, null)
//        }
//        binding.btnChooseTwoPlayers.setOnClickListener {
//            showPlayerComparisonPopUp(null, null)
//        }
//    }
//
//    private fun showPlayerComparisonPopUp(playerOne: PlayerInformation?, playerTwo: PlayerInformation?) {
//        val builder = AlertDialog.Builder(requireContext())
//        val inflater = LayoutInflater.from(requireContext())
//        val view = inflater.inflate(R.layout.item_popup, null)
//        builder.setView(view)
//        val dialog = builder.create()
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.show()
//
//        selectPlayers(playerOne, playerTwo)
//        val popUpRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewPlayers)
//        val doneButtonPopUp = view.findViewById<CardView>(R.id.cvDone)
//        val popUpAdapter = PopUpAdapter(
//            playerList = popUpPlayerList,
//            selectedPlayers = mutableListOf(),
//            showDoneButton = {
//                doneButtonPopUp.visibility = View.VISIBLE
//            },
//            hideDoneButton = {
//                doneButtonPopUp.visibility = View.GONE
//            })
//
//        popUpAdapter.updateList(popUpPlayerList)
//        popUpRecyclerView.apply {
//            adapter = popUpAdapter
//            layoutManager = GridLayoutManager(requireContext(), 3)
//        }
//        doneButtonPopUp.setOnClickListener {
//            dialog.dismiss()
//            showPlayersInfo()
//        }
//    }
//
//    private fun selectPlayers(playerOne: PlayerInformation?, playerTwo: PlayerInformation?): List<PlayerInformation> {
//        popUpPlayerList.forEach { player ->
//            if (player != playerOne && player != playerTwo) {
//                player.selected = false
//            }
//            if (player == playerOne || player == playerTwo) {
//                player.selected = true
//            }
//        }
//        return popUpPlayerList
//    }
//    private fun showPlayersInfo() {
//        binding.clMessiVsCristiano.visibility = View.GONE
//        binding.clStats.visibility = View.VISIBLE
//        fetchData()
//    }
//
//    private fun fetchData() {
//        playerOneReference = popUpPlayerList.find { it.selected }!!.stats!!
//        playerTwoReference = popUpPlayerList.findLast { it.selected }!!.stats!!
//
//        getPlayerOneStats()
//        getPlayerTwoStats()
//    }
//
//    private fun getPlayerOneStats() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.getPlayerStatsPlayerOne(playerOneReference)
//                viewModel.statePlayerOne.collect { state ->
//                    when (state) {
//                        is PlayerComparisonState.Loading -> loadingStatePlayerOne()
//                        is PlayerComparisonState.Success -> successStatePlayerOne(state.playerStats)
//                        is PlayerComparisonState.Error -> errorStatePlayerOne(state.error)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun loadingStatePlayerOne() {
//        binding.pbPlayerOne.visibility = View.VISIBLE
//    }
//
//    private fun errorStatePlayerOne(error: String) {
//        binding.pbPlayerOne.visibility = View.INVISIBLE
//        binding.pbPlayerTwo.visibility = View.INVISIBLE
//        Toast.makeText(requireContext(), "Ha ocurrido un error $error", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun successStatePlayerOne(playerStats: PlayerStatsModel) {
//        binding.pbPlayerOne.visibility = View.INVISIBLE
////        playerOne = playerStats
//        isPlayerOneLoaded = true
//        if (isPlayerTwoLoaded){
//            initComponents()
//        }
//    }
//
//    private fun getPlayerTwoStats() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.getPlayerStatsPlayerTwo(playerTwoReference)
//                viewModel.statePlayerTwo.collect { state ->
//                    when (state) {
//                        is PlayerComparisonState.Loading -> loadingStatePlayerTwo()
//                        is PlayerComparisonState.Success -> successStatePlayerTwo(state.playerStats)
//                        is PlayerComparisonState.Error -> errorStatePlayerTwo(state.error)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun loadingStatePlayerTwo() {
//        binding.pbPlayerTwo.visibility = View.VISIBLE
//    }
//
//    private fun errorStatePlayerTwo(error: String) {
//        binding.pbPlayerOne.visibility = View.INVISIBLE
//        binding.pbPlayerTwo.visibility = View.INVISIBLE
//        Toast.makeText(requireContext(), "Ha ocurrido un error $error", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun successStatePlayerTwo(playerStats: PlayerStatsModel) {
//        binding.pbPlayerTwo.visibility = View.INVISIBLE
////        playerTwo = playerStats
//        isPlayerTwoLoaded = true
//        if (isPlayerOneLoaded){
//            initComponents()
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    private fun initComponents() {
//        // Main ppal components player one
//        binding.tvPlayerOneName.text = playerOne.information!!.name
//        Glide.with(requireContext()).load(playerOne.information!!.img).into(binding.ivPlayerOne)
//        binding.tvDorsalPlayerOne.text = playerOne.information!!.dorsal.toString()
//        binding.tvPositionPlayerOne.text = playerOne.position
//        binding.tvParticipationPlayerOne.text = "${playerOne.percentage.toString()} %"
//        binding.tvGoalsPlayerOne.text = playerOne.goals.toString()
//        binding.tvAssistsPlayerOne.text = playerOne.assists.toString()
//        binding.tvPenaltiesPlayerOne.text = playerOne.penalties.toString()
//        binding.tvCleanSheetPlayerOne.text = playerOne.cleanSheet.toString()
//        binding.tvGamesPlayedPlayerOne.text = playerOne.gamesPlayed.toString()
//
//        // Main ppal components player two
//        binding.tvPlayerTwoName.text = playerTwo.information!!.name
//        Glide.with(requireContext()).load(playerTwo.information!!.img).into(binding.ivPlayerTwo)
//        binding.tvDorsalPlayerTwo.text = playerTwo.information!!.dorsal.toString()
//        binding.tvPositionPlayerTwo.text = playerTwo.position
//        binding.tvParticipationPlayerTwo.text = "${playerTwo.percentage.toString()} %"
//        binding.tvGoalsPlayerTwo.text = playerTwo.goals.toString()
//        binding.tvAssistsPlayerTwo.text = playerTwo.assists.toString()
//        binding.tvPenaltiesPlayerTwo.text = playerTwo.penalties.toString()
//        binding.tvCleanSheetPlayerTwo.text = playerTwo.cleanSheet.toString()
//        binding.tvGamesPlayedPlayerTwo.text = playerTwo.gamesPlayed.toString()
//
//        initProgressViews()
//    }
//
//    private fun initProgressViews() {
//        val maxValue = getMaxValue()
//        // Player One
//        binding.pvGoalsPlayerOne.max = maxValue.toFloat()
//        binding.pvGoalsPlayerOne.progress = playerOne.goals.toFloat()
//        binding.pvAssistsPlayerOne.max = maxValue.toFloat()
//        binding.pvAssistsPlayerOne.progress = playerOne.assists.toFloat()
//        binding.pvPenaltiesPlayerOne.max = maxValue.toFloat()
//        binding.pvPenaltiesPlayerOne.progress = playerOne.penalties.toFloat()
//        binding.pvCleanSheetPlayerOne.max = maxValue.toFloat()
//        binding.pvCleanSheetPlayerOne.progress = playerOne.cleanSheet.toFloat()
//        binding.pvGamesPlayedPlayerOne.max = maxValue.toFloat()
//        binding.pvGamesPlayedPlayerOne.progress = playerOne.gamesPlayed.toFloat()
//
//        // Player Two
//        binding.pvGoalsPlayerTwo.max = maxValue.toFloat()
//        binding.pvGoalsPlayerTwo.progress = playerTwo.goals.toFloat()
//        binding.pvAssistsPlayerTwo.max = maxValue.toFloat()
//        binding.pvAssistsPlayerTwo.progress = playerTwo.assists.toFloat()
//        binding.pvPenaltiesPlayerTwo.max = maxValue.toFloat()
//        binding.pvPenaltiesPlayerTwo.progress = playerTwo.penalties.toFloat()
//        binding.pvCleanSheetPlayerTwo.max = maxValue.toFloat()
//        binding.pvCleanSheetPlayerTwo.progress = playerTwo.cleanSheet.toFloat()
//        binding.pvGamesPlayedPlayerTwo.max = maxValue.toFloat()
//        binding.pvGamesPlayedPlayerTwo.progress = playerTwo.gamesPlayed.toFloat()
//    }
//
//
//    private fun getMaxValue(): Int {
//        return maxOf(playerOne.goals, playerTwo.goals,
//            playerOne.assists, playerTwo.assists,
//            playerOne.gamesPlayed, playerTwo.gamesPlayed,
//            playerOne.penalties, playerTwo.penalties,
//            playerOne.cleanSheet, playerTwo.cleanSheet)
//    }
}