package com.sgalera.gaztelubira.ui.insert_game

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameDetailBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.insert_game.adapter.InsertGameAdapter

class InsertGameDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertGameDetailBinding
    private lateinit var insertGameAdapter: InsertGameAdapter
    private lateinit var playerList: List<PlayerInfo>
    private var benchPlayers = mutableListOf<PlayerInfo>()
    private val viewModel by viewModels<InsertGameViewModel>()

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
        insertGameAdapter = InsertGameAdapter(benchPlayers)
        binding.rvBench.apply{
            adapter = insertGameAdapter
            layoutManager = LinearLayoutManager(this@InsertGameDetailActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAddBenchPlayer.setOnClickListener {
            // TODO change this to real player
            println(1)
            insertGameAdapter.addPlayer(PlayerInfo.Pedro)
        }
    }
}