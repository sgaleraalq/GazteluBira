package com.sgalera.gaztelubira.ui.insert_game

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.databinding.ActivityInsertGameDetailBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.ui.insert_game.adapter.InsertGameAdapter

class InsertGameDetailActivity : AppCompatActivity(), PlayerAddListener {
    private lateinit var binding: ActivityInsertGameDetailBinding
    private lateinit var insertGameAdapter: InsertGameAdapter
    private lateinit var playerList: MutableList<PlayerInfo>
    private var benchPlayers = mutableListOf<PlayerInfo>()
    private var starterPlayers = mapOf(
        "defensive_mid_fielder" to "",
        "goal_keeper" to "",
        "left_back" to "",
        "left_centre_back" to "",
        "left_mid_fielder" to "",
        "left_striker" to "",
        "right_back" to "",
        "right_centre_back" to "",
        "right_mid_fielder" to "",
        "right_striker" to "",
        "striker" to ""
    )
    private val viewModel by viewModels<InsertGameViewModel>()

    override fun onPlayerAdded(player: PlayerInfo) {
        viewModel.state.value.add(player.name)
        powerSpinnerList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initComponents()
    }

    private fun initComponents() {
        playerList = viewModel.getPlayers()
        insertGameAdapter = InsertGameAdapter(benchPlayers, this)
        binding.rvBench.apply {
            adapter = insertGameAdapter
            layoutManager = LinearLayoutManager(
                this@InsertGameDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        powerSpinnerList()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAddBenchPlayer.setOnClickListener {
            if (benchPlayers.size == 6) {
                Toast.makeText(
                    this@InsertGameDetailActivity,
                    "You can't add more players to bench, please remove one first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val player = binding.psBenchPlayer.text.toString()
                insertGameAdapter.addPlayer(viewModel.convertToPlayerInfo(player))
                viewModel.state.value.remove(player)
                powerSpinnerList()
            }
        }
    }

    private fun powerSpinnerList(){
        binding.psBenchPlayer.clearSelectedItem()
        binding.psBenchPlayer.setItems(viewModel.state.value)
    }
}