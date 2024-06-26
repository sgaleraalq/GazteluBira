package com.sgalera.gaztelubira.ui.insert_game

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameDetailBinding
import com.sgalera.gaztelubira.domain.model.players.PlayerInformation
import com.sgalera.gaztelubira.domain.model.players.PlayerStats
import com.sgalera.gaztelubira.ui.home.MainActivity
import com.sgalera.gaztelubira.ui.insert_game.adapter.InsertGameAdapter
import com.sgalera.gaztelubira.ui.stats.StatsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InsertGameDetailActivity : AppCompatActivity(), PlayerAddListener {
    private lateinit var binding: ActivityInsertGameDetailBinding
    private lateinit var insertGameAdapter: InsertGameAdapter
    private val viewModel by viewModels<InsertGameViewModel>()

    private lateinit var playerList: MutableList<PlayerInformation>
    private var goalList = mutableListOf<String>()
    private var assistList = mutableListOf<String>()
    private var penaltyList = mutableListOf<String>()
    private var cleanSheetList = mutableListOf<String>()
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
    private var benchPlayers = mutableListOf<PlayerInformation>()


    private var home: String = ""
    private var away: String = ""
    private var homeGoals: Int = 0
    private var awayGoals: Int = 0
    private var match: String = ""
    private var journey: Int = 0
    private var id: Int = 0

    private var dialog: AlertDialog? = null

    override fun updateBenchPowerSpinner() {
        powerSpinnerBenchList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        home            = intent.getStringExtra("homeTeam")!!
        away            = intent.getStringExtra("awayTeam")!!
        homeGoals       = intent.getIntExtra("homeGoals", 0)
        awayGoals       = intent.getIntExtra("awayGoals", 0)
        match           = intent.getStringExtra("match")!!
        journey         = intent.getIntExtra("journey", 0)
        id              = intent.getIntExtra("id", 0)

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

            binding.tvAssists.visibility = View.VISIBLE
            binding.llAssists.visibility = View.VISIBLE
            binding.dividerAssists.visibility = View.VISIBLE
            setAssists()

            binding.tvPenalties.visibility = View.VISIBLE
            binding.llPenalties.visibility = View.VISIBLE
            binding.dividerPenalties.visibility = View.VISIBLE
            setPenalties()
        }
        if (awayGoals == 0 ){
            binding.tvCleanSheet.visibility = View.VISIBLE
            binding.llCleanSheet.visibility = View.VISIBLE
            binding.dividerCleanSheet.visibility = View.VISIBLE
            setCleanSheet()
        }
    }

    // Sets layouts if necessary
    private fun setGoals() {
        for (i in 0 until homeGoals) {
            val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                .inflate(R.layout.item_add_goal_or_assist, binding.llGoals, false)
            itemLayout.setOnClickListener { showStatsPopUp("Goal", itemLayout) }
            binding.llGoals.addView(itemLayout)
        }
    }

    private fun setAssists() {
        val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
            .inflate(R.layout.item_add_goal_or_assist, binding.llAssists, false)
        itemLayout.setOnClickListener{ showStatsPopUp("Assist", itemLayout) }
        binding.llAssists.addView(itemLayout)
    }

    private fun setPenalties() {
        val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
            .inflate(R.layout.item_add_goal_or_assist, binding.llPenalties, false)
        itemLayout.setOnClickListener{ showStatsPopUp("Penalty", itemLayout) }
        binding.llPenalties.addView(itemLayout)
    }

    private fun setCleanSheet() {
        for (i in 0 until 4){
            val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                .inflate(R.layout.item_add_goal_or_assist, binding.llCleanSheet, false)
            itemLayout.setOnClickListener { showStatsPopUp("Clean Sheet", itemLayout) }
            binding.llCleanSheet.addView(itemLayout)
        }
    }

    private fun initComponents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getPlayersInformation()
                viewModel.statePlayers.collect {
                    when (it) {
                        is InsertGameState.Loading -> loadingStatePlayersInformation()
                        is InsertGameState.SuccessPlayers -> successGetPlayersInformation(it.players)
                        else -> errorStatePlayersInformation()
                    }
                }
            }
        }
        insertGameAdapter = InsertGameAdapter(benchPlayers, this)
        binding.rvBench.apply {
            adapter = insertGameAdapter
            layoutManager = LinearLayoutManager(
                this@InsertGameDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        initResult()
    }

    private fun initResult() {
        lifecycleScope.launch {
            val homeTeam = viewModel.getTeamInformation(home)
            val awayTeam = viewModel.getTeamInformation(away)

            binding.tvLocalTeam.text = homeTeam?.name
            Glide.with(this@InsertGameDetailActivity).load(homeTeam?.img).into(binding.ivLocalTeam)

            binding.tvAwayTeam.text = awayTeam?.name
            Glide.with(this@InsertGameDetailActivity).load(awayTeam?.img).into(binding.ivAwayTeam)
        }

        binding.tvLocalGoals.text = homeGoals.toString()
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
            insertGame()
        }
        binding.btnDeleteGame.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
            insertGameAdapter.addPlayer(playerList.find { it.name == player }!!)
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
            playerList
                .filter { it.name !in starterPlayers.values }
                .filter { it -> it.name !in benchPlayers.map { it.name } }
                .sortedBy { it.name }.forEach { player ->
                val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                    .inflate(R.layout.item_insert_starters, this, false)
                itemLayout.findViewById<TextView>(R.id.tvStarterName).text = player.name
                itemLayout.findViewById<ConstraintLayout>(R.id.parentAddStarter)
                    .setOnClickListener {
                        setStarterPlayerInfo(position, player)
                        dialogView.dismiss()
                    }
                addView(itemLayout)
            }
        }
    }

    private fun setStarterPlayerInfo(position: String, player: PlayerInformation) {
        when (position) {
            "goal_keeper" -> {
                binding.tvGoalKeeper.text = player.name
                binding.ivGoalKeeper.dorsalTextView.text = player.dorsal.toString()
            }

            "left_back" -> {
                binding.tvLeftBack.text = player.name
                binding.ivLeftBack.dorsalTextView.text = player.dorsal.toString()
            }

            "left_centre_back" -> {
                binding.tvLeftCentreBack.text = player.name
                binding.ivLeftCentreBack.dorsalTextView.text = player.dorsal.toString()
            }

            "right_centre_back" -> {
                binding.tvRightCentreBack.text = player.name
                binding.ivRightCentreBack.dorsalTextView.text = player.dorsal.toString()
            }

            "right_back" -> {
                binding.tvRightBack.text = player.name
                binding.ivRightBack.dorsalTextView.text = player.dorsal.toString()
            }

            "left_mid_fielder" -> {
                binding.tvLeftMidFielder.text = player.name
                binding.ivLeftMidFielder.dorsalTextView.text = player.dorsal.toString()
            }

            "defensive_mid_fielder" -> {
                binding.tvDefensiveMidFielder.text = player.name
                binding.ivDefensiveMidFielder.dorsalTextView.text = player.dorsal.toString()
            }

            "right_mid_fielder" -> {
                binding.tvRightMidFielder.text = player.name
                binding.ivRightMidFielder.dorsalTextView.text = player.dorsal.toString()
            }

            "left_striker" -> {
                binding.tvLeftStriker.text = player.name
                binding.ivLeftStriker.dorsalTextView.text = player.dorsal.toString()
            }

            "right_striker" -> {
                binding.tvRightStriker.text = player.name
                binding.ivRightStriker.dorsalTextView.text = player.dorsal.toString()
            }

            "striker" -> {
                binding.tvStriker.text = player.name
                binding.ivStriker.dorsalTextView.text = player.dorsal.toString()
            }
        }
        starterPlayers[position] = player.name
        powerSpinnerBenchList()
    }

    private fun powerSpinnerBenchList() {
        binding.psBenchPlayer.clearSelectedItem()
        val benchList = playerList
            .filter { it.name !in starterPlayers.values }
            .filter { it -> it.name !in benchPlayers.map { it.name } }
            .sortedBy { it.name }
            .map { it.name }

        binding.psBenchPlayer.setItems(benchList)
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

    private fun showStatsPopUp(stat: String, playerStat: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_popup_insert_starter, null)

        // Configura la vista antes de crear el diálogo
        view.findViewById<TextView>(R.id.tvStarterPosition).text = stat
        view.findViewById<LinearLayout>(R.id.llStarterPlayers).apply {
            removeAllViews()
            var statsList = playerList
            if (stat == "Clean Sheet"){ statsList = statsList.filter { it.name !in cleanSheetList }.toMutableList() }
            statsList.sortedBy { it.name }.forEach { player ->
                val playerName = playerStat.findViewById<TextView>(R.id.tvPlayerName)
                val playerImage = playerStat.findViewById<ImageView>(R.id.ivGoalPlayer)
                val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
                    .inflate(R.layout.item_insert_starters, this, false)
                itemLayout.findViewById<TextView>(R.id.tvStarterName).text = player.name
                itemLayout.findViewById<ConstraintLayout>(R.id.parentAddStarter)
                    .setOnClickListener {
                        when (stat) {
                            "Goal" -> {
                                if (playerImage.visibility == View.VISIBLE) {
                                    val index = goalList.indexOf(playerName.text.toString())
                                    goalList.removeAt(index)
                                    goalList.add(player.name)
                                } else {
                                    goalList.add(player.name)
                                }
                            }
                            "Assist" -> {
                                if (playerImage.visibility == View.VISIBLE) {
                                    val index = assistList.indexOf(playerName.text.toString())
                                    assistList.removeAt(index)
                                    assistList.add(player.name)
                                } else {
                                    assistList.add(player.name)
                                }
                            }
                            "Penalty" -> {
                                if (playerImage.visibility == View.VISIBLE) {
                                    val index = penaltyList.indexOf(playerName.text.toString())
                                    penaltyList.removeAt(index)
                                    penaltyList.add(player.name)
                                } else {
                                    penaltyList.add(player.name)
                                }
                            }
                            "Clean Sheet" -> {
                                if (playerImage.visibility == View.VISIBLE) {
                                    val index = cleanSheetList.indexOf(playerName.text.toString())
                                    cleanSheetList.removeAt(index)
                                    cleanSheetList.add(player.name)
                                } else {
                                    cleanSheetList.add(player.name)
                                }
                            }
                        }
                        Glide.with(this@InsertGameDetailActivity).load(player.img).into(playerImage)
                        playerImage.visibility = View.VISIBLE
                        playerName.text = player.name
                        playerName.visibility = View.VISIBLE
                        playerStat.findViewById<ImageView>(R.id.ivPlus).visibility = View.INVISIBLE

                        // Dismiss the dialog
                        dialog?.dismiss()
                        if (stat == "Assist" || stat == "Penalty" || stat == "Clean Sheet"){
                            checkStatsLayout(stat)
                        }
                    }
                addView(itemLayout)
            }
        }

        // Crea el diálogo después de configurar la vista
        dialog = builder.setView(view).create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    private fun checkStatsLayout(stat: String) {
        when (stat) {
            "Assist" -> {
                if (binding.llAssists.childCount < homeGoals) {
                    addLayout(stat, binding.llAssists)
                }
            }
            "Penalty" -> {
                if (binding.llPenalties.childCount < homeGoals) {
                    addLayout(stat, binding.llPenalties)
                }
            }
            "Clean Sheet" -> {
                if (cleanSheetList.size >= 4 && cleanSheetList.size == binding.llCleanSheet.childCount) {
                    addLayout(stat, binding.llCleanSheet)
                }
            }
        }
    }

    private fun addLayout(stat: String, view: LinearLayout) {
        val itemLayout = LayoutInflater.from(this@InsertGameDetailActivity)
            .inflate(R.layout.item_add_goal_or_assist, binding.llCleanSheet, false)
        itemLayout.setOnClickListener { showStatsPopUp(stat, itemLayout) }
        view.addView(itemLayout)
    }

    private fun insertGame() {
        if (checkAllFields()) {
            val homeReference = viewModel.getReference(home)
            val awayReference = viewModel.getReference(away)
            lifecycleScope.launch {
                loadingState()
                viewModel.postGame(
                    homeReference,
                    homeGoals,
                    awayReference,
                    awayGoals,
                    match,
                    journey,
                    id,
                    starterPlayers,
                    benchPlayers.map { it.name },
                    goalList,
                    assistList
                )

                viewModel.stateInsertGame.collect { state ->
                    when (state) {
                        InsertGameInfoState.Loading -> loadingState()
                        InsertGameInfoState.Success -> successState()
                        is InsertGameInfoState.Error -> errorState()
                    }
                }
            }
        }
    }

    private fun updatePlayerStats(players: List<PlayerStats>) {
        for (player in players) {
            if (player.information!!.name in goalList) { player.goals += goalList.count { it == player.information.name } }
            if (player.information.name in assistList) { player.assists += assistList.count { it == player.information.name } }
            if (player.information.name in penaltyList) { player.penalties += penaltyList.count { it == player.information.name } }
            if (player.information.name in cleanSheetList) { player.cleanSheet += 1 }
            if (player.information.name in starterPlayers.values) { player.gamesPlayed += 1 }
            if (player.information.name in benchPlayers.map { it.name }) { player.gamesPlayed += 1 }
            player.lastRanking = player.ranking
            player.percentage = getPercentage(player)
        }
        var count = 1
        for (player in players.sortedByDescending { it.percentage!!.toFloat() }) {
            player.ranking = count
            count += 1
        }
        if (viewModel.insertGameStats(players)){
            finishSuccess()
            restartApp()
        }
    }

    private fun checkAllFields(): Boolean {
        if (goalList.size < homeGoals) {
            Toast.makeText(this, "Please add all the goals", Toast.LENGTH_SHORT).show()
            return false
        } else if (awayGoals == 0 && cleanSheetList.size < 4) {
            Toast.makeText(this, "Please add all the clean sheets", Toast.LENGTH_SHORT).show()
            return false
        } else if (awayGoals == 0 && cleanSheetList.size != cleanSheetList.distinct().size) {
            Toast.makeText(this, "There are two players with the same name in clean sheet list", Toast.LENGTH_SHORT).show()
            return false
        } else if (starterPlayers.containsValue("")) {
            Toast.makeText(this, "Please select all the starters", Toast.LENGTH_SHORT).show()
            return false
        } else if (starterPlayers.containsValue(binding.psBenchPlayer.text.toString())) {
            Toast.makeText(
                this,
                "Player can't be in the bench and starter at the same time",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    private fun loadingStatePlayersInformation() {
        binding.progressBarInsertGame.visibility = View.VISIBLE
    }

    private fun errorStatePlayersInformation() {
        binding.progressBarInsertGame.visibility = View.GONE
        Toast.makeText(this, "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show()
    }

    private fun successGetPlayersInformation(players: MutableList<PlayerInformation>) {
        binding.progressBarInsertGame.visibility = View.GONE
        binding.clMainInsertGameDetail.visibility = View.VISIBLE
        playerList = players
        powerSpinnerBenchList()
    }

    private fun loadingState() {
        binding.progressBarInsertGame.visibility = View.VISIBLE
    }

    private fun errorState(){
        binding.progressBarInsertGame.visibility = View.GONE
        Toast.makeText(this, "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show()
    }

    private fun successState(){
        lifecycleScope.launch {
            viewModel.getAllPlayerInfo()
            viewModel.allPlayersState.collect{
                when (it) {
                    is StatsState.Loading -> {
                        loadingState()
                    }
                    is StatsState.Success -> {
                        val players = it.data
                        updatePlayerStats(players)
                    }
                    is StatsState.Error -> {
                        errorState()
                    }
                }
            }
        }
    }

    private fun finishSuccess() {
        binding.progressBarInsertGame.visibility = View.GONE
        Toast.makeText(this, "Partido insertado correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun restartApp() {
        Thread.sleep(2000)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
        if (applicationContext is Activity) {
            (applicationContext as Activity).finish()
        }
        Runtime.getRuntime().exit(0)
    }

}