package com.sgalera.gaztelubira.ui.insert_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.sgalera.gaztelubira.R
import com.sgalera.gaztelubira.databinding.ActivityInsertGameBinding
import com.sgalera.gaztelubira.domain.model.TeamInformation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertGame : AppCompatActivity() {
    private var homeTeam: TeamInformation? = null
    private var awayTeam: TeamInformation? = null

    private var teams = arrayListOf(
        "Anaitasuna",
        "Arsenal",
        "Aterbea",
        "Esic Gazteak",
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
                moveToInsertDetailGameActivity()
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
            homeTeam = null

            // Set away as Gaztelu
            awayTeam = null
            insertTeams()
        }
        binding.psAwayTeam.setOnSpinnerItemSelectedListener<String> { _, _, _, newItem ->
            awayTeam = null

            // Set local as Gaztelu
            homeTeam = null
            insertTeams()
        }
    }

    private fun initComponents() {
        binding.psHomeTeam.setItems(teams)
        binding.psAwayTeam.setItems(teams)
    }

    private fun insertTeams() {
        binding.tvHomeTeam.text = homeTeam!!.name
        Glide.with(this).load(homeTeam!!.img).into(binding.ivHomeTeam)

        binding.tvAwayTeam.text = awayTeam!!.name
        Glide.with(this).load(awayTeam!!.img).into(binding.ivAwayTeam)

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

    private fun moveToInsertDetailGameActivity() {
        val intent = Intent(this, InsertGameDetailActivity::class.java)
        intent.apply {
            putExtra("homeTeam", homeTeam!!.name)
            putExtra("awayTeam", awayTeam!!.name)
            putExtra("homeGoals", binding.etHomeGoals.text.toString().toInt())
            putExtra("awayGoals", binding.etAwayGoals.text.toString().toInt())
            putExtra("match", match)
            putExtra("journey", journey)
            putExtra("id", id)
        }
        startActivity(intent)
    }
}