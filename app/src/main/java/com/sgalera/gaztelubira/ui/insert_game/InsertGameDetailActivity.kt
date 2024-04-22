package com.sgalera.gaztelubira.ui.insert_game

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameDetailBinding
import com.sgalera.gaztelubira.domain.model.MappingUtils.mapTeam
import com.sgalera.gaztelubira.domain.model.players.PlayerInfo
import com.sgalera.gaztelubira.ui.insert_game.adapter.InsertGameAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertGameDetailActivity : AppCompatActivity(), PlayerAddListener {
    private lateinit var binding: ActivityInsertGameDetailBinding
    private lateinit var insertGameAdapter: InsertGameAdapter
    private lateinit var playerList: MutableList<PlayerInfo>
    private var benchPlayers = mutableListOf<PlayerInfo>()
    private var starterPlayers = mutableMapOf(
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

    private var home: Int = 0
    private var away: Int = 0
    private var homeGoals: Int = 0
    private var awayGoals: Int = 0

    override fun onPlayerAdded(player: PlayerInfo) {
        viewModel.state.value.add(player.name)
        powerSpinnerBenchList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        home = intent.getIntExtra("homeTeam", 0)
        away = intent.getIntExtra("awayTeam", 0)
        homeGoals = intent.getIntExtra("homeGoals", 0)
        awayGoals = intent.getIntExtra("awayGoals", 0)
        initUI()
        initComponents()
        initListeners()
    }

    private fun initUI() {
        if (homeGoals > 0) {
            binding.tvGoals.visibility = View.VISIBLE
            binding.llGoals.visibility = View.VISIBLE
            binding.dividerGoals.visibility = View.VISIBLE
            setGoals()
            // TODO missing assists and penalties
        }
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
        powerSpinnerBenchList()
        initResult()
    }

    private fun initResult() {
        println("${getString(home)} ${getString(away)} $homeGoals $awayGoals")
        val homeTeam = mapTeam(getString(home))
        val awayTeam = mapTeam(getString(away))
        binding.ivLocalTeam.setImageResource(homeTeam.img)
        binding.tvLocalTeam.text = getString(homeTeam.name)
        binding.tvLocalGoals.text = homeGoals.toString()
        binding.ivAwayTeam.setImageResource(awayTeam.img)
        binding.tvAwayTeam.text = getString(awayTeam.name)
        binding.tvAwayGoals.text = awayGoals.toString()
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnAddBenchPlayer.setOnClickListener {
            insertBenchPlayer()
        }

        initStartersListeners()
        binding.btnInsertGame.setOnClickListener {
            insertGoalsAssists()
        }
    }

    private fun setGoals() {
        for (i in 0 until homeGoals) {
            val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                .inflate(R.layout.item_add_goal_or_assist, binding.llGoals, false)
            itemLayout.setOnClickListener { showStatsPopUp("Goal", itemLayout) }
            binding.llGoals.addView(itemLayout)

        }
    }


    private fun initStartersListeners() {
        binding.ivGoalKeeper.root.setOnClickListener {
            showInsertPlayerDialog("goal_keeper")
        }
        binding.ivLeftBack.root.setOnClickListener {
            showInsertPlayerDialog("left_back")
        }
        binding.ivLeftCentreBack.root.setOnClickListener {
            showInsertPlayerDialog("left_centre_back")
        }
        binding.ivRightCentreBack.root.setOnClickListener {
            showInsertPlayerDialog("right_centre_back")
        }
        binding.ivRightBack.root.setOnClickListener {
            showInsertPlayerDialog("right_back")
        }
        binding.ivLeftMidFielder.root.setOnClickListener {
            showInsertPlayerDialog("left_mid_fielder")
        }
        binding.ivDefensiveMidFielder.root.setOnClickListener {
            showInsertPlayerDialog("defensive_mid_fielder")
        }
        binding.ivRightMidFielder.root.setOnClickListener {
            showInsertPlayerDialog("right_mid_fielder")
        }
        binding.ivLeftStriker.root.setOnClickListener {
            showInsertPlayerDialog("left_striker")
        }
        binding.ivRightStriker.root.setOnClickListener {
            showInsertPlayerDialog("right_striker")
        }
        binding.ivStriker.root.setOnClickListener {
            showInsertPlayerDialog("striker")
        }
    }

    private fun insertBenchPlayer() {
        if (binding.psBenchPlayer.text == "") {
            Toast.makeText(
                this@InsertGameDetailActivity,
                "Please select a player",
                Toast.LENGTH_SHORT
            ).show()
        } else if (benchPlayers.size == 6) {
            Toast.makeText(
                this@InsertGameDetailActivity,
                "You can't add more players to bench, please remove one first",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val player = binding.psBenchPlayer.text.toString()
            insertGameAdapter.addPlayer(viewModel.convertToPlayerInfo(player))
            viewModel.state.value.remove(player)
            playerList.remove(viewModel.convertToPlayerInfo(player))
            powerSpinnerBenchList()
        }
    }


    private fun showInsertPlayerDialog(position: String) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_popup_insert_starter, null)
        builder.setView(view)
        val dialogView = builder.create()
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.show()


        dialogView.findViewById<TextView>(R.id.tvStarterPosition).text = popUpText(position)
        dialogView.findViewById<LinearLayout>(R.id.llStarterPlayers).apply {
            removeAllViews()
            playerList.forEach { player ->
                val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                    .inflate(R.layout.item_insert_starters, this, false)
                itemLayout.findViewById<TextView>(R.id.tvStarterName).text = player.name
                itemLayout.findViewById<ConstraintLayout>(R.id.parentAddStarter)
                    .setOnClickListener {
                        if (starterPlayers[position] != "") {
                            playerList.add(viewModel.convertToPlayerInfo(starterPlayers[position]!!))
                        }
                        playerList.remove(player)
                        setStarterPlayerInfo(position, player.name)
                        dialogView.dismiss()
                    }
                addView(itemLayout)
            }
        }
    }

    private fun setStarterPlayerInfo(position: String, name: String) {
        viewModel.state.value.remove(name)
        powerSpinnerBenchList()
        val playerInfo = viewModel.convertToPlayerInfo(name)
        when (position) {
            "goal_keeper" -> {
                binding.tvGoalKeeper.text = playerInfo.name
                binding.ivGoalKeeper.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "left_back" -> {
                binding.tvLeftBack.text = playerInfo.name
                binding.ivLeftBack.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "left_centre_back" -> {
                binding.tvLeftCentreBack.text = playerInfo.name
                binding.ivLeftCentreBack.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "right_centre_back" -> {
                binding.tvRightCentreBack.text = playerInfo.name
                binding.ivRightCentreBack.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "right_back" -> {
                binding.tvRightBack.text = playerInfo.name
                binding.ivRightBack.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "left_mid_fielder" -> {
                binding.tvLeftMidFielder.text = playerInfo.name
                binding.ivLeftMidFielder.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "defensive_mid_fielder" -> {
                binding.tvDefensiveMidFielder.text = playerInfo.name
                binding.ivDefensiveMidFielder.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "right_mid_fielder" -> {
                binding.tvRightMidFielder.text = playerInfo.name
                binding.ivRightMidFielder.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "left_striker" -> {
                binding.tvLeftStriker.text = playerInfo.name
                binding.ivLeftStriker.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "right_striker" -> {
                binding.tvRightStriker.text = playerInfo.name
                binding.ivRightStriker.dorsalTextView.text = playerInfo.dorsal.toString()
            }

            "striker" -> {
                binding.tvStriker.text = playerInfo.name
                binding.ivStriker.dorsalTextView.text = playerInfo.dorsal.toString()
            }
        }
        starterPlayers[position] = name
    }

    private fun powerSpinnerBenchList() {
        binding.psBenchPlayer.clearSelectedItem()
        binding.psBenchPlayer.setItems(viewModel.state.value)
    }

    private fun popUpText(position: String): String {
        return when (position) {
            "goal_keeper" -> "Select Goal Keeper"
            "left_back" -> "Select Left Back"
            "left_centre_back" -> "Select Left Centre Back"
            "right_centre_back" -> "Select Right Centre Back"
            "right_back" -> "Select Right Back"
            "left_mid_fielder" -> "Select Left Mid Fielder"
            "defensive_mid_fielder" -> "Select Defensive Mid Fielder"
            "right_mid_fielder" -> "Select Right Mid Fielder"
            "left_striker" -> "Select Left Striker"
            "right_striker" -> "Select Right Striker"
            "striker" -> "Select Striker"
            else -> ""
        }
    }

    private fun insertGoalsAssists() {
//        if (checkTeams() && checkGoals() && checkPlayers()) {
//            showGoalsAssistsPopUp()
//        }
        showGoalsAssistsPopUp()
    }


    private fun checkTeams(): Boolean {
        return if (binding.tvLocalTeam.text == binding.tvAwayTeam.text) {
            Toast.makeText(this, "Please select two different teams", Toast.LENGTH_SHORT).show()
            false
        } else if (binding.tvLocalTeam.text != "Gaztelu Bira" && binding.tvAwayTeam.text != "Gaztelu Bira") {
            Toast.makeText(this, "One of the teams must be Gaztelu Bira", Toast.LENGTH_SHORT).show()
            false
        } else if (binding.ivLocalTeam.visibility == View.INVISIBLE || binding.ivAwayTeam.visibility == View.INVISIBLE) {
            Toast.makeText(this, "Please select both teams", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }


    private fun checkPlayers(): Boolean {
        return if (starterPlayers.containsValue("")) {
            Toast.makeText(this, "Please select all the starters", Toast.LENGTH_SHORT).show()
            false
        } else if (starterPlayers.containsValue(binding.psBenchPlayer.text.toString())) {
            Toast.makeText(
                this,
                "Player can't be in the bench and starter at the same time",
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            true
        }
    }

    private fun showGoalsAssistsPopUp() {
        // TODO insert game
        Toast.makeText(this, "Game inserted", Toast.LENGTH_SHORT).show()
    }

    private fun insertGame() {
        println("Game inserted")
    }

    private fun showStatsPopUp(stat: String, playerStat: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_popup_insert_starter, null)
        view.findViewById<TextView>(R.id.tvStarterPosition).text = stat

        view.findViewById<LinearLayout>(R.id.llStarterPlayers).apply {
            removeAllViews()
            playerList.forEach { player ->
                val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                    .inflate(R.layout.item_insert_starters, this, false)
                itemLayout.findViewById<TextView>(R.id.tvStarterName).text = player.name
                itemLayout.findViewById<ConstraintLayout>(R.id.parentAddStarter)
                    .setOnClickListener {
                        playerStat.findViewById<ImageView>(R.id.ivGoalPlayer).setImageResource(player.img)
                        playerStat.findViewById<TextView>(R.id.tvPlayerName).text = player.name
                        builder.create().dismiss()
                    }
                addView(itemLayout)
            }
            builder.setView(view)
        }
        val dialogView = builder.create()
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.show()
    }
}