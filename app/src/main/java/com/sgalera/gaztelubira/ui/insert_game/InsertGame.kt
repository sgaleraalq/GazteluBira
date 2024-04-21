package com.sgalera.gaztelubira.ui.insert_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.domain.model.MappingUtils
import com.sgalera.gaztelubira.domain.model.Team
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InsertGame : AppCompatActivity() {
    private var homeTeam: Team? = null
    private var awayTeam: Team? = null

    private var teams = arrayListOf(
        "Anaitasuna",
        "Arsenal",
        "Aterbea",
        "ESIC Gazteak",
        "Esmeraldeños",
        "Garre",
        "Iturrama",
        "IZN",
        "La Unica",
        "Peña School",
        "San Cristobal"
    )

    private lateinit var binding: ActivityInsertGameBinding
    private var id: Int = 0
    private var journey: Int = 0
    private var match: String = "liga"

    private val viewModel: InsertGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get the passed arguments
        id = intent.getIntExtra("id", 0)
        journey = intent.getIntExtra("journey", 0)
        initUI()
        initListeners()
    }

    private fun initUI() {
        initComponents()

    }

    private fun initListeners() {
        binding.btnContinue.setOnClickListener {
            if (checkFields()) {
                postGame()
            }
        }
        binding.cvLeague.setOnClickListener {
            match = "liga"
            binding.cvLeague.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.ivLeague.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvLeague.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.cvCup.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
            binding.ivCup.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.tvCup.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.cvCup.setOnClickListener {
            match = "copa"
            binding.cvCup.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.ivCup.setColorFilter(ContextCompat.getColor(this, R.color.white))
            binding.tvCup.setTextColor(ContextCompat.getColor(this, R.color.white))

            binding.cvLeague.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
            binding.ivLeague.setColorFilter(ContextCompat.getColor(this, R.color.black))
            binding.tvLeague.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // Set teams with Power Spinners
        binding.psHomeTeam.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            homeTeam = MappingUtils.mapTeam(newItem)

            // Set away as Gaztelu
            awayTeam = MappingUtils.mapTeam("Gaztelu Bira")
            insertTeams()
        }
        binding.psAwayTeam.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            awayTeam = MappingUtils.mapTeam(newItem)

            // Set local as Gaztelu
            homeTeam = MappingUtils.mapTeam("Gaztelu Bira")
            insertTeams()
        }
    }

    private fun initComponents() {
        binding.psHomeTeam.setItems(teams)
        binding.psAwayTeam.setItems(teams)
    }

    private fun insertTeams() {
        binding.tvHomeTeam.text = this.getString(homeTeam!!.name)
        binding.ivHomeTeam.setImageResource(homeTeam!!.img)

        binding.tvAwayTeam.text = this.getString(awayTeam!!.name)
        binding.ivAwayTeam.setImageResource(awayTeam!!.img)

        binding.ivHomeTeam.visibility = View.VISIBLE
        binding.ivAwayTeam.visibility = View.VISIBLE

        binding.psHomeTeam.clearSelectedItem()
        binding.psAwayTeam.clearSelectedItem()
    }

    private fun checkFields(): Boolean {
        return if (homeTeam != null && awayTeam != null && binding.etHomeGoals.text.toString()
                .isNotEmpty() && binding.etAwayGoals.text.toString().isNotEmpty()
        ) {
            true
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun postGame() {
        lifecycleScope.launch {
            loadingState()
            val state = viewModel.postGame(
                getString(homeTeam!!.name),
                binding.etHomeGoals.text.toString().toInt(),
                getString(awayTeam!!.name),
                binding.etAwayGoals.text.toString().toInt(),
                match,
                journey,
                id
            )
            when (state) {
                InsertGameInfoState.Loading -> loadingState()
                InsertGameInfoState.Success -> successState()
                is InsertGameInfoState.Error -> errorState()
            }
        }
    }

    private fun loadingState() {
        binding.progressBarInsertGame.visibility = View.VISIBLE
    }

    private fun errorState(){
        binding.progressBarInsertGame.visibility = View.GONE
        Toast.makeText(this, "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show()
    }

    private fun successState(){
        binding.progressBarInsertGame.visibility = View.GONE
        Toast.makeText(this, "Partido añadido correctamente", Toast.LENGTH_SHORT).show()
        moveToInsertDetailGameActivity()
    }

    private fun moveToInsertDetailGameActivity() {
        val intent = Intent(this, InsertGameDetailActivity::class.java)
        intent.apply {
            putExtra("homeTeam", homeTeam!!.name)
            putExtra("awayTeam", awayTeam!!.name)
            putExtra("homeGoals", binding.etHomeGoals.text.toString().toInt())
            putExtra("awayGoals", binding.etAwayGoals.text.toString().toInt())
        }
        startActivity(intent)
    }
}